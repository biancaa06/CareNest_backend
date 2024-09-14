package nl.fontys.s3.carenestproject.persistance.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.carenestproject.domain.classes.Address;
import nl.fontys.s3.carenestproject.domain.classes.Gender;

@SuperBuilder
@Data
@AllArgsConstructor

public abstract class UserEntity {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Address address;
    private Gender gender;
}
