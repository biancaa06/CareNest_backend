package nl.fontys.s3.carenestproject.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.domain.classes.users.Caretaker;
import nl.fontys.s3.carenestproject.service.CaretakerService;
import nl.fontys.s3.carenestproject.service.request.CreateCaretakerAccountRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while creating the caretaker account.");
        }
    }

    @GetMapping()
    public ResponseEntity<List<Caretaker>> getCaretakers() {
        try{
            return ResponseEntity.ok(caretakerService.getCaretakers());
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}
