package nl.fontys.s3.carenestproject.service.mapping;

import nl.fontys.s3.carenestproject.domain.classes.Position;
import nl.fontys.s3.carenestproject.domain.classes.users.Manager;
import nl.fontys.s3.carenestproject.persistance.entity.ManagerEntity;
import nl.fontys.s3.carenestproject.persistance.entity.PositionEntity;

public final class ManagerConverter {
    private ManagerConverter() {}

    public static Manager convertFromEntityToBase(ManagerEntity managerEntity) {
        return Manager.builder()
                .baseUser(BaseUserConverter.convertFromEntityToBase(managerEntity.getBaseUser()))
                .position(Position.valueOf(managerEntity.getPosition().getPositionName()))
                .build();

    }

    public static ManagerEntity convertFromBaseToEntity(Manager manager) {
        return ManagerEntity.builder()
                .id(manager.getBaseUser().getId())
                .baseUser(BaseUserConverter.convertFromBaseToEntity(manager.getBaseUser()))
                .position(PositionEntity.builder()
                        .id(manager.getPosition().getValue())
                        .positionName(manager.getPosition().toString())
                        .build())
                .build();
    }
}
