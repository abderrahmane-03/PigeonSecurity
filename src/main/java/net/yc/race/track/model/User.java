package net.yc.race.track.model;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    private String id;

    @NotBlank(message = "Username cannot be blank.")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters.")
    private String username;

    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;

    @NotBlank(message = "Loft name cannot be blank.")
    @Size(max = 100, message = "Loft name cannot exceed 100 characters.")
    private String loftName;

    @NotBlank(message = "GPS coordinates cannot be blank.")
    @Pattern(
            regexp = "^[-+]?\\d{1,2}\\.\\d+,[-+]?\\d{1,3}\\.\\d+$",
            message = "GPS coordinates must be in the format 'latitude,longitude'."
    )
    private String gpsCoordinates;

    public User(String id, String username, String password, String loftName, String gpsCoordinates) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.loftName = loftName;
        this.gpsCoordinates = gpsCoordinates;
    }
}
