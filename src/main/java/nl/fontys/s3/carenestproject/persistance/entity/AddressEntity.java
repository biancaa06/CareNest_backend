package nl.fontys.s3.carenestproject.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@Table(name="Address")
@AllArgsConstructor
@NoArgsConstructor
public class AddressEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotBlank
    @Column(name="country")
    private String country;

    @NotBlank
    @Column(name="city")
    private String city;

    @NotBlank
    @Column(name="street")
    private String street;

    @NotBlank
    @Column(name="number")
    private int number;
}
