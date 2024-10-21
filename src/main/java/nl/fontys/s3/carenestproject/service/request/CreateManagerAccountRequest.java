package nl.fontys.s3.carenestproject.service.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateManagerAccountRequest {
    @NotNull
    private long baseUserId;

    @NotNull(message = "Position cannot be null")
    private long position;
}
