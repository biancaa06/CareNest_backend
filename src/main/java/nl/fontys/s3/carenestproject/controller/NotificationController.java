package nl.fontys.s3.carenestproject.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.service.response.message.MessageResponse;
import nl.fontys.s3.carenestproject.service.response.notifications.AnnouncementNotification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendAnnouncementNotification(long authorId, String authorName, String announcementTitle) {
        AnnouncementNotification notification = new AnnouncementNotification(authorId, authorName, announcementTitle);
        messagingTemplate.convertAndSend("/topic/announcements", notification);
    }

    public void sendMessageNotification(MessageResponse response) {
        messagingTemplate.convertAndSendToUser(response.getReceiverId().toString(), "/queue/inboxmessages", response);
    }
}
