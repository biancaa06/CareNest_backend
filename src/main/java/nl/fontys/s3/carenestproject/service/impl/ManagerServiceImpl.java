package nl.fontys.s3.carenestproject.service.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import nl.fontys.s3.carenestproject.domain.classes.Position;
import nl.fontys.s3.carenestproject.domain.classes.Role;
import nl.fontys.s3.carenestproject.domain.classes.users.Manager;
import nl.fontys.s3.carenestproject.persistance.entity.ManagerEntity;
import nl.fontys.s3.carenestproject.persistance.entity.PositionEntity;
import nl.fontys.s3.carenestproject.persistance.entity.RoleEntity;
import nl.fontys.s3.carenestproject.persistance.entity.UserEntity;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.PositionRepo;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.UserRepo;
import nl.fontys.s3.carenestproject.service.ManagerService;
import nl.fontys.s3.carenestproject.service.exception.UserNotActiveException;
import nl.fontys.s3.carenestproject.service.mapping.ManagerConverter;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.ManagerRepo;
import nl.fontys.s3.carenestproject.service.mapping.PositionConverter;
import nl.fontys.s3.carenestproject.service.request.CreateManagerAccountRequest;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@Service
@Builder
@AllArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final PositionRepo positionRepo;
    private final UserRepo userRepo;
    private ManagerRepo managerRepo;

    @Override
    public Manager getManagerById(long id) {
        ManagerEntity managerEntity = managerRepo.findManagerEntityById(id);
        if (managerEntity == null) {
            throw new UserNotActiveException("Manager not found");
        }
        return ManagerConverter.convertFromEntityToBase(managerEntity);
    }


    @Override
    public void createManagerAccount(CreateManagerAccountRequest request) {
        if(!positionRepo.existsById(request.getPosition())){
            throw new InvalidParameterException("Position not found");
        }
        if(!userRepo.existsById(request.getBaseUserId())){
            throw new InvalidParameterException("Base user not found");
        }
        if(managerRepo.existsById(request.getBaseUserId())){
            throw new InvalidParameterException("There already exists a manager account for this user");
        }

        PositionEntity position = PositionEntity.builder()
                .id(request.getPosition())
                .positionName(Position.fromNumericValue(request.getPosition()).name()).build();

        UserEntity baseUser = userRepo.findUserEntityById(request.getBaseUserId());

        ManagerEntity manager = ManagerEntity.builder()
                .baseUser(baseUser)
                .position(position)
                .build();
        managerRepo.save(manager);

        baseUser.setRoleId(RoleEntity.builder()
                .id(Role.MANAGER.getValue())
                .roleName(Role.MANAGER.name())
                .build());
        baseUser.setActive(true);
        userRepo.save(baseUser);

    }

    public List<Manager> getManagersByPosition(long positionId) {
        Position position = Position.fromNumericValue(positionId);
        List<ManagerEntity> managerEntities= managerRepo.findManagerEntitiesByPosition(PositionConverter.convertFromBaseToEntity(position));
        List<Manager> managers = new ArrayList<>();
        for(ManagerEntity manager : managerEntities){
            managers.add(ManagerConverter.convertFromEntityToBase(manager));
        }
        return managers;
    }
}
