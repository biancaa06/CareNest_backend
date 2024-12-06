package nl.fontys.s3.carenestproject.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.service.response.notifications.AnnouncementNotification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendAnnouncementNotification(String authorName, String announcementTitle) {
        AnnouncementNotification notification = new AnnouncementNotification(authorName, announcementTitle);
        messagingTemplate.convertAndSend("/topic/announcements", notification);
    }
}
