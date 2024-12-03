package net.yc.race.track.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "pigeons")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pigeon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    @NotNull(message = "competition ID is required.")
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;

    @OneToMany(mappedBy = "pigeon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Result> results;

    @ManyToOne
    @NotNull(message = "User ID is required.")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
