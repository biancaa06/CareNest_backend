package nl.fontys.s3.carenestproject.service.impl;

import nl.fontys.s3.carenestproject.configuration.security.token.AccessTokenEncoder;
import nl.fontys.s3.carenestproject.configuration.security.token.impl.AccessTokenImpl;
import nl.fontys.s3.carenestproject.domain.classes.Address;
import nl.fontys.s3.carenestproject.domain.classes.Gender;
import nl.fontys.s3.carenestproject.domain.classes.Role;
import nl.fontys.s3.carenestproject.domain.classes.users.User;
import nl.fontys.s3.carenestproject.persistance.entity.*;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.ResetPasswordCodeRepo;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.UserRepo;
import nl.fontys.s3.carenestproject.service.AddressService;
import nl.fontys.s3.carenestproject.service.MailService;
import nl.fontys.s3.carenestproject.service.exception.*;
import nl.fontys.s3.carenestproject.service.request.AuthRequest;
import nl.fontys.s3.carenestproject.service.request.CreateBaseAccountRequest;
import nl.fontys.s3.carenestproject.service.request.ResetPasswordRequest;
import nl.fontys.s3.carenestproject.service.request.UpdateUserAddressRequest;
import nl.fontys.s3.carenestproject.service.response.AuthResponse;
import nl.fontys.s3.carenestproject.service.response.CreateBaseAccountResponse;
import nl.fontys.s3.carenestproject.service.response.StatisticsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessagingException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private ResetPasswordCodeRepo resetPasswordCodeRepo;

    @Mock
    private AddressService addressService;

    @Mock
    private MailService mailService;

    @Mock
    private AccessTokenEncoder accessTokenEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test: createUser
    @Test
    void createUser_ShouldCreateNewUser_WhenEmailDoesNotExist() {
        // Arrange
        CreateBaseAccountRequest request = CreateBaseAccountRequest.builder()
                .email("test@example.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("123456789")
                .gender("MALE")
                .build();

        when(userRepo.findUserEntityByEmail(request.getEmail())).thenReturn(null);
        when(userRepo.save(any(UserEntity.class))).thenReturn(UserEntity.builder().id(1L).build());

        // Act
        CreateBaseAccountResponse response = userService.createUser(request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(userRepo, times(1)).save(any(UserEntity.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailAlreadyExists() {
        // Arrange
        CreateBaseAccountRequest request = CreateBaseAccountRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        when(userRepo.findUserEntityByEmail(request.getEmail())).thenReturn(UserEntity.builder().active(true).build());

        // Act & Assert
        assertThrows(EmailExistsException.class, () -> userService.createUser(request));
        verify(userRepo, never()).save(any());
    }
    @Test
    void createUser_ShouldReturnResponse_WhenEmailExistsButUserIsInactive() {
        // Arrange
        CreateBaseAccountRequest request = CreateBaseAccountRequest.builder()
                .email("inactive@example.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .build();

        UserEntity existingUser = UserEntity.builder()
                .id(1L)
                .email("inactive@example.com")
                .active(false) // Inactive user
                .build();

        when(userRepo.findUserEntityByEmail(request.getEmail())).thenReturn(existingUser);

        // Act
        CreateBaseAccountResponse response = userService.createUser(request);

        // Assert
        assertNotNull(response);
        assertEquals(existingUser.getId(), response.getId());
        verify(userRepo, times(1)).findUserEntityByEmail(request.getEmail());
        verify(userRepo, never()).save(any(UserEntity.class));
    }


    // Test: getUserById
    @Test
    void getUserById_ShouldReturnUser_WhenIdIsValid() {
        // Arrange
        long userId = 1L;
        UserEntity userEntity = mockUserEntity(userId);

        when(userRepo.findUserEntityById(userId)).thenReturn(userEntity);

        // Act
        User result = userService.getUserById(userId, userId);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(userRepo, times(1)).findUserEntityById(userId);
    }
    @Test
    void getUserById_ShouldThrowException_WhenIdIsInvalid() {
        // Arrange
        long invalidId = 0L;
        long authenticatedUserId = 0L;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.getUserById(invalidId, authenticatedUserId));
        assertEquals("Invalid id", exception.getMessage());
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotActive() {
        // Arrange
        long userId = 1L;

        when(userRepo.findUserEntityById(userId)).thenReturn(null);

        // Act & Assert
        assertThrows(UserNotActiveException.class, () -> userService.getUserById(userId, userId));
    }

    @Test
    void getUserById_ShouldThrowException_WhenUnauthorized() {
        // Arrange
        long userId = 1L;
        long authenticatedUserId = 2L;

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> userService.getUserById(userId, authenticatedUserId));
    }
    @Test
    void getUserById_ShouldSetProfileImage_WhenProfileImageIsNotNull() {
        // Arrange
        long userId = 1L;
        byte[] profileImage = "Test Image".getBytes();

        UserEntity userEntity = mockUserEntity(userId);
        userEntity.setProfileImage(profileImage);

        when(userRepo.findUserEntityById(userId)).thenReturn(userEntity);

        // Act
        User result = userService.getUserById(userId, userId);

        // Assert
        assertNotNull(result.getProfileImage());
        assertEquals(Base64.getEncoder().encodeToString(profileImage), new String(result.getProfileImage()));
        verify(userRepo, times(1)).findUserEntityById(userId);
    }


    // Test: updateUserAddress
    @Test
    void updateUserAddress_ShouldUpdateAddress_WhenValidRequest() {
        // Arrange
        long userId = 1L;
        UpdateUserAddressRequest request = UpdateUserAddressRequest.builder()
                .country("Netherlands")
                .city("Eindhoven")
                .street("Fontys")
                .number(10)
                .build();

        UserEntity userEntity = mockUserEntity(userId);
        when(userRepo.findUserEntityById(userId)).thenReturn(userEntity);
        when(addressService.createAddress(any(Address.class))).thenReturn(Address.builder().build());

        // Act
        userService.updateUserAddress(request, userId, userId);

        // Assert
        verify(userRepo, times(1)).save(userEntity);
    }

    @Test
    void updateUserAddress_ShouldThrowException_WhenUnauthorized() {
        // Arrange
        long userId = 1L;
        long authenticatedUserId = 2L;
        UpdateUserAddressRequest request = UpdateUserAddressRequest.builder().build();

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> userService.updateUserAddress(request, userId, authenticatedUserId));
    }
    @Test
    void updateUserAddress_ShouldThrowUserNotActiveException_WhenUserNotActive() {
        // Arrange
        long userId = 1L;
        UpdateUserAddressRequest request = UpdateUserAddressRequest.builder()
                .country("Netherlands")
                .city("Eindhoven")
                .street("Fontys")
                .number(10)
                .build();

        // Mock a user that is not active
        UserEntity inactiveUser = UserEntity.builder()
                .id(userId)
                .active(false)
                .build();

        when(userRepo.findUserEntityById(userId)).thenReturn(inactiveUser);

        // Act & Assert
        assertThrows(UserNotActiveException.class, () -> userService.updateUserAddress(request, userId, userId));

        // Verify the interaction
        verify(userRepo, times(1)).findUserEntityById(userId);
        verify(userRepo, never()).save(any(UserEntity.class));
    }


    // Test: login
    @Test
    void login_ShouldReturnAuthResponse_WhenCredentialsAreValid() {
        // Arrange
        AuthRequest request = AuthRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        UserEntity userEntity = mockUserEntity(1L);
        when(userRepo.findUserEntityByEmail(request.getEmail())).thenReturn(userEntity);
        when(accessTokenEncoder.encode(any(AccessTokenImpl.class))).thenReturn("valid_token");

        // Act
        AuthResponse response = userService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("valid_token", response.getAccessToken());
    }

    @Test
    void login_ShouldThrowException_WhenEmailIsInvalid() {
        // Arrange
        AuthRequest request = AuthRequest.builder()
                .email("invalid@example.com")
                .password("password")
                .build();

        when(userRepo.findUserEntityByEmail(request.getEmail())).thenReturn(null);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> userService.login(request));
    }

    @Test
    void login_ShouldThrowException_WhenPasswordIsInvalid() {
        // Arrange
        AuthRequest request = AuthRequest.builder()
                .email("test@example.com")
                .password("wrongpassword")
                .build();

        UserEntity userEntity = mockUserEntity(1L);
        when(userRepo.findUserEntityByEmail(request.getEmail())).thenReturn(userEntity);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> userService.login(request));
    }

    //update picture
    @Test
    void updateProfilePicture_ShouldThrowException_WhenUnauthorized() {
        // Arrange
        long userId = 1L;
        long authenticatedUserId = 2L;
        MultipartFile file = mock(MultipartFile.class);

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> userService.updateProfilePicture(file, userId, authenticatedUserId));
        verify(userRepo, never()).save(any(UserEntity.class));
    }

    @Test
    void updateProfilePicture_ShouldThrowException_WhenUserNotActive() {
        // Arrange
        long userId = 1L;
        long authenticatedUserId = 1L;
        MultipartFile file = mock(MultipartFile.class);

        when(userRepo.findUserEntityById(userId)).thenReturn(null);

        // Act & Assert
        assertThrows(UserNotActiveException.class, () -> userService.updateProfilePicture(file, userId, authenticatedUserId));
        verify(userRepo, never()).save(any(UserEntity.class));
    }

    @Test
    void updateProfilePicture_ShouldThrowException_WhenFileTypeIsInvalid() {
        // Arrange
        long userId = 1L;
        long authenticatedUserId = 1L;
        MultipartFile file = mock(MultipartFile.class);

        when(userRepo.findUserEntityById(userId)).thenReturn(mockUserEntity(userId));
        when(file.getContentType()).thenReturn(MediaType.APPLICATION_PDF_VALUE);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.updateProfilePicture(file, userId, authenticatedUserId));
        verify(userRepo, never()).save(any(UserEntity.class));
    }

    @Test
    void updateProfilePicture_ShouldSaveProfilePicture_WhenFileIsValid() throws IOException {
        // Arrange
        long userId = 1L;
        long authenticatedUserId = 1L;
        MultipartFile file = mock(MultipartFile.class);

        UserEntity userEntity = mockUserEntity(userId);
        when(userRepo.findUserEntityById(userId)).thenReturn(userEntity);
        when(file.getContentType()).thenReturn(MediaType.IMAGE_JPEG_VALUE);
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3, 4});

        // Act
        userService.updateProfilePicture(file, userId, authenticatedUserId);

        // Assert
        verify(userRepo, times(1)).save(userEntity);
        assertArrayEquals(new byte[]{1, 2, 3, 4}, userEntity.getProfileImage());
    }

    @Test
    void updateProfilePicture_ShouldResetProfilePicture_WhenFileIsNull() throws IOException {
        // Arrange
        long userId = 1L;
        long authenticatedUserId = 1L;

        UserEntity userEntity = mockUserEntity(userId);
        when(userRepo.findUserEntityById(userId)).thenReturn(userEntity);

        // Act
        userService.updateProfilePicture(null, userId, authenticatedUserId);

        // Assert
        verify(userRepo, times(1)).save(userEntity);
        assertNull(userEntity.getProfileImage());
    }

    @Test
    void testSendForgotPassword() throws Exception {
        // Arrange
        String email = "email";
        UserEntity userEntity = mockUserEntity(1L);
        when(userRepo.existsByEmail(email)).thenReturn(true);
        when(userRepo.findUserEntityByEmail(email)).thenReturn(userEntity);
        when(resetPasswordCodeRepo.existsByUser(userEntity)).thenReturn(false);

        // Act
        userService.sendForgotPassword(email);

        // Assert
        verify(resetPasswordCodeRepo).save(argThat(resetPasswordCode -> {
            assertNotNull(resetPasswordCode);
            assertEquals(userEntity, resetPasswordCode.getUser());
            assertTrue(resetPasswordCode.getResetCode() > 0); // Ensure a valid reset code is generated
            assertNotNull(resetPasswordCode.getExpirationTime());
            return true;
        }));
        verify(mailService).sendHtmlEmail(eq(email), anyString(), anyString());
    }


    @Test
    void testSendForgotPassword_UserRepoExistsByEmailReturnsFalse() {
        // Setup
        when(userRepo.existsByEmail("email")).thenReturn(false);

        // Run the test
        assertThatThrownBy(() -> userService.sendForgotPassword("email"))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testSendForgotPassword_ResetPasswordCodeRepoExistsByUserReturnsTrue() throws Exception {
        // Arrange
        String email = "email";
        UserEntity userEntity = UserEntity.builder()
                .id(0L)
                .firstName("firstName")
                .lastName("lastName")
                .email(email)
                .roleId(RoleEntity.builder().roleName("roleName").build())
                .password("encodedPassword")
                .phoneNumber("phoneNumber")
                .address(AddressEntity.builder().id(0L).country("country").city("city").street("street").number(0).build())
                .gender(GenderEntity.builder().id(0L).genderName("gender").build())
                .profileImage("content".getBytes())
                .active(false)
                .build();

        ResetPasswordCode existingCode = ResetPasswordCode.builder()
                .resetCode(123456)
                .expirationTime(new GregorianCalendar(2025, Calendar.JANUARY, 7, 9, 50).getTime()) // Existing code
                .user(userEntity)
                .build();

        when(userRepo.existsByEmail(email)).thenReturn(true);
        when(userRepo.findUserEntityByEmail(email)).thenReturn(userEntity);
        when(resetPasswordCodeRepo.existsByUser(userEntity)).thenReturn(true);
        when(resetPasswordCodeRepo.findByUser(userEntity)).thenReturn(Optional.of(existingCode));

        // Run the test
        userService.sendForgotPassword(email);

        // Assert
        verify(resetPasswordCodeRepo, times(1)).save(any(ResetPasswordCode.class)); // Ensure a new code is saved
        verify(mailService, times(1)).sendHtmlEmail(eq(email), anyString(), anyString());
    }



    @Test
    void testSendForgotPassword_ResetPasswordCodeRepoFindByUserReturnsAbsent() {
        // Setup
        when(userRepo.existsByEmail("email")).thenReturn(true);

        // Configure UserRepo.findUserEntityByEmail(...).
        final UserEntity userEntity = UserEntity.builder()
                .id(0L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email")
                .roleId(RoleEntity.builder()
                        .roleName("roleName")
                        .build())
                .password("encodedPassword")
                .phoneNumber("phoneNumber")
                .address(AddressEntity.builder()
                        .id(0L)
                        .country("country")
                        .city("city")
                        .street("street")
                        .number(0)
                        .build())
                .gender(GenderEntity.builder()
                        .id(0L)
                        .genderName("gender")
                        .build())
                .profileImage("content".getBytes())
                .active(false)
                .build();
        when(userRepo.findUserEntityByEmail("email")).thenReturn(userEntity);

        when(resetPasswordCodeRepo.existsByUser(UserEntity.builder()
                .id(0L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email")
                .roleId(RoleEntity.builder()
                        .roleName("roleName")
                        .build())
                .password("encodedPassword")
                .phoneNumber("phoneNumber")
                .address(AddressEntity.builder()
                        .id(0L)
                        .country("country")
                        .city("city")
                        .street("street")
                        .number(0)
                        .build())
                .gender(GenderEntity.builder()
                        .id(0L)
                        .genderName("gender")
                        .build())
                .profileImage("content".getBytes())
                .active(false)
                .build())).thenReturn(true);
        when(resetPasswordCodeRepo.findByUser(UserEntity.builder()
                .id(0L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email")
                .roleId(RoleEntity.builder()
                        .roleName("roleName")
                        .build())
                .password("encodedPassword")
                .phoneNumber("phoneNumber")
                .address(AddressEntity.builder()
                        .id(0L)
                        .country("country")
                        .city("city")
                        .street("street")
                        .number(0)
                        .build())
                .gender(GenderEntity.builder()
                        .id(0L)
                        .genderName("gender")
                        .build())
                .profileImage("content".getBytes())
                .active(false)
                .build())).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> userService.sendForgotPassword("email"))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testSendForgotPassword_MailServiceThrowsMessagingException() throws Exception {
        // Arrange
        String email = "email";
        UserEntity userEntity = UserEntity.builder()
                .id(0L)
                .firstName("firstName")
                .lastName("lastName")
                .email(email)
                .roleId(RoleEntity.builder().roleName("roleName").build())
                .password("encodedPassword")
                .phoneNumber("phoneNumber")
                .address(AddressEntity.builder().id(0L).country("country").city("city").street("street").number(0).build())
                .gender(GenderEntity.builder().id(0L).genderName("gender").build())
                .profileImage("content".getBytes())
                .active(false)
                .build();

        when(userRepo.existsByEmail(email)).thenReturn(true);
        when(userRepo.findUserEntityByEmail(email)).thenReturn(userEntity);
        when(resetPasswordCodeRepo.existsByUser(userEntity)).thenReturn(false);

        doThrow(MessagingException.class).when(mailService).sendHtmlEmail(
                eq(email), eq("CareNest - Temporary Password for Your Account"), anyString());

        // Act & Assert
        assertThatThrownBy(() -> userService.sendForgotPassword(email))
                .isInstanceOf(RuntimeException.class);

        verify(resetPasswordCodeRepo, times(1)).save(any(ResetPasswordCode.class));
    }

    @Test
    void testResetPassword() {
        // Setup
        final ResetPasswordRequest request = ResetPasswordRequest.builder()
                .resetCode(0)
                .newPassword("newPassword")
                .build();

        final UserEntity userEntity = UserEntity.builder()
                .id(0L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email")
                .roleId(RoleEntity.builder()
                        .roleName("roleName")
                        .build())
                .password("encodedPassword")
                .phoneNumber("phoneNumber")
                .address(AddressEntity.builder()
                        .id(0L)
                        .country("country")
                        .city("city")
                        .street("street")
                        .number(0)
                        .build())
                .gender(GenderEntity.builder()
                        .id(0L)
                        .genderName("gender")
                        .build())
                .profileImage("content".getBytes())
                .active(false)
                .build();

        final ResetPasswordCode resetPasswordCode = ResetPasswordCode.builder()
                .resetCode(0)
                .expirationTime(Date.from(Instant.now().plusSeconds(300))) // 5 minutes from now
                .user(userEntity)
                .build();

        when(userRepo.findUserEntityByEmail("email")).thenReturn(userEntity);
        when(resetPasswordCodeRepo.findByUser(userEntity)).thenReturn(Optional.of(resetPasswordCode));

        // Act
        userService.resetPassword("email", request);

        //Assert
        verify(userRepo, times(1)).save(argThat(user ->
                user.getPassword() != null && !user.getPassword().equals("encodedPassword")
        ));
    }


    @Test
    void testResetPassword_UserRepoFindUserEntityByEmailReturnsNull() {
        // Setup
        final ResetPasswordRequest request = ResetPasswordRequest.builder()
                .resetCode(0)
                .newPassword("newPassword")
                .build();
        when(userRepo.findUserEntityByEmail("email")).thenReturn(null);

        // Run the test
        assertThatThrownBy(() -> userService.resetPassword("email", request))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testResetPassword_ResetPasswordCodeRepoReturnsAbsent() {
        // Setup
        final ResetPasswordRequest request = ResetPasswordRequest.builder()
                .resetCode(0)
                .newPassword("newPassword")
                .build();

        // Configure UserRepo.findUserEntityByEmail(...).
        final UserEntity userEntity = UserEntity.builder()
                .id(0L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email")
                .roleId(RoleEntity.builder()
                        .roleName("roleName")
                        .build())
                .password("encodedPassword")
                .phoneNumber("phoneNumber")
                .address(AddressEntity.builder()
                        .id(0L)
                        .country("country")
                        .city("city")
                        .street("street")
                        .number(0)
                        .build())
                .gender(GenderEntity.builder()
                        .id(0L)
                        .genderName("gender")
                        .build())
                .profileImage("content".getBytes())
                .active(false)
                .build();
        when(userRepo.findUserEntityByEmail("email")).thenReturn(userEntity);

        when(resetPasswordCodeRepo.findByUser(UserEntity.builder()
                .id(0L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email")
                .roleId(RoleEntity.builder()
                        .roleName("roleName")
                        .build())
                .password("encodedPassword")
                .phoneNumber("phoneNumber")
                .address(AddressEntity.builder()
                        .id(0L)
                        .country("country")
                        .city("city")
                        .street("street")
                        .number(0)
                        .build())
                .gender(GenderEntity.builder()
                        .id(0L)
                        .genderName("gender")
                        .build())
                .profileImage("content".getBytes())
                .active(false)
                .build())).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> userService.resetPassword("email", request))
                .isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testResetPassword_InvalidResetCode() {
        // Setup
        final ResetPasswordRequest request = ResetPasswordRequest.builder()
                .resetCode(1) // Provided code does not match stored code
                .newPassword("newPassword")
                .build();

        final UserEntity userEntity = UserEntity.builder()
                .id(0L)
                .email("email")
                .build();

        final ResetPasswordCode resetPasswordCode = ResetPasswordCode.builder()
                .resetCode(0) // Stored code
                .expirationTime(Date.from(Instant.now().plusSeconds(300))) // Not expired
                .user(userEntity)
                .build();

        when(userRepo.findUserEntityByEmail("email")).thenReturn(userEntity);
        when(resetPasswordCodeRepo.findByUser(userEntity)).thenReturn(Optional.of(resetPasswordCode));

        // Run the test
        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                userService.resetPassword("email", request)
        );

        // Assert
        assertTrue(exception.getMessage().contains("Please provide a valid reset code."),
                "Expected exception message to contain 'Please provide a valid reset code.'");
        verify(userRepo, never()).save(any());
        verify(resetPasswordCodeRepo, never()).delete(any());
    }

    @Test
    void testResetPassword_ExpiredResetCode() {
        // Setup
        final ResetPasswordRequest request = ResetPasswordRequest.builder()
                .resetCode(0) // Correct code
                .newPassword("newPassword")
                .build();

        final UserEntity userEntity = UserEntity.builder()
                .id(0L)
                .email("email")
                .build();

        final ResetPasswordCode resetPasswordCode = ResetPasswordCode.builder()
                .resetCode(0) // Stored code matches
                .expirationTime(Date.from(Instant.now().minusSeconds(10))) // Expired
                .user(userEntity)
                .build();

        when(userRepo.findUserEntityByEmail("email")).thenReturn(userEntity);
        when(resetPasswordCodeRepo.findByUser(userEntity)).thenReturn(Optional.of(resetPasswordCode));

        // Run the test and Assert
        TokenExpiredException exception = assertThrows(TokenExpiredException.class, () ->
                userService.resetPassword("email", request)
        );

        assertTrue(exception.getMessage().contains("The code provided is expired"),
                "Expected exception message to contain 'The code provided is expired'");
        verify(userRepo, never()).save(any());
        verify(resetPasswordCodeRepo, never()).delete(any());
    }


    // Utility methods for mocking
    private UserEntity mockUserEntity(long userId) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("password");

        return UserEntity.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password(encodedPassword)
                .gender(GenderEntity.builder().id(Gender.MALE.getValue()).genderName("MALE").build())
                .roleId(RoleEntity.builder().id(Role.PATIENT.getValue()).roleName(Role.PATIENT.name()).build())
                .active(true)
                .build();
    }

    @Test
    void getCaretakerToPatientStats_ShouldReturnValidStatisticsResponse() {
        // Arrange
        List<Object[]> mockResults = new ArrayList<>();
        mockResults.add(new Object[]{50L, 200L, 4.0});
        when(userRepo.getCaretakerToPatientStats()).thenReturn(mockResults);

        // Act
        StatisticsResponse response = userService.getCaretakerToPatientStats();

        // Assert
        assertNotNull(response);
        assertEquals(50L, response.getTotalCaretakers());
        assertEquals(200L, response.getTotalPatients());
        assertEquals(4.0, response.getCaretakerToPatientRatio());

        verify(userRepo, times(1)).getCaretakerToPatientStats();
    }

    @Test
    void getCaretakerToPatientStats_ShouldReturnNullWhenNoResults() {
        // Arrange
        when(userRepo.getCaretakerToPatientStats()).thenReturn(new ArrayList<>());

        // Act
        StatisticsResponse response = userService.getCaretakerToPatientStats();

        // Assert
        assertNull(response);
        verify(userRepo, times(1)).getCaretakerToPatientStats();
    }

    @Test
    void getCaretakerToPatientStats_ShouldHandleMultipleRowsButReturnLastRow() {
        // Arrange
        List<Object[]> mockResults = new ArrayList<>();
        mockResults.add(new Object[]{30L, 100L, 3.33});
        mockResults.add(new Object[]{50L, 200L, 4.0});
        when(userRepo.getCaretakerToPatientStats()).thenReturn(mockResults);

        // Act
        StatisticsResponse response = userService.getCaretakerToPatientStats();

        // Assert
        assertNotNull(response);
        assertEquals(50L, response.getTotalCaretakers());
        assertEquals(200L, response.getTotalPatients());
        assertEquals(4.0, response.getCaretakerToPatientRatio());

        verify(userRepo, times(1)).getCaretakerToPatientStats();
    }

}
