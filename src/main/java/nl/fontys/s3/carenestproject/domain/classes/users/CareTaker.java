package nl.fontys.s3.carenestproject.domain.classes.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.carenestproject.domain.classes.Availability;
import nl.fontys.s3.carenestproject.domain.classes.Sickness;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CareTaker{
    private User baseUser;
    private String personalDescription;
    private double salaryPerHour;
    private Availability availability;
    private List<Sickness> specialisations;
}
