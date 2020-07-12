package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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