package nl.fontys.s3.carenestproject.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Builder
@Data
@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "reset_password_code")
public class ResetPasswordCode {

    @Id
    @Column(unique = true, nullable = false, name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name="reset_code")
    private Integer resetCode;

    @NotNull
    private Date expirationTime;

    @JoinColumn(referencedColumnName = "id", name = "user_id")
    @OneToOne
    @NotNull
    private UserEntity user;
}
