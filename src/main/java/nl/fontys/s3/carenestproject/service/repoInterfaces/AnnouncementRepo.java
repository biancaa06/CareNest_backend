package nl.fontys.s3.carenestproject.service.repoInterfaces;

import nl.fontys.s3.carenestproject.domain.classes.Announcement;
import nl.fontys.s3.carenestproject.persistance.entity.AnnouncementEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepo {
    AnnouncementEntity getAnnouncementById(long id);

    AnnouncementEntity getAnnouncementByTitle(String title);

    List<AnnouncementEntity> getAnnouncements();

    AnnouncementEntity createAnnouncement(AnnouncementEntity announcement);

    AnnouncementEntity updateAnnouncement(AnnouncementEntity announcement);

    boolean deleteAnnouncementById(long id);
}
