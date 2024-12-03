package net.yc.race.track.DTO.EmbededDTO;

import jakarta.persistence.*;
import lombok.Data;
import net.yc.race.track.Enum.Status;
import net.yc.race.track.model.Competition;

import java.util.List;

@Data
public class SeasonEmbeded {
    private Long seasonId;
    private Status status;
}

