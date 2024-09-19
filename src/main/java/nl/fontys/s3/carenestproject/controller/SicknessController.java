package nl.fontys.s3.carenestproject.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.service.SicknessService;
import nl.fontys.s3.carenestproject.service.request.CreateSicknessRequest;
import nl.fontys.s3.carenestproject.service.response.CreateSicknessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sickness")
@AllArgsConstructor
public class SicknessController {

    private final SicknessService sicknessService;

    @PostMapping()
    public ResponseEntity<CreateSicknessResponse> createSickness(@RequestBody @Validated CreateSicknessRequest request) {
        CreateSicknessResponse response = sicknessService.createSickness(request);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(response);
    }
}
