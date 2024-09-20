package nl.fontys.s3.carenestproject.service.impl;

import nl.fontys.s3.carenestproject.service.request.CreateAnnouncementRequest;
import nl.fontys.s3.carenestproject.service.response.CreateAnnouncementResponse;
import nl.fontys.s3.carenestproject.persistance.repo.AnnouncementRepoImpl;
import nl.fontys.s3.carenestproject.persistance.repo.ManagerRepoImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnnouncementServiceImplTest {

    @Test
    void createAnnouncement_ShouldReturnCreatedAnnouncement() {
        AnnouncementServiceImpl announcementService = new AnnouncementServiceImpl(new AnnouncementRepoImpl(), new ManagerServiceImpl(new ManagerRepoImpl()));
        CreateAnnouncementRequest request = CreateAnnouncementRequest.builder()
                .authorId(1)
                .title("title")
                .description("desc").build();
        CreateAnnouncementResponse response = announcementService.createAnnouncement(request);

        assertNotNull(response.getAuthorEmail());
        assertEquals("title", response.getTitle());
    }
}