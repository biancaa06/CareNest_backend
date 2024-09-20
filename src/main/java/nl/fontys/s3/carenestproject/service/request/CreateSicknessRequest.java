package nl.fontys.s3.carenestproject.service.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSicknessRequest {
    @NotBlank(message = "New sickness name cannot be blank")
    private String name;
}
