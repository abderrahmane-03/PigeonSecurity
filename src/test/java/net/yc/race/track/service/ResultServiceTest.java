package net.yc.race.track.service;

import net.yc.race.track.model.Competition;
import net.yc.race.track.model.Result;
import net.yc.race.track.model.User;
import net.yc.race.track.repository.ResultRepository;
import net.yc.race.track.repository.CompetitionRepository;
import net.yc.race.track.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResultServiceTest {

    @Mock
    private ResultRepository resultRepository;

    @Mock
    private CompetitionRepository competitionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ResultService resultService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void showResult_ShouldReturnTop25PercentResults() {
        // Arrange: create mock data
        Long competitionId = (long)1;


        Result result1 = new Result((long)1, 0, "Loft A", "badge1", new Date(), 100.0, 80.0,0.0,1.2, competitionId);
        Result result2 = new Result((long)2, 0, "Loft B", "badge2", new Date(), 150.0, 90.0, 0.0,1.2, competitionId);
        Result result3 = new Result((long)3, 0, "Loft C", "badge3", new Date(), 120.0, 85.0, 0.0,1.2, competitionId);
        Result result4 = new Result((long)4, 0, "Loft D", "badge4", new Date(), 130.0, 70.0, 0.0, 1.2,competitionId);

        List<Result> allResults = new ArrayList<>();
        allResults.add(result1);
        allResults.add(result2);
        allResults.add(result3);
        allResults.add(result4);


        // Mock the repository call
        when(resultRepository.findAllByCompetitionId(eq(competitionId), any(Sort.class))).thenReturn(allResults);

        // Act: call the method under test
        List<Result> topResults = resultService.showResult(competitionId);


        // Assert: verify the results
        assertNotNull(topResults);

        // Verify that we get a list of results with ranks and points
        assertTrue(topResults.size() > 0, "Top results should not be empty.");
        assertEquals(1, topResults.get(0).getRank());
        assertEquals(100.0, topResults.get(0).getPoint());


        // Ensure that points decrease with rank
        if (topResults.size() > 1) {
            assertTrue(topResults.get(0).getPoint() > topResults.get(1).getPoint());
        }


        // Verify interactions with the repository
        verify(resultRepository, times(1)).findAllByCompetitionId(eq(competitionId), any(Sort.class));
    }


    @Test
    void showResult_ShouldHandleNoResults() {
        // Arrange: no results
        Long competitionId = (long)2;
        List<Result> allResults = new ArrayList<>();

        // Mock the repository call
        when(resultRepository.findAllByCompetitionId(eq(competitionId), any(Sort.class))).thenReturn(allResults);

        // Act: call the method under test
        List<Result> topResults = resultService.showResult(competitionId);

        // Assert: verify that no results are returned
        assertNotNull(topResults);
        assertTrue(topResults.isEmpty());

        // Verify interactions with the repository
        verify(resultRepository, times(1)).findAllByCompetitionId(eq(competitionId), any(Sort.class));
    }

    @Test
    void showResult_ShouldHandleSingleResult() {
        // Arrange: only one result
        Long competitionId = (long)3;

        Result result1 = new Result((long)1, 0, "Loft A", "badge1", new Date(), 100.0, 80.0, 0.0, 1.2,competitionId);


        List<Result> allResults = new ArrayList<>();
        allResults.add(result1);

        // Mock the repository call
        when(resultRepository.findAllByCompetitionId(eq(competitionId), any(Sort.class))).thenReturn(allResults);

        // Act: call the method under test
        List<Result> topResults = resultService.showResult(competitionId);

        // Assert: verify the result
        assertNotNull(topResults);
        assertEquals(1, topResults.size());
        assertEquals(1, topResults.get(0).getRank());
        assertEquals(100.0, topResults.get(0).getPoint());

        // Verify interactions with the repository
        verify(resultRepository, times(1)).findAllByCompetitionId(eq(competitionId), any(Sort.class));
    }

    @Test
    void saveResult_ShouldSaveResult_WhenCompetitionAndUserAreValid() {
        // Arrange
        Long competitionId = (long)1;
        String loftName = "Loft A";

        Competition competition = new Competition();
        competition.setId(competitionId);
        competition.setStartDateTime(Date.from(Instant.now().minus(10, ChronoUnit.MINUTES)));
        competition.setDelayDuration(Date.from(Instant.now().plus(20, ChronoUnit.MINUTES)));
        competition.setCoordinatesGPS("48.8566,2.3522"); // Paris coordinates for example

        User user = new User();
        user.setLoftName(loftName);
        user.setGpsCoordinates("48.8566,2.3522"); // Paris coordinates for example

        Result result = new Result();
        result.setCompetitionId(competitionId);
        result.setLoftName(loftName);
        result.setArriveHour(Date.from(Instant.now()));

        // Mock repository calls
        when(competitionRepository.findById(competitionId)).thenReturn(Optional.of(competition));
        when(userRepository.findByLoftName(loftName)).thenReturn(Optional.of(user));

        // Act
        String response = resultService.saveResult(result);

        // Assert
        assertEquals("Result enregistré avec succès.", response);
        verify(resultRepository, times(1)).save(result);
    }

//    @Test
//    void saveResult_ShouldReturnError_WhenCompetitionDelayDurationPassed() {
//        // Arrange
//        String competitionId = "competition1";
//        String loftName = "Loft A";
//
//        Competition competition = new Competition();
//        competition.setId(competitionId);
//        competition.setStartDateTime(Date.from(Instant.now().minus(30, ChronoUnit.MINUTES)));
//        competition.setDelayDuration(Date.from(Instant.now().minus(5, ChronoUnit.MINUTES))); // Delay already passed
//        competition.setCoordinatesGPS("48.8566,2.3522"); // Paris coordinates for example
//
//        User user = new User();
//        user.setLoftName(loftName);
//        user.setGpsCoordinates("48.8566,2.3522"); // Paris coordinates for example
//
//        Result result = new Result();
//        result.setCompetitionId(competitionId);
//        result.setLoftName(loftName);
//        result.setArriveHour(Date.from(Instant.now()));
//
//        // Mock repository calls
//        when(competitionRepository.findById(competitionId)).thenReturn(Optional.of(competition));
//        when(userRepository.findByLoftName(loftName)).thenReturn(Optional.of(user));
//
//        // Act
//        String response = resultService.saveResult(result);
//
//        // Assert
//        assertEquals("The competition delay duration has already passed. Result cannot be added.", response);
//        verify(resultRepository, never()).save(result);
//    }

    @Test
    void saveResult_ShouldReturnError_WhenCompetitionOrUserNotFound() {
        // Arrange
        Long competitionId = (long)1;
        String loftName = "Loft A";

        Result result = new Result();
        result.setCompetitionId(competitionId);
        result.setLoftName(loftName);

        // Mock repository calls
        when(competitionRepository.findById(competitionId)).thenReturn(Optional.empty());
        when(userRepository.findByLoftName(loftName)).thenReturn(Optional.empty());

        // Act
        String response = resultService.saveResult(result);

        // Assert
        assertEquals("Competition or User not found.", response);
        verify(resultRepository, never()).save(result);
    }

    @Test
    void calculateDistance_ShouldReturnCorrectDistance() {
        // Arrange
        String parisCoordinates = "48.8566,2.3522"; // Paris
        String londonCoordinates = "51.5074,-0.1278"; // London

        // Act
        double distance = resultService.calculateDistance(parisCoordinates, londonCoordinates);

        // Assert
        // Expected distance between Paris and London is approximately 343 km
        assertEquals(343, distance, 1.0); // Allowing a 1 km tolerance
    }

    @Test
    void calculateDistance_ShouldReturnZero_WhenCoordinatesAreTheSame() {
        // Arrange
        String coordinates = "48.8566,2.3522"; // Paris

        // Act
        double distance = resultService.calculateDistance(coordinates, coordinates);

        // Assert
        assertEquals(0.0, distance, 0.0);
    }

    @Test
    void calculateDistance_ShouldHandleNegativeCoordinates() {
        // Arrange
        String sydneyCoordinates = "-33.8688,151.2093"; // Sydney
        String tokyoCoordinates = "35.6895,139.6917"; // Tokyo

        // Act
        double distance = resultService.calculateDistance(sydneyCoordinates, tokyoCoordinates);

        // Assert
        // Expected distance between Sydney and Tokyo is approximately 7,812 km
        assertEquals(7812, distance, 20.0); // Allowing a 20 km tolerance
    }

}

