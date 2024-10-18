package nl.fontys.s3.carenestproject.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.service.PatientService;
import nl.fontys.s3.carenestproject.service.request.CreatePatientAccountRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patient")
@AllArgsConstructor
public class PatientController {
    private PatientService patientService;

    @PostMapping()
    public ResponseEntity<Void> createPatientAccount(@RequestBody @Validated CreatePatientAccountRequest request) {
        try{
            patientService.createPatientAccount(request);
            return ResponseEntity.ok().build();
        }
        catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
