package nl.fontys.s3.carenestproject.service.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateSicknessRequest {
    @NotBlank(message = "New sickness name cannot be blank")
    private String newSicknessName;
}
