package nl.fontys.s3.carenestproject.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.service.CaretakerService;
import nl.fontys.s3.carenestproject.service.request.CreateCaretakerAccountRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidParameterException;

@RestController
@RequestMapping("/caretaker")
@AllArgsConstructor
public class CaretakerController {
    private final CaretakerService caretakerService;

    @PostMapping()
    public ResponseEntity<String> createCaretakerAccount(@RequestBody @Validated CreateCaretakerAccountRequest request) {
        try {
            caretakerService.createCaretakerAccount(request);
            return ResponseEntity.ok("Caretaker account created successfully.");
        }
        catch (InvalidParameterException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while creating the caretaker account.");
        }
    }
}
