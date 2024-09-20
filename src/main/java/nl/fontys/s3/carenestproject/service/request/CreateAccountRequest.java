package nl.fontys.s3.carenestproject.service.request;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class CreateAccountRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
}
