package nl.fontys.s3.carenestproject.service.mapping;

import nl.fontys.s3.carenestproject.domain.classes.Address;
import nl.fontys.s3.carenestproject.domain.classes.Gender;
import nl.fontys.s3.carenestproject.domain.classes.Role;
import nl.fontys.s3.carenestproject.domain.classes.users.User;
import nl.fontys.s3.carenestproject.persistance.entity.AddressEntity;
import nl.fontys.s3.carenestproject.persistance.entity.GenderEntity;
import nl.fontys.s3.carenestproject.persistance.entity.RoleEntity;
import nl.fontys.s3.carenestproject.persistance.entity.UserEntity;

public final class BaseUserConverter {
    private BaseUserConverter() {}

    public static User convertFromEntityToBase(UserEntity userEntity) {
        Address address = new Address();
        if(userEntity.getAddress() != null) {
            address = AddressConverter.convertFromEntityToBase(userEntity.getAddress());
        }
        else{
            address = null;
        }
        return User.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .gender(Gender.valueOf(userEntity.getGender().getGenderName()))
                .role(Role.valueOf(userEntity.getRoleId().getRoleName()))
                .phoneNumber(userEntity.getPhoneNumber())
                .address(address)
                .active(userEntity.isActive())
                .build();

    }
    public static UserEntity convertFromBaseToEntity(User user) {
        Gender gender = user.getGender();
        AddressEntity address = new AddressEntity();
        if (user.getAddress() != null) {
            address = AddressConverter.convertFromBaseToEntity(user.getAddress());
        }
        else{
            address = null;
        }
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
                .address(address)
                .active(user.isActive())
                .build();
    }
}
