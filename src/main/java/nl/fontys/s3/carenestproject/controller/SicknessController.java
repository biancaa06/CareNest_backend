package nl.fontys.s3.carenestproject.controller;

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
    public ResponseEntity<Sickness> getSicknessById(@PathVariable long id) {
        return ResponseEntity.ok(sicknessService.getSicknessById(id));
    }

    @PostMapping()
    public ResponseEntity<CreateSicknessResponse> createSickness(@RequestBody @Validated CreateSicknessRequest request) {
        CreateSicknessResponse response = sicknessService.createSickness(request);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/id:{id}")
    public ResponseEntity<UpdateSicknessResponse> updateSickness(@PathVariable(value = "id") long id, @RequestBody @Validated UpdateSicknessRequest request) {
        try{
            request.setSicknessId(id);
            UpdateSicknessResponse response = sicknessService.updateSickness(request);

            return ResponseEntity.ok().body(response);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
