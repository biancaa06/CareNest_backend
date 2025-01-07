package nl.fontys.s3.carenestproject.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    @ManyToOne
    @NotNull
    private UserEntity sender;

    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    @ManyToOne
    @NotNull
    private UserEntity receiver;

    @Column(nullable = false, columnDefinition = "TIMESTAMP(6)")
    private LocalDateTime date;

    @Column(nullable = false)
    private String text;
}
