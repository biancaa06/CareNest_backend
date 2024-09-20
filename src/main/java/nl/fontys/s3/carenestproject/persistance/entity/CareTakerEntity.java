package nl.fontys.s3.carenestproject.persistance.entity;

import lombok.experimental.SuperBuilder;

@SuperBuilder

public class CareTakerEntity{
    private String personalDescription;
    private double salaryPerHour;
    private String availability;
}
