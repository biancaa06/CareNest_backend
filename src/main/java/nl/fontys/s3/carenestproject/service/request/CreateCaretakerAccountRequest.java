package nl.fontys.s3.carenestproject.service.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateCaretakerAccountRequest {
    @NotNull
    private long baseUserId;
    @NotBlank
    private String personalDescription;
    @NotNull
    private double salaryPerHour;
    @NotNull
    private long availabilityId;
    @NotNull
    private List<SicknessInputListRequest> specialisations;
}
