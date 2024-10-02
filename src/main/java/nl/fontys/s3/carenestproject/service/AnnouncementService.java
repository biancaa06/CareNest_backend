package nl.fontys.s3.carenestproject.service;

import nl.fontys.s3.carenestproject.domain.classes.Announcement;
import nl.fontys.s3.carenestproject.service.request.CreateAnnouncementRequest;
import nl.fontys.s3.carenestproject.service.response.CreateAnnouncementResponse;

import java.util.List;

public interface AnnouncementService {
    public Announcement getAnnouncementById(long id);
    public Announcement getAnnouncementByTitle(String title);
    //public List<Announcement> getAllAnnouncements();
    //public CreateAnnouncementResponse createAnnouncement(CreateAnnouncementRequest request);
    //public Announcement updateAnnouncement(Announcement announcement);
    public boolean deleteAnnouncement(long id);
}
