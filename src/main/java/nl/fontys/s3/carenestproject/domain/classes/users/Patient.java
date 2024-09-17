package nl.fontys.s3.carenestproject.domain.classes.users;

import lombok.*;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.carenestproject.domain.classes.Sickness;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Patient {
    private User baseUser;
    private String personalDescription;
    private Sickness sickness;
}
