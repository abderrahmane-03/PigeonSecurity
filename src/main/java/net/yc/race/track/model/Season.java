package net.yc.race.track.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.yc.race.track.Enum.Status;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "seasons")
@Data
@NoArgsConstructor
@Getter
@Setter
public class Season {
    @Id
    private String seasonId;
    private Status status;
}
