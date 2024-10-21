package nl.fontys.s3.carenestproject.service.impl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.domain.classes.Gender;
import nl.fontys.s3.carenestproject.domain.classes.users.User;
import nl.fontys.s3.carenestproject.persistance.entity.GenderEntity;
import nl.fontys.s3.carenestproject.persistance.entity.UserEntity;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.UserRepo;
import nl.fontys.s3.carenestproject.service.UserService;
import nl.fontys.s3.carenestproject.service.exception.EmailExistsException;
import nl.fontys.s3.carenestproject.service.mapping.BaseUserConverter;
import nl.fontys.s3.carenestproject.service.request.CreateBaseAccountRequest;
import nl.fontys.s3.carenestproject.service.response.CreateBaseAccountResponse;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;

    @Override
    public CreateBaseAccountResponse createUser(CreateBaseAccountRequest request) {
        if(userExistsByEmail(request.getEmail())) {
            throw new EmailExistsException();
        }

        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .gender(GenderEntity.builder()
                        .id(Gender.valueOf(request.getGender()).getValue())
                        .genderName(request.getGender())
                        .build())
                .password(request.getPassword())
                .build();

        UserEntity userEntity = userRepo.save(user);
        return CreateBaseAccountResponse.builder()
                .id(userEntity.getId())
                .build();
    }

    @Override
    public User getUserById(long id){
        UserEntity userEntity = userRepo.findUserEntityById(id);
        if(userEntity == null){
            throw new IllegalArgumentException("User not found");
        }
        return BaseUserConverter.convertFromEntityToBase(userEntity);
    }

    private boolean userExistsByEmail(String email){
        return userRepo.findUserEntityByEmail(email) != null;
    }
}
