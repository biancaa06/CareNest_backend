package nl.fontys.s3.carenestproject.service.mapping;

import nl.fontys.s3.carenestproject.domain.classes.Gender;
import nl.fontys.s3.carenestproject.domain.classes.Role;
import nl.fontys.s3.carenestproject.domain.classes.users.User;
import nl.fontys.s3.carenestproject.persistance.entity.GenderEntity;
import nl.fontys.s3.carenestproject.persistance.entity.RoleEntity;
import nl.fontys.s3.carenestproject.persistance.entity.UserEntity;

public final class BaseUserConverter {
    private BaseUserConverter() {}

    public static User convertFromEntityToBase(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .gender(Gender.valueOf(userEntity.getGender().getGenderName()))
                .role(Role.valueOf(userEntity.getRoleId().getRoleName()))
                .phoneNumber(userEntity.getPhoneNumber())
                .address(AddressConverter.convertFromEntityToBase(userEntity.getAddress()))
                .build();

    }
    public static UserEntity convertFromBaseToEntity(User user) {
        Gender gender = user.getGender();
        return UserEntity.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roleId(RoleEntity.builder()
                        .id(user.getRole().getValue())
                        .roleName(user.getRole().toString())
                        .build())
                .phoneNumber(user.getPhoneNumber())
                .gender(GenderEntity.builder()
                        .id(user.getGender().getValue())
                        .genderName(gender.toString())
                        .build())
                .address(AddressConverter.convertFromBaseToEntity(user.getAddress()))
                .build();
    }
}
