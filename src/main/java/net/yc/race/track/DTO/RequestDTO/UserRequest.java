package net.yc.race.track.DTO.RequestDTO;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.yc.race.track.Enum.RoleEnum;
import net.yc.race.track.model.Pigeon;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class UserRequest {
    private Long id;

    @NotBlank(message = "Username cannot be blank.")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters.")
    @Column(nullable = false)
    private String username;

    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    @Column(nullable = false)
    private String password;

    @Size(max = 100, message = "Loft name cannot exceed 100 characters.")
    @Column(nullable = true)
    private String loftName;

    @Pattern(
            regexp = "^[-+]?\\d{1,2}\\.\\d+,[-+]?\\d{1,3}\\.\\d+$",
            message = "GPS coordinates must be in the format 'latitude,longitude'."
    )
    @Column(nullable = true)
    private String gpsCoordinates;

    @Column(nullable = false)
    private RoleEnum role;

}
