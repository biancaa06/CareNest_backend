package nl.fontys.s3.carenestproject.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.service.UserService;
import nl.fontys.s3.carenestproject.service.request.AuthRequest;
import nl.fontys.s3.carenestproject.service.response.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest loginRequest) {
        AuthResponse loginResponse = userService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Content-Type", "application/json")
                .body(loginResponse);
    }
}
