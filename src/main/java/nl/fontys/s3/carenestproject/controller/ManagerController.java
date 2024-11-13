package nl.fontys.s3.carenestproject.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.domain.classes.users.Manager;
import nl.fontys.s3.carenestproject.service.ManagerService;
import nl.fontys.s3.carenestproject.service.request.CreateManagerAccountRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.List;

@RestController
@RequestMapping("/manager")
@AllArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping()
    public ResponseEntity<String> createManagerAccount(@RequestBody @Validated CreateManagerAccountRequest request) {
        try {
            managerService.createManagerAccount(request);
            return ResponseEntity.ok("Manager account created successfully.");
        }
        catch (InvalidParameterException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while creating the manager account.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manager> getManagerById(@PathVariable long id) {
        Manager manager = managerService.getManagerById(id);
        if (manager != null) {
            return ResponseEntity.ok(manager);
        }
        return ResponseEntity.status(404).body(null);
    }

    @GetMapping("/position/{positionId}")
    public ResponseEntity<List<Manager>> getManagersByPosition(@PathVariable long positionId) {
        return ResponseEntity.ok(managerService.getManagersByPosition(positionId));
    }
}
