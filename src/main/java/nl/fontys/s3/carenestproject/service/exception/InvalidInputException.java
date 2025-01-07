package nl.fontys.s3.carenestproject.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidInputException extends ResponseStatusException {
    public InvalidInputException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
