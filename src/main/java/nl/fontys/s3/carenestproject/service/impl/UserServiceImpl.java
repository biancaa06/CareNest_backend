package nl.fontys.s3.carenestproject.service.impl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.domain.classes.Address;
import nl.fontys.s3.carenestproject.domain.classes.Gender;
import nl.fontys.s3.carenestproject.domain.classes.users.User;
import nl.fontys.s3.carenestproject.persistance.entity.GenderEntity;
import nl.fontys.s3.carenestproject.persistance.entity.UserEntity;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.UserRepo;
import nl.fontys.s3.carenestproject.service.AddressService;
import nl.fontys.s3.carenestproject.service.UserService;
import nl.fontys.s3.carenestproject.service.exception.EmailExistsException;
import nl.fontys.s3.carenestproject.service.exception.UserNotActiveException;
import nl.fontys.s3.carenestproject.service.mapping.AddressConverter;
import nl.fontys.s3.carenestproject.service.mapping.BaseUserConverter;
import nl.fontys.s3.carenestproject.service.request.CreateBaseAccountRequest;
import nl.fontys.s3.carenestproject.service.request.UpdateUserAddressRequest;
import nl.fontys.s3.carenestproject.service.response.CreateBaseAccountResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final AddressService addressService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public CreateBaseAccountResponse createUser(CreateBaseAccountRequest request) {
        UserEntity existingUser = userRepo.findUserEntityByEmail(request.getEmail());

        if (existingUser != null) {
            if (existingUser.isActive()) {
                throw new EmailExistsException();
            } else {
                return new CreateBaseAccountResponse(existingUser.getId());
            }
        }

        String hashedPass = bCryptPasswordEncoder.encode(request.getPassword());

        UserEntity newUser = UserEntity.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .gender(GenderEntity.builder()
                        .id(Gender.valueOf(request.getGender()).getValue())
                        .genderName(request.getGender())
                        .build())
                .password(hashedPass)
                .build();

        UserEntity userEntity = userRepo.save(newUser);
        return CreateBaseAccountResponse.builder()
                .id(userEntity.getId())
                .build();
    }

    @Override
    public User getUserById(long id){
        if(id<1){
            throw new IllegalArgumentException("Invalid id");
        }

        UserEntity userEntity = userRepo.findUserEntityById(id);

        if(userEntity==null || !userEntity.isActive()){
            throw new UserNotActiveException();
        }
        return BaseUserConverter.convertFromEntityToBase(userEntity);
    }

    @Override
    public void updateUserAddress(UpdateUserAddressRequest request, long userId) {
        UserEntity userEntity = userRepo.findUserEntityById(userId);
        if(userEntity==null || !userEntity.isActive()){
            throw new UserNotActiveException();
        }
        Address address = addressService.createAddress(Address.builder()
                .country(request.getCountry())
                .city(request.getCity())
                .street(request.getStreet())
                .number(request.getNumber()).build());
        userEntity.setAddress(AddressConverter.convertFromBaseToEntity(address));
        userRepo.save(userEntity);
    }

}
