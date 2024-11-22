package nl.fontys.s3.carenestproject.service.impl;

import nl.fontys.s3.carenestproject.domain.classes.Position;
import nl.fontys.s3.carenestproject.domain.classes.Role;
import nl.fontys.s3.carenestproject.domain.classes.users.Manager;
import nl.fontys.s3.carenestproject.persistance.entity.*;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.ManagerRepo;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.PositionRepo;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.UserRepo;
import nl.fontys.s3.carenestproject.service.exception.UserNotActiveException;
import nl.fontys.s3.carenestproject.service.mapping.PositionConverter;
import nl.fontys.s3.carenestproject.service.request.CreateManagerAccountRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.InvalidParameterException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManagerServiceImplTest {

    @Mock
    private PositionRepo positionRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private ManagerRepo managerRepo;

    @InjectMocks
    private ManagerServiceImpl managerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getManagerById_ShouldReturnManager_WhenIdIsValid() {
        // Arrange
        long managerId = 1L;
        ManagerEntity managerEntity = mockManagerEntity(managerId);
        when(managerRepo.findManagerEntityById(managerId)).thenReturn(managerEntity);

        // Act
        Manager result = managerService.getManagerById(managerId);

        // Assert
        assertNotNull(result);
        assertEquals(managerId, result.getBaseUser().getId());
        verify(managerRepo, times(1)).findManagerEntityById(managerId);
    }

    @Test
    void getManagerById_ShouldThrowException_WhenManagerNotFound() {
        // Arrange
        long managerId = 1L;
        when(managerRepo.findManagerEntityById(managerId)).thenReturn(null);

        // Act & Assert
        UserNotActiveException exception = assertThrows(UserNotActiveException.class,
                () -> managerService.getManagerById(managerId));

        // Assert exception message includes the custom error
        assertEquals("404 NOT_FOUND \"Manager not found\"", exception.getMessage());
        verify(managerRepo, times(1)).findManagerEntityById(managerId);
    }



    @Test
    void createManagerAccount_ShouldCreateManager_WhenValidRequest() {
        // Arrange
        CreateManagerAccountRequest request = CreateManagerAccountRequest.builder()
                .baseUserId(1L)
                .position(Position.PR.getValue())
                .build();

        UserEntity userEntity = mockUserEntity(request.getBaseUserId());

        when(positionRepo.existsById(request.getPosition())).thenReturn(true);
        when(userRepo.existsById(request.getBaseUserId())).thenReturn(true);
        when(managerRepo.existsById(request.getBaseUserId())).thenReturn(false);
        when(userRepo.findUserEntityById(request.getBaseUserId())).thenReturn(userEntity);

        // Act
        managerService.createManagerAccount(request);

        // Assert
        verify(managerRepo, times(1)).save(any(ManagerEntity.class));
        verify(userRepo, times(1)).save(userEntity);
    }

    @Test
    void createManagerAccount_ShouldThrowException_WhenPositionNotFound() {
        // Arrange
        CreateManagerAccountRequest request = CreateManagerAccountRequest.builder()
                .baseUserId(1L)
                .position(99L) // Invalid position
                .build();

        when(positionRepo.existsById(request.getPosition())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidParameterException.class, () -> managerService.createManagerAccount(request));
        verify(positionRepo, times(1)).existsById(request.getPosition());
        verify(managerRepo, never()).save(any(ManagerEntity.class));
    }

    @Test
    void createManagerAccount_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        CreateManagerAccountRequest request = CreateManagerAccountRequest.builder()
                .baseUserId(99L) // Non-existing user
                .position(Position.PR.getValue())
                .build();

        when(positionRepo.existsById(request.getPosition())).thenReturn(true);
        when(userRepo.existsById(request.getBaseUserId())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidParameterException.class, () -> managerService.createManagerAccount(request));
        verify(userRepo, times(1)).existsById(request.getBaseUserId());
        verify(managerRepo, never()).save(any(ManagerEntity.class));
    }

    @Test
    void createManagerAccount_ShouldThrowException_WhenManagerAlreadyExists() {
        // Arrange
        CreateManagerAccountRequest request = CreateManagerAccountRequest.builder()
                .baseUserId(1L)
                .position(Position.PR.getValue())
                .build();

        when(positionRepo.existsById(request.getPosition())).thenReturn(true);
        when(userRepo.existsById(request.getBaseUserId())).thenReturn(true);
        when(managerRepo.existsById(request.getBaseUserId())).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidParameterException.class, () -> managerService.createManagerAccount(request));
        verify(managerRepo, never()).save(any(ManagerEntity.class));
    }

    @Test
    void getManagersByPosition_ShouldReturnManagers_WhenPositionExists() {
        // Arrange
        long positionId = Position.PR.getValue();
        Position position = Position.fromNumericValue(positionId);
        List<ManagerEntity> managerEntities = List.of(mockManagerEntity(1L), mockManagerEntity(2L));

        when(managerRepo.findManagerEntitiesByPosition(PositionConverter.convertFromBaseToEntity(position)))
                .thenReturn(managerEntities);

        // Act
        List<Manager> result = managerService.getManagersByPosition(positionId);

        // Assert
        assertNotNull(result);
        assertEquals(managerEntities.size(), result.size());
        verify(managerRepo, times(1)).findManagerEntitiesByPosition(any(PositionEntity.class));
    }

    @Test
    void getManagersByPosition_ShouldReturnEmptyList_WhenNoManagersExist() {
        // Arrange
        long positionId = Position.PR.getValue();
        Position position = Position.fromNumericValue(positionId);

        when(managerRepo.findManagerEntitiesByPosition(PositionConverter.convertFromBaseToEntity(position)))
                .thenReturn(List.of());

        // Act
        List<Manager> result = managerService.getManagersByPosition(positionId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(managerRepo, times(1)).findManagerEntitiesByPosition(any(PositionEntity.class));
    }

    // Utility Methods
    private ManagerEntity mockManagerEntity(long managerId) {
        return ManagerEntity.builder()
                .baseUser(mockUserEntity(managerId))
                .position(PositionEntity.builder()
                        .id(Position.PR.getValue())
                        .positionName(Position.PR.name())
                        .build())
                .build();
    }

    private UserEntity mockUserEntity(long userId) {
        GenderEntity genderEntity = GenderEntity.builder()
                .id(1L)
                .genderName("MALE")
                .build();

        return UserEntity.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .gender(genderEntity) // Set GenderEntity
                .roleId(RoleEntity.builder()
                        .id(Role.MANAGER.getValue())
                        .roleName(Role.MANAGER.name())
                        .build())
                .build();
    }

}
