package nl.fontys.s3.carenestproject.domain.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.carenestproject.domain.classes.Announcement;

@SuperBuilder
@Data
public class CreateAnnouncementResponse {
    private Announcement announcement;
}
