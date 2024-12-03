package net.yc.race.track.DTO.ResponseDTO;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.yc.race.track.DTO.EmbededDTO.CompetitionEmbeded;
import net.yc.race.track.DTO.EmbededDTO.ResultEmbeded;
import net.yc.race.track.DTO.EmbededDTO.UserEmbeded;
import net.yc.race.track.model.Competition;
import net.yc.race.track.model.Result;
import net.yc.race.track.model.User;

import java.util.List;

@Data
public class PigeonResponse {


    private Long id;

    @NotBlank(message = "Badge number cannot be blank.")
    @Pattern(regexp = "^\\d{4}$", message = "Badge number must be a 4-digit number.")
    @Column(nullable = false)
    private String numeroDeBadge;

    @Min(value = 1, message = "Age must be greater than 0.")
    @Max(value = 30, message = "Age cannot be greater than 30.")
    @Column(nullable = false)
    private int age;

    @NotBlank(message = "Color cannot be blank.")
    @Size(max = 50, message = "Color cannot exceed 50 characters.")
    @Column(nullable = false)
    private String couleur;

    private CompetitionEmbeded competition;

    private List<ResultEmbeded> results;

    private UserEmbeded user;
}
