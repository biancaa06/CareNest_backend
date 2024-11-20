package nl.fontys.s3.carenestproject.service.impl;

import nl.fontys.s3.carenestproject.configuration.security.token.AccessTokenEncoder;
import nl.fontys.s3.carenestproject.configuration.security.token.impl.AccessTokenImpl;
import nl.fontys.s3.carenestproject.domain.classes.Address;
import nl.fontys.s3.carenestproject.domain.classes.Gender;
import nl.fontys.s3.carenestproject.domain.classes.Role;
import nl.fontys.s3.carenestproject.domain.classes.users.User;
import nl.fontys.s3.carenestproject.persistance.entity.*;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.UserRepo;
import nl.fontys.s3.carenestproject.service.AddressService;
import nl.fontys.s3.carenestproject.service.exception.*;
import nl.fontys.s3.carenestproject.service.request.AuthRequest;
import nl.fontys.s3.carenestproject.service.request.CreateBaseAccountRequest;
import nl.fontys.s3.carenestproject.service.request.UpdateUserAddressRequest;
import nl.fontys.s3.carenestproject.service.response.AuthResponse;
import nl.fontys.s3.carenestproject.service.response.CreateBaseAccountResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private AddressService addressService;

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
        verify(userRepo, never()).save(any(UserEntity.class)); // Ensure no new user is saved
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
        long invalidId = 0L; // ID less than 1
        long authenticatedUserId = 0L; // Match ID to avoid unauthorized exception

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
        byte[] profileImage = "Test Image".getBytes(); // Example byte array for the image

        UserEntity userEntity = mockUserEntity(userId);
        userEntity.setProfileImage(profileImage); // Set the profile image in the mock entity

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
                .active(false) // Inactive user
                .build();

        when(userRepo.findUserEntityById(userId)).thenReturn(inactiveUser);

        // Act & Assert
        assertThrows(UserNotActiveException.class, () -> userService.updateUserAddress(request, userId, userId));

        // Verify the interaction
        verify(userRepo, times(1)).findUserEntityById(userId);
        verify(userRepo, never()).save(any(UserEntity.class)); // Ensure no save operation occurred
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
    void updateProfilePicture_ShouldThrowException_WhenUnauthorized() throws IOException {
        // Arrange
        long userId = 1L;
        long authenticatedUserId = 2L; // Different authenticated user
        MultipartFile file = mock(MultipartFile.class);

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> userService.updateProfilePicture(file, userId, authenticatedUserId));
        verify(userRepo, never()).save(any(UserEntity.class));
    }

    @Test
    void updateProfilePicture_ShouldThrowException_WhenUserNotActive() throws IOException {
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
    void updateProfilePicture_ShouldThrowException_WhenFileTypeIsInvalid() throws IOException {
        // Arrange
        long userId = 1L;
        long authenticatedUserId = 1L;
        MultipartFile file = mock(MultipartFile.class);

        when(userRepo.findUserEntityById(userId)).thenReturn(mockUserEntity(userId));
        when(file.getContentType()).thenReturn(MediaType.APPLICATION_PDF_VALUE); // Invalid file type

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
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3, 4}); // Mock file bytes

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


    // Utility methods for mocking
    private UserEntity mockUserEntity(long userId) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("password"); // Match the raw password used in the test

        return UserEntity.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password(encodedPassword)
                .gender(GenderEntity.builder().id(Gender.MALE.getValue()).genderName("MALE").build())
                .roleId(RoleEntity.builder().id(Role.PATIENT.getValue()).roleName(Role.PATIENT.name()).build()) // Replace with a valid role
                .active(true)
                .build();
    }


}
