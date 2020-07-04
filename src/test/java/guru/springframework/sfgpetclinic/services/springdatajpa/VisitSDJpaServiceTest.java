package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    @Mock
    private VisitRepository visitRepository;

    @InjectMocks
    private VisitSDJpaService visitSDJpaService;

    @DisplayName("Test find all")
    @Test
    void findAll() {
        //given
        Visit visit = new Visit();
        Set<Visit> visits = new HashSet<>();
        visits.add(visit);
        given(visitRepository.findAll()).willReturn(visits);

        //when
        Set<Visit> foundVisits = visitSDJpaService.findAll();

        //then
        then(visitRepository).should().findAll();
        assertThat(foundVisits).isEqualTo(visits);
    }

    @Test
    void findById() {
        //given
        Visit expectedVisit = new Visit();
        given(visitRepository.findById(anyLong())).willReturn(java.util.Optional.of(expectedVisit));

        //when
        Visit foundVisit = visitSDJpaService.findById(1L);

        //then
        assertThat(foundVisit).isEqualTo(expectedVisit);
        then(visitRepository).should().findById(anyLong());
    }

    @Test
    void save() {
        //given
        Visit visit = new Visit();
        given(visitRepository.save(any(Visit.class))).willReturn(visit);

        //when
        Visit savedVisit = visitSDJpaService.save(visit);

        //then
        assertThat(savedVisit).isEqualTo(visit);
        then(visitRepository).should().save(visit);
    }

    @Test
    void delete() {
        //given
        Visit visit = new Visit();

        //when
        visitSDJpaService.delete(visit);

        //then
        then(visitRepository).should().delete(visit);
    }

    @Test
    void deleteById() {

        //when
        visitSDJpaService.deleteById(1L);

        //then
        then(visitRepository).should().deleteById(1L);
    }
}