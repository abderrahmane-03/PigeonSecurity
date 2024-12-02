package net.yc.race.track;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Test {

    @GetMapping("/hi")
    public ResponseEntity<String> hi(){
        return ResponseEntity.ok("here");
    }
}
