package net.yc.race.track.controller;

import lombok.RequiredArgsConstructor;
import net.yc.race.track.Enum.Status;
import net.yc.race.track.model.Season;
import net.yc.race.track.service.SeasonService;
import net.yc.race.track.serviceInf.SeasonServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/seasons")
public class SeasonController {


    private final SeasonServiceInf seasonService;

    @PostMapping("/add")
    public Season createSeason(@RequestBody Season season) {
        return seasonService.saveSeason(season);
    }

    @PostMapping("/endSeason/{id}")
    public ResponseEntity<?> endSeason(@PathVariable Long id) {
        Optional<Season> seasonOpt = seasonService.findSeasonById(id);
        if (seasonOpt.isPresent()) {
            Season season = seasonOpt.get();
            season.setStatus(Status.DONE);
            seasonService.saveSeason(season);

            // Generate and return rankings for the season
            List<Map<String, Object>> rankings = seasonService.getBreederRankings(id);
            return ResponseEntity.ok(rankings);
        } else {
            return ResponseEntity.status(404).body("Season not found.");
        }
    }

    @GetMapping
    public List<Season> findSeasons(){return seasonService.findSeasons();}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSeason(@PathVariable Long id) {
        String result = seasonService.deleteSeasonById(id);
        if ("Season supprimé avec succès.".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body(result);
        }
    }
}
