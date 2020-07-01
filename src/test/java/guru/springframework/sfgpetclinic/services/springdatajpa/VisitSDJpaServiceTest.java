package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    @Mock
    private VisitRepository visitRepository;

    @InjectMocks
    private VisitSDJpaService visitSDJpaService;

    @Test
    void findAll() {
        Visit visit = new Visit();

        Set<Visit> visits = new HashSet<>();
        visits.add(visit);
        when(visitRepository.findAll()).thenReturn(visits);

        Set<Visit> foundVisits = visitSDJpaService.findAll();

        verify(visitRepository).findAll();

        assertThat(foundVisits).isEqualTo(visits);
    }

    @Test
    void findById() {
        Visit expectedVisit = new Visit();
        when(visitRepository.findById(anyLong())).thenReturn(java.util.Optional.of(expectedVisit));

        assertThat(visitSDJpaService.findById(1L)).isEqualTo(expectedVisit);
        verify(visitRepository).findById(anyLong());
    }

    @Test
    void save() {
        Visit visit = new Visit();
        when(visitRepository.save(any(Visit.class))).thenReturn(visit);

        assertThat(visitSDJpaService.save(visit)).isEqualTo(visit);

        verify(visitRepository).save(visit);
    }

    @Test
    void delete() {
        Visit visit = new Visit();

        visitSDJpaService.delete(visit);

        verify(visitRepository).delete(visit);
    }

    @Test
    void deleteById() {

        visitSDJpaService.deleteById(1L);

        verify(visitRepository).deleteById(1L);
    }
}