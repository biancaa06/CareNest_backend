package nl.fontys.s3.carenestproject.service.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateSicknessRequest {
    @NotNull(message = "Sickness ID cannot be null")
    private long sicknessId;
    @NotBlank(message = "New sickness name cannot be blank")
    private String newSicknessName;
}
