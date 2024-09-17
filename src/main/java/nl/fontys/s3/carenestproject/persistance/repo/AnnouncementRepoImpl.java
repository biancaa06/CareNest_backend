package nl.fontys.s3.carenestproject.persistance.repo;

import nl.fontys.s3.carenestproject.persistance.entity.AnnouncementEntity;
import nl.fontys.s3.carenestproject.service.repoInterfaces.AnnouncementRepo;
import nl.fontys.s3.carenestproject.service.repoInterfaces.ManagerRepo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class AnnouncementRepoImpl implements AnnouncementRepo {

    private List<AnnouncementEntity> announcements;
    private final ManagerRepo managerRepo;
    private static long NEXT_ID = 2;

    public AnnouncementRepoImpl() {
        announcements = new ArrayList<>();
        managerRepo = new ManagerRepoImpl();
        announcements.add(AnnouncementEntity.builder()
                        .id(1)
                        .title("health")
                        .description("take care")
                        .author(managerRepo.getManagerById(1))
                .build()
        );
    }

    @Override
    public AnnouncementEntity getAnnouncementById(long id) {
        return this.announcements
                .stream()
                .filter(AnnouncementEntity -> AnnouncementEntity.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public AnnouncementEntity getAnnouncementByTitle(String title) {
        return this.announcements
                .stream()
                .filter(AnnouncementEntity -> AnnouncementEntity.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<AnnouncementEntity> getAnnouncements() {
        return Collections.unmodifiableList(announcements);
    }

    @Override
    public AnnouncementEntity createAnnouncement(AnnouncementEntity announcement) {
        announcement.setId(NEXT_ID++);
        this.announcements.add(announcement);
        return announcement;
    }

    @Override
    public AnnouncementEntity updateAnnouncement(AnnouncementEntity announcement) {
        AnnouncementEntity oldAnnouncement = this.getAnnouncementById(announcement.getId());
        oldAnnouncement.setTitle(announcement.getTitle());
        oldAnnouncement.setDescription(announcement.getDescription());
        return oldAnnouncement;
    }

    @Override
    public boolean deleteAnnouncementById(long id) {
        announcements.removeIf(announcement -> announcement.getId() == id);
        return true;
    }

    @Override
    public int countAnnouncements() {
        return this.announcements.size();
    }
}
