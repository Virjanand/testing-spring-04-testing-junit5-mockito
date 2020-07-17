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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

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
        then(specialtyRepository).should(timeout(100)).findById(1L);
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
        then(specialtyRepository).should(timeout(100).times(2)).deleteById(1L);
    }

    @Test
    void deleteByIdAtLeast() {

        //when
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);

        //then
        then(specialtyRepository).should(timeout(100).atLeastOnce()).deleteById(1L);
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

    @Test
    void testDoThrow() {
        doThrow(new RuntimeException("boom")).when(specialtyRepository).delete(any());

        assertThrows(RuntimeException.class, () -> specialtyRepository.delete(new Speciality()));

        verify(specialtyRepository).delete(any());
    }


    @Test
    void testFindByIDThrows() {
        given(specialtyRepository.findById(1L)).willThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> specialitySDJpaService.findById(1L));

        then(specialtyRepository).should().findById(1L);
    }

    @Test
    void testDeleteBDD() {
        willThrow(new RuntimeException("boom")).given(specialtyRepository).delete(any());

        assertThrows(RuntimeException.class, () -> specialtyRepository.delete(new Speciality()));

        then(specialtyRepository).should().delete(any());
    }

    @Test
    void testSaveLambda() {
        //given
        final String MATCH_ME = "MATCH_ME";
        Speciality speciality = new Speciality();
        speciality.setDescription(MATCH_ME);

        Speciality savedSpeciality = new Speciality();
        savedSpeciality.setId(1L);

        //need mock to only return on match MATCH_ME string
        given(specialtyRepository.save(argThat(argument -> argument.getDescription().equals(MATCH_ME)))).willReturn(savedSpeciality);

        //when
        Speciality returnedSpeciality = specialitySDJpaService.save(speciality);

        //then
        assertThat(returnedSpeciality.getId()).isEqualTo(1L);
    }
}