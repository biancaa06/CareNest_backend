package nl.fontys.s3.carenestproject.persistance.repoInterfaces;

import nl.fontys.s3.carenestproject.persistance.entity.AnnouncementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepo extends JpaRepository<AnnouncementEntity, Long> {
    AnnouncementEntity findAnnouncementEntityById(long id);

    AnnouncementEntity findAnnouncementEntityByTitle(String title);

    //List<AnnouncementEntity> getAllAnnouncements();

    //AnnouncementEntity createAnnouncement(AnnouncementEntity announcement);

    //AnnouncementEntity updateAnnouncement(AnnouncementEntity announcement);
    boolean deleteAnnouncementById(long id);
    //int countAnnouncements();
}
