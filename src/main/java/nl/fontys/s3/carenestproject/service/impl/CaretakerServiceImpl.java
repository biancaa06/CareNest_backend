package nl.fontys.s3.carenestproject.service.impl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.domain.classes.Availability;
import nl.fontys.s3.carenestproject.domain.classes.Role;
import nl.fontys.s3.carenestproject.domain.classes.users.Caretaker;
import nl.fontys.s3.carenestproject.persistance.entity.*;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.*;
import nl.fontys.s3.carenestproject.service.CaretakerService;
import nl.fontys.s3.carenestproject.service.mapping.AvailabilityConverter;
import nl.fontys.s3.carenestproject.service.mapping.CaretakerConverter;
import nl.fontys.s3.carenestproject.service.request.CreateCaretakerAccountRequest;
import nl.fontys.s3.carenestproject.service.request.SicknessInputListRequest;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CaretakerServiceImpl implements CaretakerService {

    private final SicknessOfCaretakerRepo sicknessOfCaretakerRepo;
    private final UserRepo userRepo;
    private final SicknessRepo sicknessRepo;
    private CaretakerRepo caretakerRepo;

    @Override
    public void createCaretakerAccount(CreateCaretakerAccountRequest request) {
        UserEntity baseUser = userRepo.findUserEntityById(request.getBaseUserId());

        if (baseUser == null) {
            throw new InvalidParameterException("The account you are trying to create does not exist");
        }

        if (isBaseUserACaretaker(baseUser)) {
            throw new InvalidParameterException("The account you are trying to create already exists");
        }
        if (request.getSpecialisations() == null || request.getSpecialisations().isEmpty()) {
            throw new IllegalArgumentException("Sicknesses list cannot be empty");
        }

        Availability availability = Availability.fromNumericValue(request.getAvailabilityId());
        CaretakerEntity caretakerEntity = CaretakerEntity.builder()
                .baseUser(baseUser)
                .availability(AvailabilityConverter.convertFromBaseToEntity(availability))
                .personalDescription(request.getPersonalDescription())
                .salaryPerHour(request.getSalaryPerHour())
                .build();

        caretakerRepo.save(caretakerEntity);

        baseUser.setRoleId(RoleEntity.builder()
                .id(Role.CARETAKER.getValue())
                .roleName(Role.CARETAKER.name())
                .build());
        baseUser.setActive(true);
        userRepo.save(baseUser);

        for (SicknessInputListRequest sicknessInput : request.getSpecialisations()) {
            SicknessEntity sicknessEntity = sicknessRepo.findSicknessEntityById(sicknessInput.getSicknessId());
            SicknessesForCaretaker sicknessesForCaretaker = SicknessesForCaretaker.builder()
                    .caretaker(caretakerEntity)
                    .sickness(sicknessEntity)
                    .build();
            sicknessOfCaretakerRepo.save(sicknessesForCaretaker);
        }
    }

    @Override
    public List<Caretaker> getCaretakers() {
        List<CaretakerEntity> caretakerEntities = caretakerRepo.findAll();
        List<Caretaker> caretakers = new ArrayList<>();
        for(CaretakerEntity caretakerEntity : caretakerEntities) {
            caretakers.add(CaretakerConverter.convertFromEntityToBase(caretakerEntity, sicknessOfCaretakerRepo.findSicknessesForCaretakersByCaretaker(caretakerEntity)));
        }
        return caretakers;
    }

    private boolean isBaseUserACaretaker(UserEntity userEntity) {
        return caretakerRepo.existsById(userEntity.getId());
    }
}
