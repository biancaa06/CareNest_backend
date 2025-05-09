package nl.fontys.s3.carenestproject.service.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.carenestproject.domain.classes.users.Manager;

import java.time.LocalDate;
import java.util.Date;

@SuperBuilder
@Data
public class CreateAnnouncementResponse {
    private String title;
    private String description;
    private Manager author;
    private Date date;

}
