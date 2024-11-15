package nl.fontys.s3.carenestproject.service.impl;

import nl.fontys.s3.carenestproject.domain.classes.Announcement;
import nl.fontys.s3.carenestproject.persistance.entity.*;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.AnnouncementRepo;
import nl.fontys.s3.carenestproject.service.ManagerService;
import nl.fontys.s3.carenestproject.service.mapping.ManagerConverter;
import nl.fontys.s3.carenestproject.service.request.CreateAnnouncementRequest;
import nl.fontys.s3.carenestproject.service.request.UpdateAnnouncementRequest;
import nl.fontys.s3.carenestproject.service.response.CreateAnnouncementResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnnouncementServiceImplTest {

    @Mock
    private AnnouncementRepo announcementRepo;

    @Mock
    private ManagerService managerService;

    @InjectMocks
    private AnnouncementServiceImpl announcementService;

    @BeforeEach
    void setUp() {
        announcementService = new AnnouncementServiceImpl(announcementRepo, managerService);
    }

    // getAnnouncementById

    @Test
    void getAnnouncementById_ShouldReturnAnnouncement_WhenIdIsValid() {
        long id = 1L;

        AddressEntity address = AddressEntity.builder()
                .id(1L)
                .city("Test City")
                .country("Test Country")
                .street("Test Street")
                .number(123)
                .build();

        GenderEntity genderEntity = GenderEntity.builder()
                .id(1L)
                .genderName("MALE")
                .build();

        RoleEntity roleEntity = RoleEntity.builder().id(1L).roleName("MANAGER").build();

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .gender(genderEntity)
                .email("john.doe@example.com")
                .address(address)
                .roleId(roleEntity)
                .build();
        PositionEntity positionEntity = PositionEntity.builder().id(1L).positionName("PR").build();
        ManagerEntity managerEntity = ManagerEntity.builder().baseUser(userEntity).position(positionEntity).build();

        AnnouncementEntity entity = AnnouncementEntity.builder()
                .title("Announcement Title")
                .description("Announcement Description")
                .author(managerEntity)
                .date(new Date())
                .build();

        when(announcementRepo.findAnnouncementEntityById(id)).thenReturn(entity);

        // Act
        Announcement result = announcementService.getAnnouncementById(id);

        // Assert
        assertNotNull(result);
        assertEquals("Announcement Title", result.getTitle());
        assertEquals("Announcement Description", result.getDescription());
        assertNotNull(result.getAuthor());
        assertEquals("John", result.getAuthor().getBaseUser().getFirstName());
    }


    @Test
    void getAnnouncementById_ShouldReturnNull_WhenIdIsInvalid() {
        long id = 1L;
        when(announcementRepo.findAnnouncementEntityById(id)).thenReturn(null);

        Announcement result = announcementService.getAnnouncementById(id);

        assertNull(result);
        verify(announcementRepo, times(1)).findAnnouncementEntityById(id);
    }

    // getAnnouncementByTitle

    @Test
    void getAnnouncementByTitle_ShouldReturnAnnouncement_WhenTitleIsValid() {
        String title = "Valid Title";

        AddressEntity address = AddressEntity.builder()
                .id(1L)
                .city("Test City")
                .country("Test Country")
                .street("Test Street")
                .number(123)
                .build();

        GenderEntity genderEntity = GenderEntity.builder()
                .id(1L)
                .genderName("MALE")
                .build();

        RoleEntity roleEntity = RoleEntity.builder().id(1L).roleName("MANAGER").build();

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .gender(genderEntity)
                .email("john.doe@example.com")
                .address(address)
                .roleId(roleEntity)
                .build();
        PositionEntity positionEntity = PositionEntity.builder().id(1L).positionName("PR").build();
        ManagerEntity managerEntity = ManagerEntity.builder().baseUser(userEntity).position(positionEntity).build();

        AnnouncementEntity entity = AnnouncementEntity.builder()
                .title(title)
                .description("Announcement Description")
                .author(managerEntity)
                .date(new Date())
                .build();

        when(announcementRepo.findAnnouncementEntityByTitle(title)).thenReturn(entity);

        // Act
        Announcement result = announcementService.getAnnouncementByTitle(title);

        // Assert
        assertNotNull(result);
        assertEquals(title, result.getTitle());
        assertEquals("Announcement Description", result.getDescription());
        assertNotNull(result.getAuthor());
        assertEquals("John", result.getAuthor().getBaseUser().getFirstName());
        verify(announcementRepo, times(1)).findAnnouncementEntityByTitle(title);
    }


    @Test
    void getAnnouncementByTitle_ShouldReturnNull_WhenTitleIsInvalid() {
        String title = "Nonexistent Title";
        when(announcementRepo.findAnnouncementEntityByTitle(title)).thenReturn(null);

        Announcement result = announcementService.getAnnouncementByTitle(title);

        assertNull(result);
        verify(announcementRepo, times(1)).findAnnouncementEntityByTitle(title);
    }

    // getAllAnnouncements

    @Test
    void getAllAnnouncements_ShouldReturnListOfAnnouncements() {
        //Arrange
        AddressEntity address = AddressEntity.builder()
                .id(1L)
                .city("Test City")
                .country("Test Country")
                .street("Test Street")
                .number(123)
                .build();

        GenderEntity genderEntity = GenderEntity.builder()
                .id(1L)
                .genderName("MALE")
                .build();

        RoleEntity roleEntity = RoleEntity.builder().id(1L).roleName("MANAGER").build();

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .gender(genderEntity)
                .email("john.doe@example.com")
                .address(address)
                .roleId(roleEntity)
                .build();
        PositionEntity positionEntity = PositionEntity.builder().id(1L).positionName("PR").build();
        ManagerEntity managerEntity = ManagerEntity.builder().baseUser(userEntity).position(positionEntity).build();

        AnnouncementEntity entity1 = AnnouncementEntity.builder()
                .title("Announcement 1")
                .description("Description 1")
                .author(managerEntity)
                .date(new Date())
                .build();
        AnnouncementEntity entity2 = AnnouncementEntity.builder()
                .title("Announcement 2")
                .description("Description 2")
                .author(managerEntity)
                .date(new Date())
                .build();

        when(announcementRepo.findAll()).thenReturn(List.of(entity1, entity2));

        // Act
        List<Announcement> result = announcementService.getAllAnnouncements();

        // Assert
        assertEquals(2, result.size());
        verify(announcementRepo, times(1)).findAll();
    }


    @Test
    void getAllAnnouncements_ShouldReturnEmptyList_WhenNoAnnouncementsExist() {
        when(announcementRepo.findAll()).thenReturn(List.of());

        List<Announcement> result = announcementService.getAllAnnouncements();

        assertTrue(result.isEmpty());
        verify(announcementRepo, times(1)).findAll();
    }

    // createAnnouncement

    @Test
    void createAnnouncement_ShouldReturnCreatedAnnouncementResponse() {
        long authorId = 1L;
        CreateAnnouncementRequest request = CreateAnnouncementRequest.builder()
                .title("New Announcement")
                .description("This is a test announcement.")
                .authorId(authorId)
                .build();

        AddressEntity address = AddressEntity.builder()
                .id(1L)
                .city("Test City")
                .country("Test Country")
                .street("Test Street")
                .number(123)
                .build();

        GenderEntity genderEntity = GenderEntity.builder()
                .id(1L)
                .genderName("MALE")
                .build();

        RoleEntity roleEntity = RoleEntity.builder().id(1L).roleName("MANAGER").build();

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .gender(genderEntity)
                .email("john.doe@example.com")
                .address(address) // Set a non-null address
                .roleId(roleEntity)
                .build();
        PositionEntity positionEntity = PositionEntity.builder().id(1L).positionName("PR").build();
        ManagerEntity managerEntity = ManagerEntity.builder().baseUser(userEntity).position(positionEntity).build();
        when(managerService.getManagerById(authorId)).thenReturn(ManagerConverter.convertFromEntityToBase(managerEntity));

        AnnouncementEntity savedEntity = AnnouncementEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .author(managerEntity)
                .date(new Date())
                .build();
        when(announcementRepo.save(any(AnnouncementEntity.class))).thenReturn(savedEntity);

        // Act
        CreateAnnouncementResponse response = announcementService.createAnnouncement(request);

        // Assert
        assertEquals(request.getTitle(), response.getTitle());
        assertEquals(request.getDescription(), response.getDescription());

        verify(managerService, times(1)).getManagerById(authorId);
        verify(announcementRepo, times(1)).save(any(AnnouncementEntity.class));
    }

    @Test
    void createAnnouncement_ShouldThrowException_WhenAuthorIdIsInvalid() {
        long invalidAuthorId = -1L;
        CreateAnnouncementRequest request = CreateAnnouncementRequest.builder()
                .title("New Announcement")
                .description("This is a test announcement.")
                .authorId(invalidAuthorId)
                .build();

        assertThrows(IllegalArgumentException.class, () -> announcementService.createAnnouncement(request),
                "Expected IllegalArgumentException for invalid author ID");

        verify(announcementRepo, never()).save(any(AnnouncementEntity.class));
    }

    @Test
    void createAnnouncement_ShouldThrowException_WhenAuthorIdDoesNotExist() {
        long invalidAuthorId = 2L;
        CreateAnnouncementRequest request = CreateAnnouncementRequest.builder()
                .title("New Announcement")
                .description("This is a test announcement.")
                .authorId(invalidAuthorId)
                .build();

        when(managerService.getManagerById(invalidAuthorId)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> announcementService.createAnnouncement(request),
                "Author with given ID does not exist.");

        verify(announcementRepo, never()).save(any(AnnouncementEntity.class));
    }


    // updateAnnouncement

    @Test
    void updateAnnouncement_ShouldUpdateSuccessfully_WhenIdIsValid() {
        long id = 1L;
        UpdateAnnouncementRequest updateRequest = UpdateAnnouncementRequest.builder()
                .title("Updated Title")
                .description("Updated Description")
                .build();

        announcementService.updateAnnouncement(id, updateRequest, 1L);

        verify(announcementRepo, times(1)).updateAnnouncementEntity(id, updateRequest.getTitle(), updateRequest.getDescription());
    }

    // deleteAnnouncementById

    @Test
    void deleteAnnouncementById_ShouldDeleteSuccessfully_WhenIdIsValid() {
        long id = 1L;

        announcementService.deleteAnnouncementById(id);

        verify(announcementRepo, times(1)).deleteAnnouncementById(id);
    }
}
