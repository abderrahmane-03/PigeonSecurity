package net.yc.race.track.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.yc.race.track.Enum.RoleEnum;
import net.yc.race.track.Enum.Status;
import net.yc.race.track.model.Season;
import net.yc.race.track.service.SeasonService;
import net.yc.race.track.serviceInf.SeasonServiceInf;
import net.yc.race.track.serviceInf.UserServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/roles")
public class RoleController {


    private final UserServiceInf userService;
    @PostMapping("/changeToOrganizer/{id}")
    public ResponseEntity<String> changeToOrganizer(@PathVariable Long id) {
        userService.changeRole(id, RoleEnum.ROLE_ORGANIZER);
        return ResponseEntity.ok("User role changed successfully");
    }
    @PostMapping("/changeToAdmin/{id}")
    public ResponseEntity<String> changeToAdmin(@PathVariable Long id) {
        userService.changeRole(id, RoleEnum.ROLE_ADMIN);
        return ResponseEntity.ok("User role changed successfully");
    }

}
