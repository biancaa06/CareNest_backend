package nl.fontys.s3.carenestproject.persistance.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.carenestproject.domain.classes.users.Manager;

import java.time.LocalDate;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementEntity {
    private long id;
    private String title;
    private String description;
    private ManagerEntity author;
    private LocalDate date;
}
