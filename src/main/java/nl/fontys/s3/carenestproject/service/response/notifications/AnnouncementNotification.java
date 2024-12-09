package nl.fontys.s3.carenestproject.service.response.notifications;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnnouncementNotification {
    private String authorName;
    private String announcementTitle;
}
