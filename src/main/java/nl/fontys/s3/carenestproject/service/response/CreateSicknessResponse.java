package nl.fontys.s3.carenestproject.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CreateSicknessResponse {
    private long id;
    private String name;
}
