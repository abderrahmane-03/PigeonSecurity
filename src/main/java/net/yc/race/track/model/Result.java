package net.yc.race.track.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    @Id
    private int id;

    @Min(value = 1, message = "Rank must be a positive number.")
    private int rank;

    @NotBlank(message = "Loft name cannot be blank.")
    @Size(max = 100, message = "Loft name cannot exceed 100 characters.")
    private String loftName;

    @NotBlank(message = "Badge number cannot be blank.")
    @Pattern(regexp = "^\\d{4}$", message = "Badge number must be a 4-digit number.")
    private String numeroDeBadge;

    @NotNull(message = "Arrival hour cannot be null.")
    private Date arriveHour;

    @Min(value = 0, message = "Distance must be a positive number.")
    private double distance;

    @Min(value = 0, message = "Speed must be a positive number.")
    private double speed;

    @Min(value = 0, message = "Points must be a positive number.")
    private double point;

    @Min(value = 0, message = "Adjusted speed must be a positive number.")
    private double adjustedSpeed;

    @NotBlank(message = "Competition ID cannot be blank.")
    private String competitionId;
}
