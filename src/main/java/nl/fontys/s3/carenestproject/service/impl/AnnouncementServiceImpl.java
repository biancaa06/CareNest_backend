package nl.fontys.s3.carenestproject.service.impl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.domain.classes.Announcement;
import nl.fontys.s3.carenestproject.domain.request.CreateAnnouncementRequest;
import nl.fontys.s3.carenestproject.domain.response.CreateAnnouncementResponse;
import nl.fontys.s3.carenestproject.persistance.entity.AnnouncementEntity;
import nl.fontys.s3.carenestproject.service.AnnouncementService;
import nl.fontys.s3.carenestproject.service.impl.mapping.AnnouncementConverter;
import nl.fontys.s3.carenestproject.service.repoInterfaces.AnnouncementRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepo announcementRepo;


    @Override
    public Announcement getAnnouncementById(long id) {
        AnnouncementEntity announcementEntity = announcementRepo.getAnnouncementById(id);

        if(announcementEntity != null) {
            return Announcement.builder()
                    .id(announcementEntity.getId())
                    .title(announcementEntity.getTitle())
                    .description(announcementEntity.getDescription())
                    .author(announcementEntity.getAuthor())
                    .date(announcementEntity.getDate())
                    .build();
        }

        return null;
    }

    @Override
    public Announcement getAnnouncementByTitle(String title) {
        AnnouncementEntity announcementEntity = announcementRepo.getAnnouncementByTitle(title);

        if(announcementEntity != null) {
            return Announcement.builder()
                    .id(announcementEntity.getId())
                    .title(announcementEntity.getTitle())
                    .description(announcementEntity.getDescription())
                    .author(announcementEntity.getAuthor())
                    .date(announcementEntity.getDate())
                    .build();
        }

        return null;
    }

    @Override
    public List<Announcement> getAllAnnouncements() {
        return announcementRepo.getAnnouncements()
                .stream()
                .map(AnnouncementConverter :: convertFromEntityToBase)
                .toList();
    }

    @Override
    public CreateAnnouncementResponse createAnnouncement(CreateAnnouncementRequest request) {
        AnnouncementEntity announcementEntity = AnnouncementEntity.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .date(request.getDate()).build();

        AnnouncementEntity savedEntity = announcementRepo.createAnnouncement(announcementEntity);
        Announcement announcement = AnnouncementConverter.convertFromEntityToBase(savedEntity);
        return CreateAnnouncementResponse.builder().announcement(announcement).build();
    }

    @Override
    public Announcement updateAnnouncement(Announcement announcement) {
        AnnouncementEntity announcementEntity = AnnouncementConverter.convertFromBaseToEntity(announcement);

        AnnouncementEntity savedEntity = announcementRepo.updateAnnouncement(announcementEntity);

        return AnnouncementConverter.convertFromEntityToBase(savedEntity);
    }

    @Override
    public boolean deleteAnnouncement(long id) {
        return announcementRepo.deleteAnnouncementById(id);
    }
}
