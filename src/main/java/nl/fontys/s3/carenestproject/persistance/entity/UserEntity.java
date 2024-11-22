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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "User")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    @NotBlank
    private String firstName;

    @Column(name = "last_name")
    @NotBlank
    private String lastName;

    @Column(unique = true, name = "email")
    @NotBlank
    private String email;

    @JoinColumn(name="role_id", referencedColumnName = "id")
    @ManyToOne
    private RoleEntity roleId;

    @Column(name = "password")
    @NotBlank
    private String password;

    @Column(name = "phone_number")
    @NotBlank
    private String phoneNumber;

    @JoinColumn(name = "address_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AddressEntity address;

    @JoinColumn(name = "gender_id")
    @ManyToOne
    @NotNull
    private GenderEntity gender;

    @Lob
    @Column(name = "profile_image", columnDefinition = "BLOB")
    private byte[] profileImage;

    @Column(name="active")
    private boolean active;
}
