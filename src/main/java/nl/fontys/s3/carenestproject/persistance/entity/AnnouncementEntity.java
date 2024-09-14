package nl.fontys.s3.carenestproject.persistance.entity;

import nl.fontys.s3.carenestproject.domain.classes.users.Manager;

import java.time.LocalDate;

public class AnnouncementEntity {
    private long id;
    private String title;
    private String description;
    private Manager author;
    private LocalDate date;
}
