package nl.fontys.s3.carenestproject.service.impl;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.configuration.security.token.AccessTokenEncoder;
import nl.fontys.s3.carenestproject.configuration.security.token.impl.AccessTokenImpl;
import nl.fontys.s3.carenestproject.domain.classes.Address;
import nl.fontys.s3.carenestproject.domain.classes.Gender;
import nl.fontys.s3.carenestproject.domain.classes.users.User;
import nl.fontys.s3.carenestproject.persistance.entity.ResetPasswordCode;
import nl.fontys.s3.carenestproject.persistance.entity.GenderEntity;
import nl.fontys.s3.carenestproject.persistance.entity.UserEntity;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.ResetPasswordCodeRepo;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.UserRepo;
import nl.fontys.s3.carenestproject.service.AddressService;
import nl.fontys.s3.carenestproject.service.MailService;
import nl.fontys.s3.carenestproject.service.UserService;
import nl.fontys.s3.carenestproject.service.exception.*;
import nl.fontys.s3.carenestproject.service.mapping.AddressConverter;
import nl.fontys.s3.carenestproject.service.mapping.BaseUserConverter;
import nl.fontys.s3.carenestproject.service.request.AuthRequest;
import nl.fontys.s3.carenestproject.service.request.CreateBaseAccountRequest;
import nl.fontys.s3.carenestproject.service.request.ResetPasswordRequest;
import nl.fontys.s3.carenestproject.service.request.UpdateUserAddressRequest;
import nl.fontys.s3.carenestproject.service.response.AuthResponse;
import nl.fontys.s3.carenestproject.service.response.CreateBaseAccountResponse;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final AddressService addressService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final AccessTokenEncoder accessTokenEncoder;
    private final ResetPasswordCodeRepo resetPasswordRepo;
    private final MailService mailService;
    private final ResetPasswordCodeRepo resetPasswordCodeRepo;


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
    public User getUserById(long id, long authenticatedUserId) {

        if(id != authenticatedUserId) {
            throw new UnauthorizedException("You are not authorized for this action");
        }

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
    public void updateUserAddress(UpdateUserAddressRequest request, long userId, long authenticatedUserId) {
        if(userId != authenticatedUserId) {
            throw new UnauthorizedException("You cannot update the address of another user. Please log in with your own account");
        }

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
    public void updateProfilePicture(MultipartFile file, long userId, long authenticatedUserId) throws IOException {

        if(userId != authenticatedUserId) {
            throw new UnauthorizedException("You cannot update the address of another user. Please log in with your own account");
        }

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

    @Override
    @Transactional
    public void sendForgotPassword(String email) {
        if(!userRepo.existsByEmail(email)) {
            throw new ObjectNotFoundException("User not found");
        }

        UserEntity user = userRepo.findUserEntityByEmail(email);

        Integer otp = generateResetCode(user);
        String body = """
            <!DOCTYPE html>
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <p>Dear <strong>%s</strong>,</p>
        
                <p>We received a request to reset the password for your CareNest account. A reset code has been generated for you:</p>
        
                <p><strong>Reset Code:</strong> <span style="font-size: 16px; color: #2e6b34;">%s</span></p>
        
                <p>Please use this code to log in to your account at: <a href="[CareNest Login URL]" target="_blank">CareNest Login</a></p>
        
                <p><strong>Note:</strong> This password is valid for <strong>5 minutes</strong> only. If you do not log in within this time, you will need to request a new password reset.</p>
        
                <p>If you did not request to reset your password, please ignore this email. Your password will remain unchanged.</p>
        
                <p>For any assistance, feel free to contact our support team.</p>
        
                <p>Best regards,</p>
                <p>The CareNest Team</p>
            </body>
            </html>
            """.formatted(user.getFirstName(), otp);

        try{
            mailService.sendHtmlEmail(email, "CareNest - Temporary Password for Your Account", body);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resetPassword(String email, ResetPasswordRequest request) {
        UserEntity user = userRepo.findUserEntityByEmail(email);
        if(user == null) {
            throw new ObjectNotFoundException("User not found");
        }
        ResetPasswordCode code = resetPasswordCodeRepo.findByUser(user).orElseThrow(() -> new ObjectNotFoundException("A reset code was not requested for this user"));
        if(!Objects.equals(code.getResetCode(), request.getResetCode())) {
            throw new InvalidInputException("Please provide a valid reset code.");
        }
        if(code.getExpirationTime().before(Date.from(Instant.now()))) {
            throw new TokenExpiredException("The code provided is expired");
        }

        user.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
        userRepo.save(user);
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

    private Integer generateResetCode(UserEntity user){
        Random random = new Random();

        Integer code = random.nextInt(100000, 999999);
        Instant now = Instant.now();
        Date expirationTime = Date.from(now.plus(5, ChronoUnit.MINUTES));
        new ResetPasswordCode();
        ResetPasswordCode resetPasswordCode;
        if(resetPasswordRepo.existsByUser(user)){
            resetPasswordCode = resetPasswordRepo.findByUser(user).orElseThrow(() -> new ObjectNotFoundException("No reset code found"));
            resetPasswordCode.setResetCode(code);
            resetPasswordCode.setExpirationTime(expirationTime);
        }
        else{
            resetPasswordCode = ResetPasswordCode.builder()
                    .resetCode(code)
                    .expirationTime(expirationTime)
                    .user(user).build();
        }
        resetPasswordRepo.save(resetPasswordCode);
        return code;
    }

}
