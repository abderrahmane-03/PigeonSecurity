package net.yc.race.track.controller;

import lombok.RequiredArgsConstructor;
import net.yc.race.track.model.Competition;
import net.yc.race.track.model.Pigeon;
import net.yc.race.track.service.CompetitionService;
import net.yc.race.track.service.PigeonService;
import net.yc.race.track.serviceInf.CompetitionServiceInf;
import net.yc.race.track.serviceInf.PigeonServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/competition")
public class CompetitionController {


    private final PigeonServiceInf pigeonService;

    private final CompetitionServiceInf competitionService;

    @PostMapping("/add")
    public ResponseEntity<String> createCompetition(@RequestBody Competition competition) {
        Long seasonId = competition.getSeason().getSeasonId();
        String result = competitionService.saveCompetition(competition, seasonId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/addPigeonToCompetition/{competitionId}")
    public ResponseEntity<String> updatePigeonToCompetition(
            @PathVariable Long competitionId, @RequestBody Long pigeonId) {

        Optional<Competition> competitionOpt = competitionService.findCompetitionById(competitionId);
        if (competitionOpt.isPresent()) {
            Competition competition = competitionOpt.get();

            Optional<Pigeon> pigeonOpt = pigeonService.findPigeonById(pigeonId);
            if (pigeonOpt.isPresent()) {
                Pigeon pigeon = pigeonOpt.get();

                if (!competition.getPigeons().contains(pigeon)) {
                    competition.getPigeons().add(pigeon);
                    pigeon.setCompetition(competition); // Ensure bidirectional relationship is maintained
                    competitionService.saveCompetition(competition, competition.getSeason().getSeasonId());
                    return ResponseEntity.ok("Pigeon ajouté à la compétition.");
                } else {
                    return ResponseEntity.badRequest().body("Pigeon already added to the competition.");
                }
            } else {
                return ResponseEntity.status(404).body("Pigeon not found.");
            }
        } else {
            return ResponseEntity.status(404).body("Competition not found.");
        }
    }





    @GetMapping
    public List<Competition> findCompetitions(){return competitionService.findCompetitions();}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCompetition(@PathVariable Long id) {
        String result = competitionService.deleteCompetitionById(id);
            if ("Competition supprimé avec succès.".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body(result);
        }
    }
}
