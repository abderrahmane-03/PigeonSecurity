package net.yc.race.track.service;

import net.yc.race.track.Enum.Status;
import net.yc.race.track.model.Competition;
import net.yc.race.track.model.Pigeon;
import net.yc.race.track.model.Season;
import net.yc.race.track.model.User;
import net.yc.race.track.repository.CompetitionRepository;
import net.yc.race.track.repository.PigeonRepository;
import net.yc.race.track.repository.SeasonRepository;
import net.yc.race.track.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CompetitionServiceTest {

    @Mock
    private CompetitionRepository competitionRepository;

    @Mock
    private SeasonRepository seasonRepository;

    @Mock
    private PigeonRepository pigeonRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CompetitionService competitionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveCompetition_ShouldReturnErrorMessage_WhenSeasonIsDone() {
        Long seasonId = (long)1;

        // Créer une saison avec un statut DONE
        Season doneSeason = new Season();
        doneSeason.setStatus(Status.DONE);

        // Créer une compétition avec une liste de pigeons
        Competition competition = new Competition();
        List<Long> pigeonIds = new ArrayList<>();
        pigeonIds.add((long)1);
        competition.setPigeonId(pigeonIds);


        Pigeon pigeon = new Pigeon();
        pigeon.setUser_id((long)100);
        when(pigeonRepository.findById((long)1)).thenReturn(Optional.of(pigeon));


        User user = new User();
        user.setGpsCoordinates("48.8566,2.3522");
        when(userRepository.findById((long)100)).thenReturn(Optional.of(user));


        when(seasonRepository.findById(seasonId)).thenReturn(Optional.of(doneSeason));


        String result = competitionService.saveCompetition(competition, seasonId);

        assertEquals("La saison n'est pas active. Impossible d'enregistrer la compétition.", result);
        verify(competitionRepository, never()).save(any(Competition.class));
    }



    @Test
    void saveCompetition_ShouldSaveCompetition_WhenSeasonIsActiveOrNotFound() {
        Long seasonId = (long)2;
        Season activeSeason = new Season();
        activeSeason.setStatus(Status.NOT_YET);

        Competition competition = new Competition();
        List<Long> pigeonIds = new ArrayList<>();
        pigeonIds.add((long)1);
        competition.setPigeonId(pigeonIds);
        Pigeon pigeon = new Pigeon();
        pigeon.setId((long)2);
        pigeon.setUser_id((long)2);

        User user = new User();
        user.setId((long)2);
        user.setGpsCoordinates("48.8566,2.3522");
        competition.setCoordinatesGPS("48.8570,2.3525");
        competition.setDistance((long) 1.0);

        when(seasonRepository.findById(seasonId)).thenReturn(Optional.of(activeSeason));
        when(pigeonRepository.findById((long)1)).thenReturn(Optional.of(pigeon));
        when(userRepository.findById((long)1)).thenReturn(Optional.of(user));

        String result = competitionService.saveCompetition(competition, seasonId);

        assertEquals("Compétition enregistrée avec succès.", result);
        verify(competitionRepository, times(1)).save(competition);
    }



    @Test
    void saveCompetition_ShouldSaveCompetition_WhenSeasonIsNotFound() {

        Long seasonId = (long)3;


        Competition competition = new Competition();
        competition.setPigeonId(new ArrayList<>());
        competition.getPigeonId().add((long)1);


        Pigeon pigeon = new Pigeon();
        pigeon.setUser_id((long)100);

        User user = new User();
        user.setGpsCoordinates("48.8566,2.3522");


        when(seasonRepository.findById(seasonId)).thenReturn(Optional.empty());


        when(pigeonRepository.findById((long)1)).thenReturn(Optional.of(pigeon));


        when(userRepository.findById((long)100)).thenReturn(Optional.of(user));


        competition.setCoordinatesGPS("48.8566,2.3522");
        competition.setDistance((long)0.0);

        String result = competitionService.saveCompetition(competition, seasonId);

        assertEquals("Compétition enregistrée avec succès.", result);
        verify(competitionRepository, times(1)).save(competition);
    }



    @Test
    void findCompetitions_ShouldReturnListOfCompetitions() {

        Competition competition1 = new Competition();
        competition1.setId((long)1);
        competition1.setCourseName("Course 1");

        Competition competition2 = new Competition();
        competition2.setId((long)2);
        competition2.setCourseName("Course 2");

        List<Competition> expectedCompetitions = Arrays.asList(competition1, competition2);

        when(competitionRepository.findAll()).thenReturn(expectedCompetitions);


        List<Competition> actualCompetitions = competitionService.findCompetitions();


        assertEquals(expectedCompetitions.size(), actualCompetitions.size());
        assertEquals(expectedCompetitions, actualCompetitions);
    }

    @Test
    void findCompetitionById_ShouldReturnCompetition_WhenIdExists() {

        Long competitionId = (long)234567890;
        Competition competition = new Competition();
        competition.setId(competitionId);
        competition.setCourseName("Course Test");

        when(competitionRepository.findById(competitionId)).thenReturn(Optional.of(competition));


        Optional<Competition> result = competitionService.findCompetitionById(competitionId);


        assertTrue(result.isPresent());
        assertEquals(competition, result.get());
    }

    @Test
    void findCompetitionById_ShouldReturnEmptyOptional_WhenIdDoesNotExist() {

        Long competitionId = (long)234567890;
        when(competitionRepository.findById(competitionId)).thenReturn(Optional.empty());


        Optional<Competition> result = competitionService.findCompetitionById(competitionId);


        assertTrue(result.isEmpty());
    }


    @Test
    void deleteCompetitionById_ShouldReturnSuccessMessage_WhenCompetitionExists() {

        Long competitionId = (long)1;
        when(competitionRepository.existsById(competitionId)).thenReturn(true);


        String result = competitionService.deleteCompetitionById(competitionId);


        assertEquals("Competition supprimé avec succès.", result);
        verify(competitionRepository, times(1)).deleteById(competitionId);
    }

    @Test
    void deleteCompetitionById_ShouldReturnNotFoundMessage_WhenCompetitionDoesNotExist() {

        Long competitionId = (long)234567890;
        when(competitionRepository.existsById(competitionId)).thenReturn(false);


        String result = competitionService.deleteCompetitionById(competitionId);


        assertEquals("Competition non trouvé.", result);
        verify(competitionRepository, never()).deleteById(competitionId);
    }

}
