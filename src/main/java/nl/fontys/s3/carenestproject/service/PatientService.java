package nl.fontys.s3.carenestproject.service;

import nl.fontys.s3.carenestproject.service.request.CreatePatientAccountRequest;

public interface PatientService {
    void createPatientAccount(CreatePatientAccountRequest request);
    }
