package nl.fontys.s3.carenestproject.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @OneToOne
    @MapsId
    @JoinColumn(name = "base_user_id")
    private UserEntity baseUser;

    @NotBlank
    @Column(name = "position")
    private String position;
}
