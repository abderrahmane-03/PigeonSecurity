package net.yc.race.track.DTO.ResponseDTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.yc.race.track.DTO.EmbededDTO.CompetitionEmbeded;
import net.yc.race.track.Enum.Status;
import net.yc.race.track.model.Competition;
import net.yc.race.track.model.Result;

import java.util.List;

@Data
public class SeasonResponse {
    private Long seasonId;
    private Status status;
    private List<CompetitionEmbeded> competitions;
}

