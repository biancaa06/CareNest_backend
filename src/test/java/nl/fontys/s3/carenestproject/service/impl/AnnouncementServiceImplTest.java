package nl.fontys.s3.carenestproject.service.impl;

import nl.fontys.s3.carenestproject.domain.classes.Announcement;
import nl.fontys.s3.carenestproject.domain.classes.Gender;
import nl.fontys.s3.carenestproject.domain.classes.Position;
import nl.fontys.s3.carenestproject.domain.classes.Role;
import nl.fontys.s3.carenestproject.domain.classes.users.Manager;
import nl.fontys.s3.carenestproject.domain.classes.users.User;
import nl.fontys.s3.carenestproject.persistance.entity.*;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.AnnouncementRepo;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.ManagerRepo;
import nl.fontys.s3.carenestproject.service.ManagerService;
import nl.fontys.s3.carenestproject.service.exception.ObjectNotFoundException;
import nl.fontys.s3.carenestproject.service.exception.UnauthorizedException;
import nl.fontys.s3.carenestproject.service.request.CreateAnnouncementRequest;
import nl.fontys.s3.carenestproject.service.request.UpdateAnnouncementRequest;
import nl.fontys.s3.carenestproject.service.response.CreateAnnouncementResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnnouncementServiceImplTest {

    @Mock
    private AnnouncementRepo announcementRepo;
    @Mock
    private ManagerRepo managerRepo;

    @Mock
    private ManagerService managerService;

    @InjectMocks
    private AnnouncementServiceImpl announcementService;

    @BeforeEach
    void setUp() {
        announcementService = new AnnouncementServiceImpl(announcementRepo, managerService, managerRepo);
    }

    // Test: getAnnouncementById
    @Test
    void getAnnouncementById_ShouldReturnAnnouncement_WhenIdIsValid() {
        long id = 1L;
        AnnouncementEntity entity = mockAnnouncementEntityWithUser(1L);

        when(announcementRepo.findAnnouncementEntityById(id)).thenReturn(entity);

        Announcement result = announcementService.getAnnouncementById(id);

        assertNotNull(result);
        assertEquals(entity.getTitle(), result.getTitle());
        assertEquals(entity.getDescription(), result.getDescription());
        assertEquals(entity.getAuthor().getBaseUser().getFirstName(), result.getAuthor().getBaseUser().getFirstName());
    }

    @Test
    void getAnnouncementById_ShouldThrowException_WhenIdIsInvalid() {
        long id = 1L;
        when(announcementRepo.findAnnouncementEntityById(id)).thenReturn(null);

        assertThrows(ObjectNotFoundException.class, () -> announcementService.getAnnouncementById(id));
        verify(announcementRepo, times(1)).findAnnouncementEntityById(id);
    }

    // Test: getAnnouncementByTitle
    @Test
    void getAnnouncementByTitle_ShouldReturnAnnouncement_WhenTitleIsValid() {
        String title = "Valid Title";

        AnnouncementEntity entity = mockAnnouncementEntityWithUser(1L);
        entity.setTitle(title);

        when(announcementRepo.findAnnouncementEntityByTitle(title)).thenReturn(entity);

        Announcement result = announcementService.getAnnouncementByTitle(title);

        assertNotNull(result);
        assertEquals(title, result.getTitle());
        assertEquals(entity.getDescription(), result.getDescription());
    }

    @Test
    void getAnnouncementByTitle_ShouldThrowException_WhenTitleIsInvalid() {
        String title = "Invalid Title";

        when(announcementRepo.findAnnouncementEntityByTitle(title)).thenReturn(null);

        assertThrows(ObjectNotFoundException.class, () -> announcementService.getAnnouncementByTitle(title));
        verify(announcementRepo, times(1)).findAnnouncementEntityByTitle(title);
    }

    // Test: getAllAnnouncements
    @Test
    void getAllAnnouncements_ShouldReturnListOfAnnouncements_WhenDataExists() {
        List<AnnouncementEntity> entities = List.of(
                mockAnnouncementEntityWithUser(1L),
                mockAnnouncementEntityWithUser(2L)
        );

        when(announcementRepo.findAll()).thenReturn(entities);

        List<Announcement> result = announcementService.getAllAnnouncements();

        assertNotNull(result);
        assertEquals(entities.size(), result.size());
    }

    @Test
    void getAllAnnouncements_ShouldReturnEmptyList_WhenNoDataExists() {
        when(announcementRepo.findAll()).thenReturn(List.of());

        List<Announcement> result = announcementService.getAllAnnouncements();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(announcementRepo, times(1)).findAll();
    }

    // Test: createAnnouncement
    @Test
    void createAnnouncement_ShouldThrowException_WhenAuthorIdIsInvalid() {
        CreateAnnouncementRequest request = CreateAnnouncementRequest.builder()
                .title("New Title")
                .description("New Description")
                .authorId(0L) // Invalid ID
                .build();

        assertThrows(IllegalArgumentException.class, () -> announcementService.createAnnouncement(request));
        verify(announcementRepo, never()).save(any());
    }

    @Test
    void createAnnouncement_ShouldThrowException_WhenManagerNotFound() {
        CreateAnnouncementRequest request = CreateAnnouncementRequest.builder()
                .title("New Title")
                .description("New Description")
                .authorId(99L) // Valid ID, but manager does not exist
                .build();

        when(managerService.getManagerById(request.getAuthorId())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> announcementService.createAnnouncement(request));
        verify(announcementRepo, never()).save(any());
    }

    @Test
    void createAnnouncement_ShouldReturnResponse_WhenValidRequest() {
        CreateAnnouncementRequest request = CreateAnnouncementRequest.builder()
                .title("New Title")
                .description("New Description")
                .authorId(1L)
                .build();

        Manager manager = mockManager();
        when(managerService.getManagerById(request.getAuthorId())).thenReturn(manager);

        AnnouncementEntity savedEntity = mockAnnouncementEntityWithUser(1L);
        savedEntity.setTitle(request.getTitle());
        savedEntity.setDescription(request.getDescription());

        when(announcementRepo.save(any(AnnouncementEntity.class))).thenReturn(savedEntity);

        CreateAnnouncementResponse response = announcementService.createAnnouncement(request);

        assertNotNull(response);
        assertEquals(request.getTitle(), response.getTitle());
        assertEquals(request.getDescription(), response.getDescription());
        verify(announcementRepo, times(1)).save(any(AnnouncementEntity.class));
    }

    // Test: updateAnnouncement
    @Test
    void updateAnnouncement_ShouldUpdateSuccessfully_WhenValid() {
        long id = 1L;
        long userId = 1L;
        UpdateAnnouncementRequest request = UpdateAnnouncementRequest.builder()
                .title("Updated Title")
                .description("Updated Description")
                .build();

        AnnouncementEntity entity = mockAnnouncementEntityWithUser(userId);
        when(announcementRepo.findAnnouncementEntityById(id)).thenReturn(entity);

        Date expectedDate = new Date();

        announcementService.updateAnnouncement(id, request, userId);

        verify(announcementRepo, times(1)).updateAnnouncementEntity(
                eq(id),
                eq(request.getTitle()),
                eq(request.getDescription()),
                argThat(date -> Math.abs(date.getTime() - expectedDate.getTime()) < 1000)
        );
    }


    @Test
    void updateAnnouncement_ShouldThrowException_WhenAnnouncementNotFound() {
        long id = 1L;
        long userId = 1L;
        UpdateAnnouncementRequest request = UpdateAnnouncementRequest.builder()
                .title("Updated Title")
                .description("Updated Description")
                .build();

        when(announcementRepo.findAnnouncementEntityById(id)).thenReturn(null);
        assertThrows(ObjectNotFoundException.class, () -> announcementService.updateAnnouncement(id, request, userId));
        verify(announcementRepo, never()).updateAnnouncementEntity(anyLong(), any(), any(), any());
    }

    @Test
    void updateAnnouncement_ShouldThrowException_WhenUserNotAuthorized() {
        long id = 1L;
        long userId = 2L;
        UpdateAnnouncementRequest request = UpdateAnnouncementRequest.builder()
                .title("Updated Title")
                .description("Updated Description")
                .build();

        AnnouncementEntity entity = mockAnnouncementEntityWithUser(1L);
        when(announcementRepo.findAnnouncementEntityById(id)).thenReturn(entity);

        assertThrows(UnauthorizedException.class, () -> announcementService.updateAnnouncement(id, request, userId));
        verify(announcementRepo, never()).updateAnnouncementEntity(anyLong(), any(), any(), any());
    }

    // Test: deleteAnnouncementById
    @Test
    void deleteAnnouncementById_ShouldDeleteSuccessfully() {
        long id = 1L;

        announcementService.deleteAnnouncementById(id);

        verify(announcementRepo, times(1)).deleteAnnouncementById(id);
    }

    @Test
    void getAnnouncementsByAuthor_ShouldReturnAnnouncements_WhenAuthorExistsAndHasAnnouncements() {
        // Arrange
        long authorId = 1L;
        ManagerEntity authorEntity = mockAnnouncementEntityWithUser(authorId).getAuthor();
        AnnouncementEntity announcementEntity = mockAnnouncementEntityWithUser(authorId);

        when(managerRepo.existsById(authorId)).thenReturn(true);
        when(managerRepo.findManagerEntityById(authorId)).thenReturn(authorEntity);
        when(announcementRepo.findAnnouncementEntitiesByAuthor(authorEntity)).thenReturn(List.of(announcementEntity));

        // Act
        List<Announcement> announcements = announcementService.getAnnouncementsByAuthor(authorId);

        // Assert
        assertNotNull(announcements);
        assertEquals(1, announcements.size());
        assertEquals(announcementEntity.getTitle(), announcements.get(0).getTitle());
        assertEquals(announcementEntity.getDescription(), announcements.get(0).getDescription());
        verify(managerRepo, times(1)).existsById(authorId);
        verify(managerRepo, times(1)).findManagerEntityById(authorId);
        verify(announcementRepo, times(1)).findAnnouncementEntitiesByAuthor(authorEntity);
    }

    @Test
    void getAnnouncementsByAuthor_ShouldThrowException_WhenAuthorDoesNotExist() {
        // Arrange
        long authorId = 1L;
        when(managerRepo.existsById(authorId)).thenReturn(false);

        // Run the test
        assertThatThrownBy(() -> announcementService.getAnnouncementsByAuthor(authorId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Author with ID " + authorId + " not found");

        verify(managerRepo, times(1)).existsById(authorId);
        verify(managerRepo, never()).findManagerEntityById(anyLong());
        verify(announcementRepo, never()).findAnnouncementEntitiesByAuthor(any());
    }

    @Test
    void getAnnouncementsByAuthor_ShouldReturnEmptyList_WhenAuthorExistsButHasNoAnnouncements() {
        // Arrange
        long authorId = 1L;
        ManagerEntity authorEntity = mockAnnouncementEntityWithUser(authorId).getAuthor();

        when(managerRepo.existsById(authorId)).thenReturn(true);
        when(managerRepo.findManagerEntityById(authorId)).thenReturn(authorEntity);
        when(announcementRepo.findAnnouncementEntitiesByAuthor(authorEntity)).thenReturn(new ArrayList<>());

        // Act
        List<Announcement> announcements = announcementService.getAnnouncementsByAuthor(authorId);

        // Assert
        assertNotNull(announcements);
        assertTrue(announcements.isEmpty());
        verify(managerRepo, times(1)).existsById(authorId);
        verify(managerRepo, times(1)).findManagerEntityById(authorId);
        verify(announcementRepo, times(1)).findAnnouncementEntitiesByAuthor(authorEntity);
    }


    // Utility Methods
    private AnnouncementEntity mockAnnouncementEntityWithUser(long userId) {
        GenderEntity genderEntity = GenderEntity.builder()
                .id(1L)
                .genderName("MALE")
                .build();

        AddressEntity addressEntity = AddressEntity.builder()
                .city("Test City")
                .country("Test Country")
                .street("Test Street")
                .number(123)
                .build();

        RoleEntity roleEntity = RoleEntity.builder()
                .id(1L)
                .roleName("MANAGER")
                .build();

        PositionEntity positionEntity = PositionEntity.builder()
                .id(1L)
                .positionName("PR")
                .build();

        UserEntity userEntity = UserEntity.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .gender(genderEntity)
                .address(addressEntity)
                .roleId(roleEntity)
                .build();

        ManagerEntity managerEntity = ManagerEntity.builder()
                .baseUser(userEntity)
                .position(positionEntity)
                .build();

        return AnnouncementEntity.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .author(managerEntity)
                .date(new Date())
                .build();
    }

    private Manager mockManager() {
        return Manager.builder()
                .baseUser(mockBaseUser())
                .position(Position.PR)
                .build();
    }

    private User mockBaseUser() {
        return User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .role(Role.MANAGER)
                .gender(Gender.MALE)
                .build();
    }

}
