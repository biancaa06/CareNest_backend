package nl.fontys.s3.carenestproject.service.mapping;

import nl.fontys.s3.carenestproject.domain.classes.Gender;
import nl.fontys.s3.carenestproject.domain.classes.users.User;
import nl.fontys.s3.carenestproject.persistance.entity.UserEntity;

public final class BaseUserConverter {
    private BaseUserConverter() {}

    public static User convertFromEntityToBase(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .phoneNumber(userEntity.getPhoneNumber())
                .gender(Gender.valueOf(userEntity.getGender()))
                .address(AddressConverter.convertFromEntityToBase(userEntity.getAddress()))
                .build();

    }
    public static UserEntity convertFromBaseToEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender().toString())
                .address(AddressConverter.convertFromBaseToEntity(user.getAddress()))
                .build();
    }
}
