package nl.fontys.s3.carenestproject.service.impl.mapping;

import nl.fontys.s3.carenestproject.domain.classes.Announcement;
import nl.fontys.s3.carenestproject.persistance.entity.AnnouncementEntity;

public final class AnnouncementConverter {
    private AnnouncementConverter() {}

    public static Announcement convertFromEntityToBase(AnnouncementEntity announcementEntity) {
        return Announcement.builder()
                .id(announcementEntity.getId())
                .title(announcementEntity.getTitle())
                .description(announcementEntity.getDescription())
                .author(announcementEntity.getAuthor())
                .date(announcementEntity.getDate())
                .build();
    }

    public static AnnouncementEntity convertFromBaseToEntity(Announcement announcement) {
        AnnouncementEntity announcementEntity = new AnnouncementEntity();
        announcementEntity.setId(announcement.getId());
        announcementEntity.setTitle(announcement.getTitle());
        announcementEntity.setDescription(announcement.getDescription());
        announcementEntity.setAuthor(announcement.getAuthor());
        announcementEntity.setDate(announcement.getDate());
        return announcementEntity;
    }
}
