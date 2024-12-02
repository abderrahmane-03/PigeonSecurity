package net.yc.race.track.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "competitions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Competition {
    @Id
    private String id;

    @NotBlank(message = "Season ID cannot be blank.")
    private String seasonId;

    @NotBlank(message = "Course name cannot be blank.")
    @Size(max = 100, message = "Course name cannot exceed 100 characters.")
    private String courseName;

    @NotBlank(message = "Coordinates GPS cannot be blank.")
    @Pattern(
            regexp = "^[-+]?\\d{1,2}\\.\\d+,[-+]?\\d{1,3}\\.\\d+$",
            message = "Coordinates GPS must be in the format 'latitude,longitude'."
    )
    private String coordinatesGPS;

    @NotNull(message = "Distance cannot be null.")
    @Positive(message = "Distance must be a positive number.")
    private Long distance;

    @NotNull(message = "Start date and time cannot be null.")
    @Future(message = "Start date and time must be in the future.")
    private Date startDateTime;

    @NotNull(message = "Delay duration cannot be null.")
    private Date delayDuration;

    @NotNull(message = "Pigeon IDs cannot be null.")
    @Size(min = 1, message = "At least one pigeon ID must be provided.")
    private List<Integer> pigeonId;
}
