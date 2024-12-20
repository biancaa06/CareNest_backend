package nl.fontys.s3.carenestproject.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.configuration.security.auth.RequestAuthenticatedUserProvider;
import nl.fontys.s3.carenestproject.configuration.security.token.AccessToken;
import nl.fontys.s3.carenestproject.domain.classes.users.User;
import nl.fontys.s3.carenestproject.service.UserService;
import nl.fontys.s3.carenestproject.service.request.CreateBaseAccountRequest;
import nl.fontys.s3.carenestproject.service.request.UpdateUserAddressRequest;
import nl.fontys.s3.carenestproject.service.response.CreateBaseAccountResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/baseUser")
@AllArgsConstructor
public class BaseUserController {
    private final UserService userService;
    private final RequestAuthenticatedUserProvider requestAuthenticatedUserProvider;

    @PostMapping()
    public ResponseEntity<?> createBaseAccount(@RequestBody @Validated CreateBaseAccountRequest request) {
        CreateBaseAccountResponse response = userService.createUser(request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    @RolesAllowed({"MANAGER", "CARETAKER", "PATIENT"})
    public ResponseEntity<User> getBaseUserAccount(@PathVariable long id) {
        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();

        if (accessToken == null || accessToken.getUserId() == null){
            return ResponseEntity.status(401).build();
        }
        User user = userService.getUserById(id, accessToken.getUserId());
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/updateAddress/{userId}")
    @RolesAllowed({"MANAGER", "CARETAKER", "PATIENT"})
    public ResponseEntity<Void> updateBaseUsersAddress(@RequestBody @Validated UpdateUserAddressRequest request, @PathVariable long userId) {
        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();

        if (accessToken == null || accessToken.getUserId() == null){
            return ResponseEntity.status(401).build();
        }

        userService.updateUserAddress(request, userId, accessToken.getUserId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/image-upload")
    @RolesAllowed({"MANAGER", "CARETAKER", "PATIENT"})
    public ResponseEntity<Void> uploadImage(@PathVariable long userId, @RequestParam("file") MultipartFile file) {
        try {
            AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();

            if (accessToken == null || accessToken.getUserId() == null){
                return ResponseEntity.status(401).build();
            }

            userService.updateProfilePicture(file, userId, accessToken.getUserId());
            return ResponseEntity.ok().build();
        }
        catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
