package nl.fontys.s3.carenestproject.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailExistsException extends ResponseStatusException {
    public EmailExistsException() {
        super(HttpStatus.BAD_REQUEST,"This email is linked to an already active account");
    }
}
