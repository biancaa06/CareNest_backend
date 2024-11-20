package nl.fontys.s3.carenestproject.service.impl;

import nl.fontys.s3.carenestproject.domain.classes.Role;
import nl.fontys.s3.carenestproject.domain.classes.users.Caretaker;
import nl.fontys.s3.carenestproject.persistance.entity.*;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.*;
import nl.fontys.s3.carenestproject.service.request.CreateCaretakerAccountRequest;
import nl.fontys.s3.carenestproject.service.request.SicknessInputListRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.InvalidParameterException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CaretakerServiceImplTest {

    @Mock
    private SicknessOfCaretakerRepo sicknessOfCaretakerRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private SicknessRepo sicknessRepo;

    @Mock
    private CaretakerRepo caretakerRepo;

    @InjectMocks
    private CaretakerServiceImpl caretakerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test: createCaretakerAccount
    @Test
    void createCaretakerAccount_ShouldCreateAccount_WhenValidRequest() {
        // Arrange
        long baseUserId = 1L;
        long sicknessId1 = 101L;
        long sicknessId2 = 102L;

        CreateCaretakerAccountRequest request = CreateCaretakerAccountRequest.builder()
                .baseUserId(baseUserId)
                .availabilityId(1L)
                .personalDescription("Test description")
                .salaryPerHour(25.0)
                .specialisations(List.of(
                        SicknessInputListRequest.builder().sicknessId(sicknessId1).build(),
                        SicknessInputListRequest.builder().sicknessId(sicknessId2).build()
                ))
                .build();

        UserEntity baseUser = mockUserEntity(baseUserId);
        SicknessEntity sicknessEntity1 = mockSicknessEntity(sicknessId1);
        SicknessEntity sicknessEntity2 = mockSicknessEntity(sicknessId2);
        CaretakerEntity caretakerEntity = mockCaretakerEntity(baseUser);

        when(userRepo.findUserEntityById(baseUserId)).thenReturn(baseUser);
        when(caretakerRepo.existsById(baseUserId)).thenReturn(false);
        when(sicknessRepo.findSicknessEntityById(sicknessId1)).thenReturn(sicknessEntity1);
        when(sicknessRepo.findSicknessEntityById(sicknessId2)).thenReturn(sicknessEntity2);
        when(caretakerRepo.save(any(CaretakerEntity.class))).thenReturn(caretakerEntity);

        // Act
        caretakerService.createCaretakerAccount(request);

        // Assert
        verify(caretakerRepo, times(1)).save(any(CaretakerEntity.class));
        verify(userRepo, times(1)).save(baseUser);
        verify(sicknessOfCaretakerRepo, times(2)).save(any(SicknessesForCaretaker.class));
    }

    @Test
    void createCaretakerAccount_ShouldThrowException_WhenBaseUserIsNull() {
        // Arrange
        long baseUserId = 1L;

        CreateCaretakerAccountRequest request = CreateCaretakerAccountRequest.builder()
                .baseUserId(baseUserId)
                .availabilityId(1L)
                .personalDescription("Test description")
                .salaryPerHour(25.0)
                .specialisations(List.of())
                .build();

        when(userRepo.findUserEntityById(baseUserId)).thenReturn(null);

        // Act & Assert
        assertThrows(InvalidParameterException.class, () -> caretakerService.createCaretakerAccount(request));
    }

    @Test
    void createCaretakerAccount_ShouldThrowException_WhenSpecialisationsIsEmpty() {
        // Arrange
        long baseUserId = 1L;

        CreateCaretakerAccountRequest request = CreateCaretakerAccountRequest.builder()
                .baseUserId(baseUserId)
                .availabilityId(1L)
                .personalDescription("Test description")
                .salaryPerHour(25.0)
                .specialisations(List.of()) // Empty specialisations
                .build();

        UserEntity baseUser = mockUserEntity(baseUserId);
        when(userRepo.findUserEntityById(baseUserId)).thenReturn(baseUser);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> caretakerService.createCaretakerAccount(request));
    }
    @Test
    void createCaretakerAccount_ShouldThrowException_WhenCaretakerAccountAlreadyExists() {
        // Arrange
        long baseUserId = 1L;

        CreateCaretakerAccountRequest request = CreateCaretakerAccountRequest.builder()
                .baseUserId(baseUserId)
                .availabilityId(1L)
                .personalDescription("Test description")
                .salaryPerHour(25.0)
                .specialisations(List.of(
                        SicknessInputListRequest.builder().sicknessId(101L).build()
                ))
                .build();

        UserEntity baseUser = mockUserEntity(baseUserId);

        when(userRepo.findUserEntityById(baseUserId)).thenReturn(baseUser);
        when(caretakerRepo.existsById(baseUserId)).thenReturn(true); // Simulate existing caretaker

        // Act & Assert
        InvalidParameterException exception = assertThrows(InvalidParameterException.class,
                () -> caretakerService.createCaretakerAccount(request));

        assertEquals("The account you are trying to create already exists", exception.getMessage());

        // Verify that no other operations occur
        verify(caretakerRepo, never()).save(any(CaretakerEntity.class));
        verify(userRepo, never()).save(any(UserEntity.class));
        verify(sicknessOfCaretakerRepo, never()).save(any(SicknessesForCaretaker.class));
    }


    // Test: getCaretakers
    @Test
    void getCaretakers_ShouldReturnCaretakerList_WhenCaretakersExist() {
        // Arrange
        List<CaretakerEntity> caretakerEntities = List.of(mockCaretakerEntity(mockUserEntity(1L)), mockCaretakerEntity(mockUserEntity(2L)));
        when(caretakerRepo.findAll()).thenReturn(caretakerEntities);

        // Act
        List<Caretaker> result = caretakerService.getCaretakers();

        // Assert
        assertNotNull(result);
        assertEquals(caretakerEntities.size(), result.size());
        verify(caretakerRepo, times(1)).findAll();
    }

    @Test
    void getCaretakers_ShouldReturnEmptyList_WhenNoCaretakersExist() {
        // Arrange
        when(caretakerRepo.findAll()).thenReturn(List.of());

        // Act
        List<Caretaker> result = caretakerService.getCaretakers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(caretakerRepo, times(1)).findAll();
    }

    // Utility methods for mocking
    private UserEntity mockUserEntity(long userId) {
        return UserEntity.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .gender(GenderEntity.builder()
                        .id(1L)
                        .genderName("MALE")
                        .build())
                .roleId(RoleEntity.builder()
                        .id(Role.CARETAKER.getValue())
                        .roleName(Role.CARETAKER.name())
                        .build())
                .build();
    }

    private SicknessEntity mockSicknessEntity(long sicknessId) {
        return SicknessEntity.builder()
                .id(sicknessId)
                .name("Test Sickness")
                .build();
    }

    private CaretakerEntity mockCaretakerEntity(UserEntity baseUser) {
        return CaretakerEntity.builder()
                .baseUser(baseUser)
                .availability(AvailabilityEntity.builder().id(1L).availabilityName("FULL_TIME").build())
                .personalDescription("Test description")
                .salaryPerHour(25.0)
                .build();
    }
}
