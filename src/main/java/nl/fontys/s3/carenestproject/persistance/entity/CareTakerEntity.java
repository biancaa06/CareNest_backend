package nl.fontys.s3.carenestproject.persistance.entity;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.carenestproject.domain.classes.Availability;

@SuperBuilder
@Data
public class CareTakerEntity extends UserEntity {
    private String personalDescription;
    private double salaryPerHour;
    private Availability availability;
}
