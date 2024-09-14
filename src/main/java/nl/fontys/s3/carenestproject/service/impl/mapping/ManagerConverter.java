package nl.fontys.s3.carenestproject.service.impl.mapping;

import nl.fontys.s3.carenestproject.domain.classes.Announcement;
import nl.fontys.s3.carenestproject.domain.classes.Gender;
import nl.fontys.s3.carenestproject.domain.classes.Position;
import nl.fontys.s3.carenestproject.domain.classes.users.Manager;
import nl.fontys.s3.carenestproject.persistance.entity.AnnouncementEntity;
import nl.fontys.s3.carenestproject.persistance.entity.ManagerEntity;

public class ManagerConverter {
    private ManagerConverter() {}

    public static Manager convertFromEntityToBase(ManagerEntity managerEntity) {
        return Manager.builder()
                .id(managerEntity.getId())
                .firstName(managerEntity.getFirstName())
                .lastName(managerEntity.getLastName())
                .email(managerEntity.getEmail())
                .phoneNumber(managerEntity.getPhoneNumber())
                .gender(Gender.valueOf(managerEntity.getGender()))
                .address(AddressConverter.convertFromEntityToBase(managerEntity.getAddress()))
                .position(Position.valueOf(managerEntity.getPosition()))
                .build();

    }

    public static ManagerEntity convertFromBaseToEntity(Manager manager) {
        return ManagerEntity.builder()
                .id(manager.getId())
                .firstName(manager.getFirstName())
                .lastName(manager.getLastName())
                .email(manager.getEmail())
                .phoneNumber(manager.getPhoneNumber())
                .gender(manager.getGender().toString())
                .address(AddressConverter.convertFromBaseToEntity(manager.getAddress()))
                .position(manager.getPosition().toString())
                .build();
    }
}
