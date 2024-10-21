package nl.fontys.s3.carenestproject.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Caretaker")
public class CaretakerEntity {
    @Id
    private Long id;

    @MapsId
    @JoinColumn(name = "base_user_id")
    @OneToOne
    @NotNull
    private UserEntity baseUser;

    @Column(name="personal_description")
    @NotBlank
    private String personalDescription;

    @Column(name="salary")
    @NotNull
    private double salaryPerHour;

    @ManyToOne
    @JoinColumn(name="availability_id", referencedColumnName = "id")
    @NotNull
    private AvailabilityEntity availability;
}


