package nl.fontys.s3.carenestproject.domain.classes.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.fontys.s3.carenestproject.domain.classes.Position;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Manager extends User {
    private Position position;
}
