package nl.fontys.s3.carenestproject.domain.request;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.carenestproject.domain.classes.users.Manager;

import java.time.LocalDate;

@SuperBuilder
@Data
public class CreateAnnouncementRequest {
    private String title;
    private String description;
    private Manager author;
    private LocalDate date;
}
