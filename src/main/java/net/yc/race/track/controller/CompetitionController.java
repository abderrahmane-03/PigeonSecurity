package net.yc.race.track.controller;

import net.yc.race.track.model.Competition;
import net.yc.race.track.model.Pigeon;
import net.yc.race.track.service.CompetitionService;
import net.yc.race.track.service.PigeonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/competition")
public class CompetitionController {

    @Autowired
    private PigeonService pigeonService;
    @Autowired
    private CompetitionService competitionService;

    @PostMapping("/add")
    public ResponseEntity<String> createCompetition(@RequestBody Competition competition) {
        String seasonId = competition.getSeasonId();
        String result = competitionService.saveCompetition(competition, seasonId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/addPigeonToCompetition/{competitionId}")
    public ResponseEntity<String> updatePigeonToCompetition(
            @PathVariable String competitionId, @RequestBody Integer pigeonId) {

        Optional<Competition> competitionOpt = competitionService.findCompetitionById(competitionId);
        if (competitionOpt.isPresent()) {
            Competition competition = competitionOpt.get();
            if (!competition.getPigeonId().contains(pigeonId)) {
                competition.getPigeonId().add(pigeonId);
                Optional<Pigeon> pigeonOpt = pigeonService.findPigeonById(pigeonId);
                if (pigeonOpt.isPresent()){
                    competitionService.saveCompetition(competition, competition.getSeasonId());
                    return ResponseEntity.ok("Pigeon ajouté à la compétition.");
                }

            } else {
                return ResponseEntity.badRequest().body("Pigeon already added to the competition.");
            }
        } else {
            return ResponseEntity.status(404).body("Competition not found.");
        }
        return ResponseEntity.status(404).body("pigeon not found ");
    }




    @GetMapping
    public List<Competition> findCompetitions(){return competitionService.findCompetitions();}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCompetition(@PathVariable String id) {
        String result = competitionService.deleteCompetitionById(id);
            if ("Competition supprimé avec succès.".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body(result);
        }
    }
}
