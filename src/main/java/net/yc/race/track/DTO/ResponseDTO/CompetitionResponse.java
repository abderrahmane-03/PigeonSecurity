package net.yc.race.track.DTO.ResponseDTO;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.yc.race.track.DTO.EmbededDTO.PigeonEmbeded;
import net.yc.race.track.DTO.EmbededDTO.ResultEmbeded;
import net.yc.race.track.DTO.EmbededDTO.SeasonEmbeded;
import net.yc.race.track.model.Pigeon;
import net.yc.race.track.model.Result;
import net.yc.race.track.model.Season;

import java.util.Date;
import java.util.List;

@Data
public class CompetitionResponse{

    private Long id;

    @NotBlank(message = "Course name cannot be blank.")
    @Size(max = 100, message = "Course name cannot exceed 100 characters.")
    @Column(nullable = false)
    private String courseName;


    @NotBlank(message = "Coordinates GPS cannot be blank.")
    @Pattern(
            regexp = "^[-+]?\\d{1,2}\\.\\d+,[-+]?\\d{1,3}\\.\\d+$",
            message = "Coordinates GPS must be in the format 'latitude,longitude'."
    )
    @Column(nullable = false)
    private String coordinatesGPS;

    @NotBlank(message = "Distance cannot be null.")
    @Positive(message = "Distance must be a positive number.")
    @Column(name = "distance", nullable = false)
    private Long distance;

    @NotBlank(message = "Start date and time cannot be null.")
    @Future(message = "Start date and time must be in the future.")
    @Column(name = "creation_date", nullable = false)
    private Date startDateTime;

    @NotBlank(message = "Delay duration cannot be null.")
    @Column(name = "delay", nullable = false)
    private Date delayDuration;

    @NotNull(message = "Season ID is required.")
    private SeasonEmbeded season;

    private List<ResultEmbeded> results;

    @Size(min = 1, message = "At least one pigeon must be provided.")
    private List<PigeonEmbeded> pigeons;


}
