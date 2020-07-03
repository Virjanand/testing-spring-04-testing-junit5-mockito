package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialitySDJpaServiceTest {

    @Mock
    SpecialtyRepository specialtyRepository;

    @InjectMocks
    SpecialitySDJpaService specialitySDJpaService;

    @Test
    void testDeleteByOjbect() {
        //given
        Speciality speciality = new Speciality();

        //when
        specialitySDJpaService.delete(speciality);

        //then
        then(specialtyRepository).should().delete(any(Speciality.class));
    }

    @Test
    void findByIdTest() {
        //given
        Speciality speciality = new Speciality();
        given(specialtyRepository.findById(1L)).willReturn(Optional.of(speciality));

        //when
        Speciality foundSpeciality = specialitySDJpaService.findById(1L);

        //then
        assertThat(foundSpeciality).isNotNull();
        then(specialtyRepository).should().findById(1L);
        then(specialtyRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void delete() {
        //when
        specialitySDJpaService.delete(new Speciality());

        //then
        then(specialtyRepository).should().delete(any(Speciality.class));
    }

    @Test
    void deleteById() {

        //when
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);

        //then
        then(specialtyRepository).should(times(2)).deleteById(1L);
    }

    @Test
    void deleteByIdAtLeast() {

        //when
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);

        //then
        then(specialtyRepository).should(atLeastOnce()).deleteById(1L);
    }

    @Test
    void deleteByIdAtMost() {

        //when
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);

        //then
        then(specialtyRepository).should(atMost(3)).deleteById(1L);
    }

    @Test
    void deleteByIdNever() {

        //when
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);

        //then
        then(specialtyRepository).should(never()).deleteById(2L);
    }
}