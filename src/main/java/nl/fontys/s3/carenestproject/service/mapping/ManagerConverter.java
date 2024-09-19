package nl.fontys.s3.carenestproject.service.mapping;

import nl.fontys.s3.carenestproject.domain.classes.Position;
import nl.fontys.s3.carenestproject.domain.classes.users.Manager;
import nl.fontys.s3.carenestproject.persistance.entity.ManagerEntity;

public final class ManagerConverter {
    private ManagerConverter() {}

    public static Manager convertFromEntityToBase(ManagerEntity managerEntity) {
        return Manager.builder()
                .baseUser(BaseUserConverter.convertFromEntityToBase(managerEntity.getBaseUser()))
                .position(Position.valueOf(managerEntity.getPosition()))
                .build();

    }

    public static ManagerEntity convertFromBaseToEntity(Manager manager) {
        return ManagerEntity.builder()
                .baseUser(BaseUserConverter.convertFromBaseToEntity(manager.getBaseUser()))
                .position(manager.getPosition().toString())
                .build();
    }
}
