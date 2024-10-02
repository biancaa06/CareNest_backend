package nl.fontys.s3.carenestproject.service.impl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.domain.classes.Announcement;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.AnnouncementRepo;
import nl.fontys.s3.carenestproject.service.request.CreateAnnouncementRequest;
import nl.fontys.s3.carenestproject.service.response.CreateAnnouncementResponse;
import nl.fontys.s3.carenestproject.persistance.entity.AnnouncementEntity;
import nl.fontys.s3.carenestproject.service.AnnouncementService;
import nl.fontys.s3.carenestproject.service.ManagerService;
import nl.fontys.s3.carenestproject.service.mapping.AnnouncementConverter;
import nl.fontys.s3.carenestproject.service.mapping.ManagerConverter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    /*@Override
    public List<Announcement> getAllAnnouncements() {
        return announcementRepo.getAllAnnouncements()
                .stream()
                .map(AnnouncementConverter :: convertFromEntityToBase)
                .toList();
    }*/

    /*@Override
    public CreateAnnouncementResponse createAnnouncement(CreateAnnouncementRequest request) {
        AnnouncementEntity announcementEntity = AnnouncementEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .author(ManagerConverter.convertFromBaseToEntity(managerService.getManagerById(request.getAuthorId())))
                .date(LocalDate.now()).build();

        AnnouncementEntity savedEntity = announcementRepo.createAnnouncement(announcementEntity);

        return CreateAnnouncementResponse.builder()
                .title(savedEntity.getTitle())
                .description(savedEntity.getDescription())
                .author(ManagerConverter.convertFromEntityToBase(savedEntity.getAuthor()))
                .date(savedEntity.getDate())
                .build();
    }*/

    /*@Override
    public Announcement updateAnnouncement(Announcement announcement) {
        AnnouncementEntity announcementEntity = AnnouncementConverter.convertFromBaseToEntity(announcement);

        AnnouncementEntity savedEntity = announcementRepo.updateAnnouncement(announcementEntity);

        return AnnouncementConverter.convertFromEntityToBase(savedEntity);
    }*/

    @Override
    public boolean deleteAnnouncement(long id) {
        return announcementRepo.deleteAnnouncementById(id);
    }
}
