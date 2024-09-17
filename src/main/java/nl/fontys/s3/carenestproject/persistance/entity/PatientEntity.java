package nl.fontys.s3.carenestproject.persistance.entity;

import lombok.experimental.SuperBuilder;

@SuperBuilder

public class PatientEntity{
    private String personalDescription;
    private SicknessEntity sickness;
}
