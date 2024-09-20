package nl.fontys.s3.carenestproject.domain.classes.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.carenestproject.domain.classes.Address;
import nl.fontys.s3.carenestproject.domain.classes.Gender;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Address address;
    private Gender gender;
}
