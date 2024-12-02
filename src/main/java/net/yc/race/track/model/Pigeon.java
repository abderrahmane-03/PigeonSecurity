package net.yc.race.track.model;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pigeons")
@Getter
@Setter
@NoArgsConstructor
public class Pigeon {

    @Id
    private int id;

    @NotBlank(message = "User ID cannot be blank.")
    private String user_id;

    @NotBlank(message = "Badge number cannot be blank.")
    @Pattern(regexp = "^\\d{4}$", message = "Badge number must be a 4-digit number.")
    private String numeroDeBadge;

    @Min(value = 1, message = "Age must be greater than 0.")
    @Max(value = 30, message = "Age cannot be greater than 30.")
    private int age;

    @NotBlank(message = "Color cannot be blank.")
    @Size(max = 50, message = "Color cannot exceed 50 characters.")
    private String couleur;
}
