package nl.fontys.s3.carenestproject.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Announcement")
public class AnnouncementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotBlank
    @Column(name="title")
    private String title;

    @NotBlank
    @Column(name="description")
    private String description;

    @NotNull
    @JoinColumn(name="author_id")
    @ManyToOne
    private ManagerEntity author;

    @NotNull
    @Column(name="date")
    private Date date;
}
