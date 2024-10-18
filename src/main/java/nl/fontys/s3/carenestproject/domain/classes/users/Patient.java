package nl.fontys.s3.carenestproject.domain.classes.users;

import lombok.*;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.carenestproject.domain.classes.Sickness;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Patient {
    private User baseUser;
    private String personalDescription;
    private List<Sickness> sickness;
}
