package nl.fontys.s3.carenestproject.persistance.entity;

import jakarta.persistence.*;
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
@Table(name = "Manager")
public class ManagerEntity {

    @Id
    private Long id;

    @MapsId
    @JoinColumn(name = "base_user_id")
    @OneToOne
    @NotNull
    private UserEntity baseUser;

    @JoinColumn(name = "position_id")
    @ManyToOne
    @NotNull
    private PositionEntity position;
}
