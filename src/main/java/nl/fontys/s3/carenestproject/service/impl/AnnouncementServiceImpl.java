package nl.fontys.s3.carenestproject.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.domain.classes.Announcement;
import nl.fontys.s3.carenestproject.domain.classes.users.Manager;
import nl.fontys.s3.carenestproject.persistance.entity.ManagerEntity;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.AnnouncementRepo;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.ManagerRepo;
import nl.fontys.s3.carenestproject.service.exception.ObjectNotFoundException;
import nl.fontys.s3.carenestproject.service.exception.UnauthorizedException;
import nl.fontys.s3.carenestproject.service.request.CreateAnnouncementRequest;
import nl.fontys.s3.carenestproject.service.request.UpdateAnnouncementRequest;
import nl.fontys.s3.carenestproject.service.response.CreateAnnouncementResponse;
import nl.fontys.s3.carenestproject.persistance.entity.AnnouncementEntity;
import nl.fontys.s3.carenestproject.service.AnnouncementService;
import nl.fontys.s3.carenestproject.service.ManagerService;
import nl.fontys.s3.carenestproject.service.mapping.AnnouncementConverter;
import nl.fontys.s3.carenestproject.service.mapping.ManagerConverter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepo announcementRepo;
    private final ManagerService managerService;
    private final ManagerRepo managerRepo;


    @Override
    public Announcement getAnnouncementById(long id) {
        AnnouncementEntity announcementEntity = announcementRepo.findAnnouncementEntityById(id);

        if (announcementEntity == null) {
            throw new ObjectNotFoundException("Announcement with ID " + id + " not found");
        }

        return Announcement.builder()
                .id(announcementEntity.getId())
                .title(announcementEntity.getTitle())
                .description(announcementEntity.getDescription())
                .author(ManagerConverter.convertFromEntityToBase(announcementEntity.getAuthor()))
                .date(announcementEntity.getDate())
                .build();
    }

    @Override
    public Announcement getAnnouncementByTitle(String title) {
        AnnouncementEntity announcementEntity = announcementRepo.findAnnouncementEntityByTitle(title);

        if (announcementEntity == null) {
            throw new ObjectNotFoundException("Announcement with title '" + title + "' not found");
        }

        return Announcement.builder()
                .id(announcementEntity.getId())
                .title(announcementEntity.getTitle())
                .description(announcementEntity.getDescription())
                .author(ManagerConverter.convertFromEntityToBase(announcementEntity.getAuthor()))
                .date(announcementEntity.getDate())
                .build();
    }

    @Override
    public List<Announcement> getAnnouncementsByAuthor(long authorId) {
        if(!managerRepo.existsById(authorId)) {
            throw new ObjectNotFoundException("Author with ID " + authorId + " not found");
        }
        ManagerEntity authorEntity = managerRepo.findManagerEntityById(authorId);
        List<AnnouncementEntity> announcementEntities = announcementRepo.findAnnouncementEntitiesByAuthor(authorEntity);
        List<Announcement> announcements = new ArrayList<>();
        for(AnnouncementEntity announcementEntity : announcementEntities) {
            announcements.add(AnnouncementConverter.convertFromEntityToBase(announcementEntity));
        }
        return announcements;
    }

    @Override
    public List<Announcement> getAllAnnouncements() {
        return announcementRepo.findAll()
                .stream()
                .map(AnnouncementConverter :: convertFromEntityToBase)
                .toList();
    }

    @Override
    @Transactional
    public CreateAnnouncementResponse createAnnouncement(CreateAnnouncementRequest request) {
        if (request.getAuthorId() <= 0) {
            throw new IllegalArgumentException("Author ID must be a positive number.");
        }

        Manager manager = managerService.getManagerById(request.getAuthorId());
        if (manager == null) {
            throw new IllegalArgumentException("Author with given ID does not exist.");
        }

        ManagerEntity author = ManagerConverter.convertFromBaseToEntity(manager);
        AnnouncementEntity announcementEntity = AnnouncementEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .author(author)
                .date(new Date())
                .build();


        AnnouncementEntity savedEntity = announcementRepo.save(announcementEntity);

        return CreateAnnouncementResponse.builder()
                .title(savedEntity.getTitle())
                .description(savedEntity.getDescription())
                .author(ManagerConverter.convertFromEntityToBase(savedEntity.getAuthor()))
                .date(savedEntity.getDate())
                .build();
    }

    @Override
    public void updateAnnouncement(long id, UpdateAnnouncementRequest announcement, long authenticatedUserId) {
        Announcement existingAnnouncement = getAnnouncementById(id);
        if (existingAnnouncement == null) {
            throw new ObjectNotFoundException("Announcement not found");
        }

        if (existingAnnouncement.getAuthor() == null ||
                existingAnnouncement.getAuthor().getBaseUser().getId() != authenticatedUserId) {
            throw new UnauthorizedException("User is not the author of this announcement");
        }
        announcementRepo.updateAnnouncementEntity(id, announcement.getTitle(), announcement.getDescription(), new Date());
    }

    @Transactional
    @Override
    public void deleteAnnouncementById(long id) {
        announcementRepo.deleteAnnouncementById(id);
    }
}
