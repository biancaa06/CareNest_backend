package nl.fontys.s3.carenestproject.persistance.entity;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.carenestproject.domain.classes.Sickness;

@SuperBuilder

public class PatientEntity extends UserEntity {
    private String personalDescription;
    private Sickness sickness;
}
