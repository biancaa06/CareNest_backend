package nl.fontys.s3.carenestproject.domain.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Data
public class CreateAnnouncementResponse {
    private String title;
    private String description;
    private String authorEmail;
    private LocalDate date;

}
