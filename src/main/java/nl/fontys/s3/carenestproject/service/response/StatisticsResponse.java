package nl.fontys.s3.carenestproject.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StatisticsResponse {
    private Long totalCaretakers;
    private Long totalPatients;
    private Double caretakerToPatientRatio;
}
