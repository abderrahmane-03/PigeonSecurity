package net.yc.race.track.serviceInf;

import lombok.RequiredArgsConstructor;
import net.yc.race.track.model.Competition;
import net.yc.race.track.model.Result;
import net.yc.race.track.model.User;
import net.yc.race.track.repository.CompetitionRepository;
import net.yc.race.track.repository.ResultRepository;
import net.yc.race.track.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ResultServiceInf {
     List<Result> showResult(Long competitionId);
     String saveResult(Result result);
     double calculateDistance(String gps1, String gps2);
     List<Result> findResults();
     String deleteResultById(Long id);
     String exportResultsToPdf(Long competitionId, String outputPath);

}
