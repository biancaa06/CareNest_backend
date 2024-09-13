package nl.fontys.s3.carenestproject.domain.classes.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.fontys.s3.carenestproject.domain.classes.Availability;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CareTaker extends User {
    private String personalDescription;
    private double salaryPerHour;
    private Availability availability;
}
