package nl.fontys.s3.carenestproject.service.impl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.configuration.security.token.AccessTokenEncoder;
import nl.fontys.s3.carenestproject.configuration.security.token.impl.AccessTokenImpl;
import nl.fontys.s3.carenestproject.domain.classes.Address;
import nl.fontys.s3.carenestproject.domain.classes.Gender;
import nl.fontys.s3.carenestproject.domain.classes.users.User;
import nl.fontys.s3.carenestproject.persistance.entity.GenderEntity;
import nl.fontys.s3.carenestproject.persistance.entity.UserEntity;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.UserRepo;
import nl.fontys.s3.carenestproject.service.AddressService;
import nl.fontys.s3.carenestproject.service.UserService;
import nl.fontys.s3.carenestproject.service.exception.EmailExistsException;
import nl.fontys.s3.carenestproject.service.exception.InvalidCredentialsException;
import nl.fontys.s3.carenestproject.service.exception.UserNotActiveException;
import nl.fontys.s3.carenestproject.service.mapping.AddressConverter;
import nl.fontys.s3.carenestproject.service.mapping.BaseUserConverter;
import nl.fontys.s3.carenestproject.service.request.AuthRequest;
import nl.fontys.s3.carenestproject.service.request.CreateBaseAccountRequest;
import nl.fontys.s3.carenestproject.service.request.UpdateUserAddressRequest;
import nl.fontys.s3.carenestproject.service.response.AuthResponse;
import nl.fontys.s3.carenestproject.service.response.CreateBaseAccountResponse;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final AddressService addressService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final AccessTokenEncoder accessTokenEncoder;

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
    public User getUserById(long id) {
        if (id < 1) {
            throw new IllegalArgumentException("Invalid id");
        }

        UserEntity userEntity = userRepo.findUserEntityById(id);
        if (userEntity == null || !userEntity.isActive()) {
            throw new UserNotActiveException();
        }

        User user = BaseUserConverter.convertFromEntityToBase(userEntity);

        if (userEntity.getProfileImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(userEntity.getProfileImage());
            user.setProfileImage(base64Image.getBytes());
        }

        return user;
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

    @Override
    public void updateProfilePicture(MultipartFile file, long userId) throws IOException {
        UserEntity userEntity = userRepo.findUserEntityById(userId);
        if(userEntity==null || !userEntity.isActive()){
            throw new UserNotActiveException();
        }
        //if file not null upload new picture else jeep default avatar
        if(file != null){
            String fileType = file.getContentType();
            if (!MediaType.IMAGE_JPEG_VALUE.equals(fileType) && !MediaType.IMAGE_PNG_VALUE.equals(fileType)) {
                throw new IllegalArgumentException("Only PNG and JPG files are allowed.");
            }
            else{
                userEntity.setProfileImage(file.getBytes());
            }
        }
        else{
            userEntity.setProfileImage(null);
        }

        userRepo.save(userEntity);
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        UserEntity user = userRepo.findUserEntityByEmail(request.getEmail());
        if (user == null) {
            throw new InvalidCredentialsException();
        }

        if (!matchesPassword(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = generateAccessToken(user);
        return AuthResponse.builder().accessToken(accessToken).build();
    }

    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

    private String generateAccessToken(UserEntity user) {
        Long studentId = (user.getId() != null) ? user.getId() : null;

        List<String> roles = new ArrayList<>();
        roles.add(user.getRoleId().getRoleName());

        return accessTokenEncoder.encode(
                new AccessTokenImpl(user.getEmail(), studentId, roles));
    }

}
