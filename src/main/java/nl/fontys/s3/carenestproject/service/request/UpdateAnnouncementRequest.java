package nl.fontys.s3.carenestproject.service.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class UpdateAnnouncementRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;
}
