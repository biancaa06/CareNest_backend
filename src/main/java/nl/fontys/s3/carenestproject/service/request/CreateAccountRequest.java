package nl.fontys.s3.carenestproject.service.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class CreateAccountRequest {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "^\\+?(\\d{1,3})?[-.\\s]?(\\(?\\d{3}\\)?)?[-.\\s]?(\\d{3})[-.\\s]?(\\d{4})$",
            message = "Invalid phone number format")
    private String phoneNumber;
    @NotBlank
    private String password;
}
