package nl.fontys.s3.carenestproject.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Position")
public class PositionEntity {

    @Id
    @Column(unique = true, nullable = false, name="id")
    private long id;

    @NotBlank
    @Column(name="position_name")
    private String positionName;
}
