package nl.fontys.s3.carenestproject.controller;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.service.UserService;
import nl.fontys.s3.carenestproject.service.request.ResetPasswordRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reset-password")
@AllArgsConstructor
public class ResetPasswordController {

    private final UserService userService;

    @PostMapping("/verify-email/{email}")
    public ResponseEntity<Void> verifyEmail(@PathVariable @Email String email) {
        userService.sendForgotPassword(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/new-password/{email}")
    public ResponseEntity<Void> resetPassword (@PathVariable String email ,@RequestBody ResetPasswordRequest resetPasswordRequest) {
        userService.resetPassword(email, resetPasswordRequest);
        return ResponseEntity.ok().build();
    }
}
