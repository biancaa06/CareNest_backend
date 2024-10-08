package nl.fontys.s3.carenestproject.domain.classes;

import lombok.*;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.carenestproject.domain.classes.users.Manager;

import java.time.LocalDate;
import java.util.Date;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Announcement {
    private long id;
    private String title;
    private String description;
    private Manager author;
    private Date date;
}
