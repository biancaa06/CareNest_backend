package nl.fontys.s3.carenestproject.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.configuration.security.token.impl.AccessTokenEncoderDecoderImpl;
import nl.fontys.s3.carenestproject.configuration.security.token.impl.AccessTokenImpl;
import nl.fontys.s3.carenestproject.persistance.entity.RefreshToken;
import nl.fontys.s3.carenestproject.service.RefreshTokenService;
import nl.fontys.s3.carenestproject.service.UserService;
import nl.fontys.s3.carenestproject.service.request.AuthRequest;
import nl.fontys.s3.carenestproject.service.request.RefreshTokenRequest;
import nl.fontys.s3.carenestproject.service.response.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final RefreshTokenService refreshTokenService;
    private final AccessTokenEncoderDecoderImpl tokenService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest loginRequest) {
        AuthResponse loginResponse = userService.login(loginRequest);

        String refreshToken = refreshTokenService.createRefreshToken(tokenService.decode(loginResponse.getAccessToken()).getUserId()).getToken();
        loginResponse.setRefreshToken(refreshToken);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Content-Type", "application/json")
                .body(loginResponse);
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            RefreshToken tokenEntity = refreshTokenService.verifyExpiration(
                    refreshTokenService.findByToken(request.getRefreshToken())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"))
            );

            String newAccessToken = tokenService.encode(
                    new AccessTokenImpl(
                            tokenEntity.getUser().getEmail(),
                            tokenEntity.getUser().getId(),
                            tokenEntity.getUser().getRoleId().getRoleName().lines().toList()
                    )
            );

            AuthResponse response = new AuthResponse(newAccessToken, request.getRefreshToken());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null));
        }
    }
}
