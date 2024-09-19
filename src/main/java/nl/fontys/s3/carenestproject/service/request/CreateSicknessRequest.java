package nl.fontys.s3.carenestproject.service.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSicknessRequest {
    private String name;
}
