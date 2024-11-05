package nl.fontys.s3.carenestproject.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Availability")
public class AvailabilityEntity {
    @Id
    @Column(name="id")
    @NotNull
    private long id;

    @Column(name="availability_name")
    @NotBlank
    private String availabilityName;
}
