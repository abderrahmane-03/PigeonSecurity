package net.yc.race.track.controller;

import lombok.RequiredArgsConstructor;
import net.yc.race.track.model.Pigeon;
import net.yc.race.track.service.PigeonService;
import net.yc.race.track.serviceInf.PigeonServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pigeons")
public class PigeonController {

    private final PigeonServiceInf pigeonService;

    @PostMapping("/add")
    public ResponseEntity<String>createPigeon(@RequestBody Pigeon pigeon) {
        Long user_id = pigeon.getUser().getId();
        String result =  pigeonService.savePigeon(pigeon,user_id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public List<Pigeon> findPigeons(){return pigeonService.findPigeons();}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePigeon(@PathVariable Long id) {
        String result = pigeonService.deletePigeonById(id);
        if ("Pigeon supprimé avec succès.".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body(result);
        }
    }

}
