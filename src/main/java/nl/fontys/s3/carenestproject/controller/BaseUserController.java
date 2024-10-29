package nl.fontys.s3.carenestproject.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.service.UserService;
import nl.fontys.s3.carenestproject.service.exception.EmailExistsException;
import nl.fontys.s3.carenestproject.service.request.CreateBaseAccountRequest;
import nl.fontys.s3.carenestproject.service.response.CreateBaseAccountResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/baseUser")
@AllArgsConstructor
public class BaseUserController {
    private final UserService userService;

    @PostMapping()
    public ResponseEntity<?> createBaseAccount(@RequestBody @Validated CreateBaseAccountRequest request) {
        try {
            CreateBaseAccountResponse response = userService.createUser(request);
            if (response == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().body(response);
        } catch (EmailExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getBody());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}
