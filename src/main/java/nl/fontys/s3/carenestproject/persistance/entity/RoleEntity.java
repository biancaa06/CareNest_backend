package nl.fontys.s3.carenestproject.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Role")
public class RoleEntity {
    @Id
    @Column(name = "id")
    @NotBlank
    private long id;

    @Column(name = "role_name")
    @NotBlank
    private String roleName;
}
