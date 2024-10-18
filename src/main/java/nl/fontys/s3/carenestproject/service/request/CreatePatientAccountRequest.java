package nl.fontys.s3.carenestproject.service.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreatePatientAccountRequest {
    @NotNull
    private long baseUserId;

    @NotBlank
    private String personalDescription;

    @NotNull(message = "Sicknesses cannot be null")
    @Size(min = 1, message = "At least one sickness must be provided")
    private List<SicknessInputListRequest> sicknesses;
}
