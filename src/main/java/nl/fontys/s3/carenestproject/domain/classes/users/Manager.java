package nl.fontys.s3.carenestproject.domain.classes.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.carenestproject.domain.classes.Position;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Manager extends User {
    private Position position;
}
