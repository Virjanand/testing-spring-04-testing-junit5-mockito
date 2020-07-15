package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    @Mock
    OwnerService ownerService;

    @Mock
    BindingResult result;

    @InjectMocks
    OwnerController ownerController;

    @Captor
    ArgumentCaptor<String> stringCaptor;

    private void setUp() {
        given(ownerService.findAllByLastNameLike(stringCaptor.capture()))
                .willAnswer(invocation -> {
                    List<Owner> owners = new ArrayList<>();
                    String name = invocation.getArgument(0);
                    if (name.equals("%Buck%")) {
                        owners.add(new Owner(1L, "Jane", "Buck"));
                        return owners;
                    } else if (name.equals("%DontFindMe%")) {
                        return owners;
                    } else if (name.equals("%FindMe%")) {
                        owners.add(new Owner(1L, "Jane", "Buck"));
                        owners.add(new Owner(1L, "Jane", "Buck"));
                        return owners;
                    }
                    throw new RuntimeException("Invalid argument");
                });
    }

    @Test
    void processFindFromWildcardStringAnnotation() {
        //given
        setUp();
        Owner owner = new Owner(1L, "Jane", "Buck");

        //when
        String viewName = ownerController.processFindForm(owner, result, null);

        //then
        assertThat("%Buck%").isEqualToIgnoringCase(stringCaptor.getValue());
        assertThat("redirect:/owners/1").isEqualToIgnoringCase(viewName);
    }

    @Test
    void processFindFromWildcardStringAnnotationNotFound() {
        //given
        setUp();
        Owner owner = new Owner(1L, "Jane", "DontFindMe");

        //when
        String viewName = ownerController.processFindForm(owner, result, null);

        //then
        assertThat("%DontFindMe%").isEqualToIgnoringCase(stringCaptor.getValue());
        assertThat("owners/findOwners").isEqualToIgnoringCase(viewName);
    }

    @Test
    void processFindFromWildcardStringAnnotationFindMultiple() {
        //given
        setUp();
        Owner owner = new Owner(1L, "Jane", "FindMe");

        //when
        String viewName = ownerController.processFindForm(owner, result, mock(Model.class));

        //then
        assertThat("%FindMe%").isEqualToIgnoringCase(stringCaptor.getValue());
        assertThat("owners/ownersList").isEqualToIgnoringCase(viewName);
    }

    @Test
    void processFindFromWildcardString() {
        //given
        Owner owner = new Owner(1L, "Jane", "Buck");
        List<Owner> ownerList = new ArrayList<>();
//        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        given(ownerService.findAllByLastNameLike(stringCaptor.capture())).willReturn(ownerList);

        //when
        String viewName = ownerController.processFindForm(owner, result, null);

        //then
        assertThat(stringCaptor.getValue()).isEqualToIgnoringCase("%Buck%");
    }

    @Test
    void processCreationFormWithErrors() {
        when(result.hasErrors()).thenReturn(true);
        Owner owner = new Owner(1L, "Billy", "Elliot");

        assertThat(ownerController.processCreationForm(owner, result)).isEqualTo("owners/createOrUpdateOwnerForm");
    }

    @Test
    void processCreationForm() {
        Owner owner = new Owner(1L, "Billy", "Elliot");
        when(ownerService.save(argThat(arg -> arg.getId() == 1L))).thenReturn(owner);

        assertThat(ownerController.processCreationForm(owner, result)).isEqualTo("redirect:/owners/1");

        verify(ownerService).save(owner);
    }
}