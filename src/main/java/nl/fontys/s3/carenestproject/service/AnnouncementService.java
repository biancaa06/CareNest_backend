package nl.fontys.s3.carenestproject.service;

import nl.fontys.s3.carenestproject.domain.classes.Announcement;
import nl.fontys.s3.carenestproject.service.request.CreateAnnouncementRequest;
import nl.fontys.s3.carenestproject.service.request.UpdateAnnouncementRequest;
import nl.fontys.s3.carenestproject.service.response.CreateAnnouncementResponse;

import java.util.List;

public interface AnnouncementService {
    Announcement getAnnouncementById(long id);
    Announcement getAnnouncementByTitle(String title);
    List<Announcement> getAnnouncementsByAuthor(long authorId);
    List<Announcement> getAllAnnouncements();
    CreateAnnouncementResponse createAnnouncement(CreateAnnouncementRequest request);
    void updateAnnouncement(long id, UpdateAnnouncementRequest announcement,long authenticatedUserId);
    void deleteAnnouncementById(long id);
}
