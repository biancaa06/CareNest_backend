package nl.fontys.s3.carenestproject.domain.classes.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.carenestproject.domain.classes.Position;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Manager {
    private User baseUser;
    private Position position;
}
