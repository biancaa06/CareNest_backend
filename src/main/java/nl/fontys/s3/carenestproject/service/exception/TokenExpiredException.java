package nl.fontys.s3.carenestproject.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TokenExpiredException extends ResponseStatusException {
    public TokenExpiredException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
