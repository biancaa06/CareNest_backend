package nl.fontys.s3.carenestproject.service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {
    private String message;
    private String details;
}
