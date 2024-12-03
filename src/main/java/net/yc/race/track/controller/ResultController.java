package net.yc.race.track.controller;

import lombok.RequiredArgsConstructor;
import net.yc.race.track.model.Result;
import net.yc.race.track.service.ResultService;
import net.yc.race.track.serviceInf.ResultServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/results")
public class ResultController {


    private final ResultServiceInf resultService;

    @PostMapping("/add")
    public ResponseEntity<String> createResult(@RequestBody Result result) {
        String saveMessage = resultService.saveResult(result);

        if ("Result ajouté avec succès.".equals(saveMessage)) {
            return ResponseEntity.ok(saveMessage);
        } else {
            return ResponseEntity.status(404).body(saveMessage);
        }
    }

    @GetMapping("/export/{competitionId}")
    public ResponseEntity<String> exportResults(@PathVariable Long competitionId) {
        String outputPath = "C:/temp/results.pdf";
        String message = resultService.exportResultsToPdf(competitionId, outputPath);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/show/{competitionId}")
    public ResponseEntity<List<Result>> showResults(@PathVariable Long competitionId) {
        List<Result> results = resultService.showResult(competitionId);
        if (results.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        } else {
            return ResponseEntity.ok(results);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteResult(@PathVariable Long id) {
        String result = resultService.deleteResultById(id);
        if ("Result supprimé avec succès.".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body(result);
        }
    }

}
