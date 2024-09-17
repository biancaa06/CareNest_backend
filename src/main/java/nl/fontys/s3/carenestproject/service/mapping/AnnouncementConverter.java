package nl.fontys.s3.carenestproject.service.mapping;

import nl.fontys.s3.carenestproject.domain.classes.Announcement;
import nl.fontys.s3.carenestproject.persistance.entity.AnnouncementEntity;

public final class AnnouncementConverter {
    private AnnouncementConverter() {}

    public static Announcement convertFromEntityToBase(AnnouncementEntity announcementEntity) {
        return Announcement.builder()
                .id(announcementEntity.getId())
                .title(announcementEntity.getTitle())
                .description(announcementEntity.getDescription())
                .author(ManagerConverter.convertFromEntityToBase(announcementEntity.getAuthor()))
                .date(announcementEntity.getDate())
                .build();
    }

    public static AnnouncementEntity convertFromBaseToEntity(Announcement announcement) {
        return AnnouncementEntity.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .description(announcement.getDescription())
                .date(announcement.getDate())
                .author(ManagerConverter.convertFromBaseToEntity(announcement.getAuthor())).build();
    }
}
