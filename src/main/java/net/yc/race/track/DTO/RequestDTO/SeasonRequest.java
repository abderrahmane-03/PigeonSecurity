package net.yc.race.track.DTO.RequestDTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.yc.race.track.Enum.Status;
import net.yc.race.track.model.Competition;

import java.util.List;

@Data
public class SeasonRequest {
    private Long seasonId;
    private Status status;
}

