package nl.fontys.s3.carenestproject.service.impl;

import nl.fontys.s3.carenestproject.domain.classes.Sickness;
import nl.fontys.s3.carenestproject.persistance.entity.SicknessEntity;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.SicknessRepo;
import nl.fontys.s3.carenestproject.service.request.CreateSicknessRequest;
import nl.fontys.s3.carenestproject.service.request.UpdateSicknessRequest;
import nl.fontys.s3.carenestproject.service.response.CreateSicknessResponse;
import nl.fontys.s3.carenestproject.service.response.UpdateSicknessResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SicknessServiceImplTest {

    @Mock
    private SicknessRepo sicknessMockRepo;
    @InjectMocks
    private SicknessServiceImpl sicknessService;


    @BeforeEach
    void setUp() {
        sicknessService = new SicknessServiceImpl(sicknessMockRepo);
    }

    @Test
    void getSicknessById_ShouldReturnExistingSickness() {
        SicknessEntity sicknessEntity = SicknessEntity.builder().id(1L).name("Sickness").build();
        when(sicknessMockRepo.findSicknessEntityById(1L)).thenReturn(sicknessEntity);

        Sickness sickness = sicknessService.getSicknessById(1L);

        assertThat(sickness.getId()).isEqualTo(1L);
        assertThat(sickness.getName()).isEqualTo("Sickness");
        assertThat(sickness).isNotNull();
    }

    @Test
    void getSicknessById_ShouldReturnNull_InvalidId() {

        when(sicknessMockRepo.findSicknessEntityById(0L)).thenReturn(null);

        Sickness sickness = sicknessService.getSicknessById(0L);

        assertThat(sickness).isNull();

    }

    @Test
    void getAllSicknesses_ShouldReturnAllSicknesses() {
        SicknessEntity sickness1 = SicknessEntity.builder().id(1L).name("Sickness1").build();
        SicknessEntity sickness2 = SicknessEntity.builder().id(2L).name("Sickness2").build();
        SicknessEntity sickness3 = SicknessEntity.builder().id(3L).name("Sickness3").build();

        when(sicknessMockRepo.findAll()).thenReturn(List.of(sickness1, sickness2, sickness3));

        List<Sickness> allSicknesses = sicknessService.getAllSicknesses();

        assertThat(allSicknesses).hasSize(3);
        assertThat(allSicknesses.get(0).getName()).isEqualTo(sickness1.getName());
        assertThat(allSicknesses.get(1).getName()).isEqualTo(sickness2.getName());
        assertThat(allSicknesses.get(2).getName()).isEqualTo(sickness3.getName());
    }

    @Test
    void getAllSicknesses_ShouldReturnEmptyList() {
        when(sicknessMockRepo.findAll()).thenReturn(List.of());

        List<Sickness> allSicknesses = sicknessService.getAllSicknesses();

        assertThat(allSicknesses).isEmpty();
    }

    @Test
    void createSickness_ShouldCreateNewSickness() {

        CreateSicknessRequest request = CreateSicknessRequest.builder().name("Sickness").build();

        SicknessEntity sicknessEntity = SicknessEntity.builder().id(4L).name("Sickness").build();

        when(sicknessMockRepo.save(any(SicknessEntity.class))).thenReturn(sicknessEntity);

        CreateSicknessResponse response = sicknessService.createSickness(request);

        verify(sicknessMockRepo).save(any(SicknessEntity.class));
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(4L);
        assertThat(response.getName()).isEqualTo("Sickness");
    }

    @Test
    void createSickness_ShouldThrowException_WhenNameIsNull() {

        CreateSicknessRequest request = CreateSicknessRequest.builder().name(null).build();

        assertThrows(IllegalArgumentException.class, () -> sicknessService.createSickness(request));

        verify(sicknessMockRepo, never()).save(any(SicknessEntity.class));
    }

    @Test
    void deleteSickness_ShouldDeleteSuccessfully_WhenSicknessIsValid() {

        Sickness sickness = Sickness.builder().id(1L).name("Valid Sickness").build();

        sicknessService.deleteSicknessById(sickness.getId());

        verify(sicknessMockRepo).deleteSicknessEntityById(1L);
    }

    @Test
    void deleteSickness_ShouldThrowException_WhenSicknessIsNull() {
        assertThrows(IllegalArgumentException.class, () -> sicknessService.deleteSicknessById(0L));

        verify(sicknessMockRepo, never()).deleteSicknessEntityById(anyLong());
    }

    @Test
    void deleteSickness_ShouldThrowException_WhenSicknessIdIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> sicknessService.deleteSicknessById(0L));

        verify(sicknessMockRepo, never()).deleteSicknessEntityById(anyLong());
    }

    @Test
    void updateSickness_ShouldUpdateSickness_validData() {
        UpdateSicknessRequest request = UpdateSicknessRequest.builder()
                .newSicknessName("Updated Sickness Name")
                .build();

        SicknessEntity existingEntity = SicknessEntity.builder()
                .id(1L)
                .name("Old Sickness Name")
                .build();

        SicknessEntity updatedEntity = SicknessEntity.builder()
                .id(1L)
                .name("Updated Sickness Name")
                .build();

        when(sicknessMockRepo.findSicknessEntityById(1L)).thenReturn(existingEntity);

        when(sicknessMockRepo.save(any(SicknessEntity.class))).thenReturn(updatedEntity);

        UpdateSicknessResponse response = sicknessService.updateSickness(1L, request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNewName()).isEqualTo("Updated Sickness Name");

        verify(sicknessMockRepo).save(any(SicknessEntity.class));
    }

    @Test
    void updateSickness_ShouldThrowException_WhenSicknessIdIsInvalid() {
        UpdateSicknessRequest request = UpdateSicknessRequest.builder()
                .newSicknessName("Updated Sickness Name")
                .build();

        assertThrows(IllegalArgumentException.class, () -> sicknessService.updateSickness(0L, request));

        verify(sicknessMockRepo, never()).save(any(SicknessEntity.class));
    }

    @Test
    void updateSickness_ShouldThrowException_WhenNewSicknessNameIsNull() {
        UpdateSicknessRequest request = UpdateSicknessRequest.builder()
                .newSicknessName(null)
                .build();

        assertThrows(IllegalArgumentException.class, () -> sicknessService.updateSickness(1L, request),
                "Expected IllegalArgumentException for null sickness name");

        verify(sicknessMockRepo, never()).save(any(SicknessEntity.class));
    }
    @Test
    void updateSickness_ShouldThrowException_WhenSicknessAlreadyExists() {
        UpdateSicknessRequest request = UpdateSicknessRequest.builder()
                .newSicknessName("Existing Sickness Name")
                .build();

        when(sicknessMockRepo.findAll()).thenReturn(List.of(
                SicknessEntity.builder().id(2L).name("Existing Sickness Name").build()
        ));

        assertThrows(IllegalArgumentException.class, () -> sicknessService.updateSickness(2L,request));

        verify(sicknessMockRepo, never()).save(any(SicknessEntity.class));
    }

    @Test
    void updateSickness_ShouldThrowException_WhenIdIsZeroOrNegative() {
        UpdateSicknessRequest request = UpdateSicknessRequest.builder()
                .newSicknessName("Valid Name")
                .build();

        assertThrows(IllegalArgumentException.class, () -> sicknessService.updateSickness(0L, request),
                "Expected IllegalArgumentException for invalid ID (0)");
        assertThrows(IllegalArgumentException.class, () -> sicknessService.updateSickness(-1L, request),
                "Expected IllegalArgumentException for invalid ID (-1)");

        verify(sicknessMockRepo, never()).save(any(SicknessEntity.class));
    }


}