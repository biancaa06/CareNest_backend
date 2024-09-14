package nl.fontys.s3.carenestproject.persistance.entity;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.stereotype.Repository;

@SuperBuilder
@Data
public class AddressEntity  {

    private long id;
    private String country;
    private String city;
    private String street;
    private int number;
}
