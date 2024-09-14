package nl.fontys.s3.carenestproject.persistance.entity;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class AddressEntity {
    private long id;
    private String Country;
    private String City;
    private String Street;
    private int number;
}
