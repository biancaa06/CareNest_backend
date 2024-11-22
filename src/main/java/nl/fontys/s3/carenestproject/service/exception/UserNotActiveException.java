package nl.fontys.s3.carenestproject.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotActiveException extends ResponseStatusException {
    public UserNotActiveException() {
      super(HttpStatus.NOT_FOUND,"This is not an active user");
    }
    public UserNotActiveException(String message){
      super(HttpStatus.NOT_FOUND,message);
    }
}
