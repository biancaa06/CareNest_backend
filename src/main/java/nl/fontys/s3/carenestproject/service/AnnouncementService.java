package nl.fontys.s3.carenestproject.service;

import nl.fontys.s3.carenestproject.domain.classes.Address;
import nl.fontys.s3.carenestproject.domain.classes.Announcement;
import nl.fontys.s3.carenestproject.domain.request.CreateAccountRequest;
import nl.fontys.s3.carenestproject.domain.request.CreateAnnouncementRequest;
import nl.fontys.s3.carenestproject.domain.response.CreateAnnouncementResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface AnnouncementService {
    public Announcement getAnnouncementById(long id);
    public Announcement getAnnouncementByTitle(String title);
    public List<Announcement> getAllAnnouncements();
    public CreateAnnouncementResponse createAnnouncement(CreateAnnouncementRequest request);
    public Announcement updateAnnouncement(Announcement announcement);
    public boolean deleteAnnouncement(long id);
}
