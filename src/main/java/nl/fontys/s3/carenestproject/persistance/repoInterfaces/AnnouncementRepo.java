package nl.fontys.s3.carenestproject.persistance.repoInterfaces;

import jakarta.transaction.Transactional;
import nl.fontys.s3.carenestproject.persistance.entity.AnnouncementEntity;
import nl.fontys.s3.carenestproject.persistance.entity.ManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface AnnouncementRepo extends JpaRepository<AnnouncementEntity, Long> {
    AnnouncementEntity findAnnouncementEntityById(long id);

    AnnouncementEntity findAnnouncementEntityByTitle(String title);
    List<AnnouncementEntity> findAnnouncementEntitiesByAuthor(ManagerEntity author);
    @Modifying
    @Transactional
    @Query("UPDATE AnnouncementEntity a SET a.title = :title, a.description = :description, a.date= :lastUpdatedDate WHERE a.id = :id")
    void updateAnnouncementEntity(long id, String title, String description, Date lastUpdatedDate);

    void deleteAnnouncementById(long id);
}
