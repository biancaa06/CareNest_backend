package nl.fontys.s3.carenestproject.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.domain.classes.Announcement;
import nl.fontys.s3.carenestproject.domain.classes.users.Manager;
import nl.fontys.s3.carenestproject.persistance.entity.ManagerEntity;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.AnnouncementRepo;
import nl.fontys.s3.carenestproject.service.request.CreateAnnouncementRequest;
import nl.fontys.s3.carenestproject.service.request.UpdateAnnouncementRequest;
import nl.fontys.s3.carenestproject.service.response.CreateAnnouncementResponse;
import nl.fontys.s3.carenestproject.persistance.entity.AnnouncementEntity;
import nl.fontys.s3.carenestproject.service.AnnouncementService;
import nl.fontys.s3.carenestproject.service.ManagerService;
import nl.fontys.s3.carenestproject.service.mapping.AnnouncementConverter;
import nl.fontys.s3.carenestproject.service.mapping.ManagerConverter;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepo announcementRepo;
    private final ManagerService managerService;


    @Override
    public Announcement getAnnouncementById(long id) {
        AnnouncementEntity announcementEntity = announcementRepo.findAnnouncementEntityById(id);

        if(announcementEntity != null) {
            return Announcement.builder()
                    .id(announcementEntity.getId())
                    .title(announcementEntity.getTitle())
                    .description(announcementEntity.getDescription())
                    .author(ManagerConverter.convertFromEntityToBase(announcementEntity.getAuthor()))
                    .date(announcementEntity.getDate())
                    .build();
        }

        return null;
    }

    @Override
    public Announcement getAnnouncementByTitle(String title) {
        AnnouncementEntity announcementEntity = announcementRepo.findAnnouncementEntityByTitle(title);

        if(announcementEntity != null) {
            return Announcement.builder()
                    .id(announcementEntity.getId())
                    .title(announcementEntity.getTitle())
                    .description(announcementEntity.getDescription())
                    .author(ManagerConverter.convertFromEntityToBase(announcementEntity.getAuthor()))
                    .date(announcementEntity.getDate())
                    .build();
        }

        return null;
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
    public void updateAnnouncement(long id, UpdateAnnouncementRequest announcement) {
        announcementRepo.updateAnnouncementEntity(id, announcement.getTitle(), announcement.getDescription()    );
    }

    @Override
    public void deleteAnnouncementById(long id) {
        announcementRepo.deleteAnnouncementById(id);
    }
}
