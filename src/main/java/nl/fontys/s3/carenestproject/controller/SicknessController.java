package nl.fontys.s3.carenestproject.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.domain.classes.Sickness;
import nl.fontys.s3.carenestproject.service.SicknessService;
import nl.fontys.s3.carenestproject.service.request.CreateSicknessRequest;
import nl.fontys.s3.carenestproject.service.request.UpdateSicknessRequest;
import nl.fontys.s3.carenestproject.service.response.CreateSicknessResponse;
import nl.fontys.s3.carenestproject.service.response.UpdateSicknessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sickness")
@AllArgsConstructor
public class SicknessController {

    private final SicknessService sicknessService;

    @GetMapping()
    public ResponseEntity<List<Sickness>> getSickness() {
        return ResponseEntity.ok(sicknessService.getAllSicknesses());
    }
    @GetMapping("/id:{id}")
    public ResponseEntity<Sickness> getSicknessById(@PathVariable(value = "id") long id) {
        return ResponseEntity.ok().body(sicknessService.getSicknessById(id));
    }

    @PostMapping()
    @RolesAllowed({"MANAGER"})
    public ResponseEntity<CreateSicknessResponse> createSickness(@RequestBody @Validated CreateSicknessRequest request) {
        CreateSicknessResponse response = sicknessService.createSickness(request);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/id:{id}")
    @RolesAllowed({"MANAGER"})
    public ResponseEntity<UpdateSicknessResponse> updateSickness(@PathVariable(value = "id") long id, @RequestBody @Validated UpdateSicknessRequest request) {
        sicknessService.updateSickness(id,request);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    @RolesAllowed({"MANAGER"})
    public ResponseEntity<Void> deleteSickness(@PathVariable(value = "id") long id) {
        sicknessService.deleteSicknessById(id);
        return ResponseEntity.noContent().build();
    }
}
