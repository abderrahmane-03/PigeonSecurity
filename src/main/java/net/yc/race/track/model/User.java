package net.yc.race.track.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import net.yc.race.track.Enum.RoleEnum;
import jakarta.persistence.Id;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(nullable = false)
    private String loftName;

    @Pattern(
            regexp = "^[-+]?\\d{1,2}\\.\\d+,[-+]?\\d{1,3}\\.\\d+$",
            message = "GPS coordinates must be in the format 'latitude,longitude'."
    )
    @Column(nullable = false)
    private String gpsCoordinates;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private List<Pigeon> Pigeons;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role must not be null")
    private RoleEnum role;
}
