package nl.fontys.s3.carenestproject.service.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAnnouncementRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;
}
