package nl.fontys.s3.carenestproject.service.impl;

import nl.fontys.s3.carenestproject.domain.classes.Role;
import nl.fontys.s3.carenestproject.persistance.entity.*;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.PatientRepo;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.SicknessOfPatientRepo;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.SicknessRepo;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.UserRepo;
import nl.fontys.s3.carenestproject.service.request.CreatePatientAccountRequest;
import nl.fontys.s3.carenestproject.service.request.SicknessInputListRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PatientServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private SicknessOfPatientRepo sicknessOfPatientRepo;

    @Mock
    private SicknessRepo sicknessRepo;

    @Mock
    private PatientRepo patientRepo;

    @InjectMocks
    private PatientServiceImpl patientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPatientAccount_ShouldCreateAccount_WhenValidRequest() {
        // Arrange
        long baseUserId = 1L;
        long sicknessId1 = 101L;
        long sicknessId2 = 102L;

        CreatePatientAccountRequest request = CreatePatientAccountRequest.builder()
                .baseUserId(baseUserId)
                .personalDescription("Test description")
                .sicknesses(List.of(
                        SicknessInputListRequest.builder().sicknessId(sicknessId1).build(),
                        SicknessInputListRequest.builder().sicknessId(sicknessId2).build()
                ))
                .build();

        UserEntity baseUser = mockUserEntity(baseUserId);

        SicknessEntity sicknessEntity1 = mockSicknessEntity(sicknessId1);
        SicknessEntity sicknessEntity2 = mockSicknessEntity(sicknessId2);

        when(userRepo.findUserEntityById(baseUserId)).thenReturn(baseUser);
        when(sicknessRepo.findSicknessEntityById(sicknessId1)).thenReturn(sicknessEntity1);
        when(sicknessRepo.findSicknessEntityById(sicknessId2)).thenReturn(sicknessEntity2);

        // Act
        patientService.createPatientAccount(request);

        // Assert
        verify(patientRepo, times(1)).save(any(PatientEntity.class));
        verify(userRepo, times(1)).save(baseUser);
        verify(sicknessOfPatientRepo, times(2)).save(any(SicknessesOfPatient.class));
    }

    @Test
    void createPatientAccount_ShouldThrowException_WhenSicknessListIsEmpty() {
        // Arrange
        CreatePatientAccountRequest request = CreatePatientAccountRequest.builder()
                .baseUserId(1L)
                .personalDescription("Test description")
                .sicknesses(List.of())
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> patientService.createPatientAccount(request));
        verifyNoInteractions(patientRepo, userRepo, sicknessRepo, sicknessOfPatientRepo);
    }

    @Test
    void createPatientAccount_ShouldThrowException_WhenSicknessListIsNull() {
        // Arrange
        CreatePatientAccountRequest request = CreatePatientAccountRequest.builder()
                .baseUserId(1L)
                .personalDescription("Test description")
                .sicknesses(null)
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> patientService.createPatientAccount(request));
        verifyNoInteractions(patientRepo, userRepo, sicknessRepo, sicknessOfPatientRepo);
    }

    @Test
    void createPatientAccount_ShouldHandleNonExistingSickness() {
        // Arrange
        long baseUserId = 1L;
        long sicknessId1 = 101L;
        long sicknessId2 = 102L;

        CreatePatientAccountRequest request = CreatePatientAccountRequest.builder()
                .baseUserId(baseUserId)
                .personalDescription("Test description")
                .sicknesses(List.of(
                        SicknessInputListRequest.builder().sicknessId(sicknessId1).build(),
                        SicknessInputListRequest.builder().sicknessId(sicknessId2).build()
                ))
                .build();

        UserEntity baseUser = mockUserEntity(baseUserId);
        SicknessEntity sicknessEntity1 = mockSicknessEntity(sicknessId1);

        when(userRepo.findUserEntityById(baseUserId)).thenReturn(baseUser);
        when(sicknessRepo.findSicknessEntityById(sicknessId1)).thenReturn(sicknessEntity1);
        when(sicknessRepo.findSicknessEntityById(sicknessId2)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> patientService.createPatientAccount(request));

        assertEquals("Sickness with ID 102 not found", exception.getMessage());

        // Verify interactions
        verify(userRepo, times(1)).findUserEntityById(baseUserId);
        verify(sicknessRepo, times(2)).findSicknessEntityById(anyLong());
        verify(patientRepo, times(1)).save(any(PatientEntity.class));
        verify(sicknessOfPatientRepo, never()).save(any(SicknessesOfPatient.class));
    }




    // Utility methods for mocking
    private UserEntity mockUserEntity(long userId) {
        return UserEntity.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .roleId(RoleEntity.builder()
                        .id(Role.PATIENT.getValue())
                        .roleName(Role.PATIENT.name())
                        .build())
                .build();
    }

    private SicknessEntity mockSicknessEntity(long sicknessId) {
        return SicknessEntity.builder()
                .id(sicknessId)
                .name("Test Sickness")
                .build();
    }
}
