package nl.fontys.s3.carenestproject.service.impl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.domain.classes.Role;
import nl.fontys.s3.carenestproject.persistance.entity.*;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.PatientRepo;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.SicknessOfPatientRepo;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.SicknessRepo;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.UserRepo;
import nl.fontys.s3.carenestproject.service.PatientService;
import nl.fontys.s3.carenestproject.service.request.CreatePatientAccountRequest;
import nl.fontys.s3.carenestproject.service.request.SicknessInputListRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final UserRepo userRepo;
    private final SicknessOfPatientRepo sicknessOfPatientRepo;
    private final SicknessRepo sicknessRepo;
    private PatientRepo patientRepo;

    @Override
    public void createPatientAccount(CreatePatientAccountRequest request) {
        if (request.getSicknesses() == null || request.getSicknesses().isEmpty()) {
            throw new IllegalArgumentException("Sicknesses list cannot be empty");
        }

        UserEntity baseUser = userRepo.findUserEntityById(request.getBaseUserId());
        PatientEntity patient = PatientEntity.builder()
                .baseUser(baseUser)
                .personalDescription(request.getPersonalDescription())
                .build();
        baseUser.setRoleId(RoleEntity.builder()
                .id(Role.PATIENT.getValue())
                .roleName(Role.PATIENT.name())
                .build());
        baseUser.setActive(true);
        patientRepo.save(patient);
        userRepo.save(baseUser);

        // Validate all sicknesses before saving
        for (SicknessInputListRequest sicknessRequest : request.getSicknesses()) {
            SicknessEntity sicknessEntity = sicknessRepo.findSicknessEntityById(sicknessRequest.getSicknessId());
            if (sicknessEntity == null) {
                throw new IllegalArgumentException("Sickness with ID " + sicknessRequest.getSicknessId() + " not found");
            }
        }

        // Save all sicknesses after validation
        request.getSicknesses().forEach(sicknessRequest -> {
            SicknessEntity sicknessEntity = sicknessRepo.findSicknessEntityById(sicknessRequest.getSicknessId());
            SicknessesOfPatient sicknessItem = SicknessesOfPatient.builder()
                    .patientId(patient)
                    .sicknessId(sicknessEntity)
                    .build();
            sicknessOfPatientRepo.save(sicknessItem);
        });
    }



}
