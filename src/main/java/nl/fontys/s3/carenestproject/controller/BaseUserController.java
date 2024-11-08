package nl.fontys.s3.carenestproject.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.domain.classes.users.User;
import nl.fontys.s3.carenestproject.service.UserService;
import nl.fontys.s3.carenestproject.service.exception.EmailExistsException;
import nl.fontys.s3.carenestproject.service.exception.UserNotActiveException;
import nl.fontys.s3.carenestproject.service.request.CreateBaseAccountRequest;
import nl.fontys.s3.carenestproject.service.request.UpdateUserAddressRequest;
import nl.fontys.s3.carenestproject.service.response.CreateBaseAccountResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<User> getBaseUserAccount(@PathVariable long id) {
        try{
            User user = userService.getUserById(id);
            return ResponseEntity.ok().body(user);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
        catch(EmailExistsException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateAddress/{userId}")
    public ResponseEntity<Void> updateBaseUsersAddress(@RequestBody @Validated UpdateUserAddressRequest request, @PathVariable long userId) {
        try{
            userService.updateUserAddress(request, userId);
            return ResponseEntity.ok().build();
        }
        catch(UserNotActiveException e){
            return ResponseEntity.notFound().build();
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
        catch(Exception  e){
            return ResponseEntity.internalServerError().build();
        }
    }
}
