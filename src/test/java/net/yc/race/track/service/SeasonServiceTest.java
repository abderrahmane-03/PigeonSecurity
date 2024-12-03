package net.yc.race.track.service;

import net.yc.race.track.Enum.Status;
import net.yc.race.track.model.Season;
import net.yc.race.track.repository.SeasonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SeasonServiceTest {

    @Mock
    private SeasonRepository seasonRepository;

    @InjectMocks
    private SeasonService seasonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveSeason_ShouldReturnSavedSeason() {

        Season season = new Season();
        season.setSeasonId((long)1);
        season.setStatus(Status.NOT_YET);

        when(seasonRepository.save(season)).thenReturn(season);


        Season savedSeason = seasonService.saveSeason(season);


        assertEquals(season.getSeasonId(), savedSeason.getSeasonId());
        assertEquals(season.getStatus(), savedSeason.getStatus());
        verify(seasonRepository, times(1)).save(season);
    }

    @Test
    void findSeasons_ShouldReturnListOfSeasons() {
        // Arrange
        Season season1 = new Season();
        season1.setSeasonId((long)1);
        season1.setStatus(Status.NOT_YET);

        Season season2 = new Season();
        season2.setSeasonId((long)2);
        season2.setStatus(Status.DONE);

        List<Season> seasons = Arrays.asList(season1, season2);


        when(seasonRepository.findAll()).thenReturn(seasons);


        List<Season> foundSeasons = seasonService.findSeasons();


        assertEquals(2, foundSeasons.size());
        assertEquals("1", foundSeasons.get(0).getSeasonId());
        assertEquals("2", foundSeasons.get(1).getSeasonId());
        verify(seasonRepository, times(1)).findAll();
    }


    @Test
    void deleteSeasonById_ShouldReturnSuccessMessage_WhenSeasonExists() {

        Long seasonId = (long)1;


        when(seasonRepository.existsById(seasonId)).thenReturn(true);


        String result = seasonService.deleteSeasonById(seasonId);


        assertEquals("Season supprimé avec succès.", result);  // Vérifier le message de succès
        verify(seasonRepository, times(1)).existsById(seasonId);  // Vérifier que existsById a été appelé une fois
        verify(seasonRepository, times(1)).deleteById(seasonId);  // Vérifier que deleteById a été appelé une fois
    }

    @Test
    void deleteSeasonById_ShouldReturnNotFoundMessage_WhenSeasonDoesNotExist() {

        Long seasonId = (long)2;


        when(seasonRepository.existsById(seasonId)).thenReturn(false);


        String result = seasonService.deleteSeasonById(seasonId);


        assertEquals("Season non trouvé.", result);  // Vérifier le message d'erreur
        verify(seasonRepository, times(1)).existsById(seasonId);  // Vérifier que existsById a été appelé une fois
        verify(seasonRepository, times(0)).deleteById(seasonId);  // Vérifier que deleteById n'a pas été appelé
    }
}
