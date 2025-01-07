package nl.fontys.s3.carenestproject.service.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateAnnouncementRequest {
    private String title;
    private String description;
    private long authorId;
}
