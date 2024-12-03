package net.yc.race.track.DTO.ResponseDTO;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.yc.race.track.model.Competition;
import net.yc.race.track.model.Pigeon;

import java.util.Date;


@Data
public class ResultResponse {

    private Long id;

    @Min(value = 1, message = "Rank must be a positive number.")
    @Column(nullable = false)
    private int rank;

    @NotBlank(message = "Loft name cannot be blank.")
    @Size(max = 100, message = "Loft name cannot exceed 100 characters.")
    @Column(nullable = false)
    private String loftName;

    @NotBlank(message = "Badge number cannot be blank.")
    @Pattern(regexp = "^\\d{4}$", message = "Badge number must be a 4-digit number.")
    @Column(nullable = false)
    private String numeroDeBadge;

    @NotNull(message = "Arrival hour cannot be null.")
    @Column(nullable = false)
    private Date arriveHour;

    @Min(value = 0, message = "Distance must be a positive number.")
    @Column(nullable = false)
    private double distance;

    @Min(value = 0, message = "Speed must be a positive number.")
    @Column(nullable = false)
    private double speed;

    @Min(value = 0, message = "Points must be a positive number.")
    @Column(nullable = false)
    private double point;

    @Min(value = 0, message = "Adjusted speed must be a positive number.")
    @Column(nullable = false)
    private double adjustedSpeed;

    private Pigeon pigeon;

    private Competition competition;
}
