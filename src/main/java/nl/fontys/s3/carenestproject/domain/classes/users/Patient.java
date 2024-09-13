package nl.fontys.s3.carenestproject.domain.classes.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.fontys.s3.carenestproject.domain.classes.Sickness;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Patient extends User {
    private String personalDescription;
    private Sickness sickness;
}
