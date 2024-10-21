package nl.fontys.s3.carenestproject.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.service.ManagerService;
import nl.fontys.s3.carenestproject.service.request.CreateManagerAccountRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidParameterException;

@RestController
@RequestMapping("/manager")
@AllArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping()
    public ResponseEntity<String> createManagerAccount(@RequestBody @Validated CreateManagerAccountRequest request) {
        try {
            managerService.createManagerAccount(request);
            return ResponseEntity.ok("Caretaker account created successfully.");
        }
        catch (InvalidParameterException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while creating the manager account.");
        }
    }
}
