package nl.fontys.s3.carenestproject.domain.classes;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Address {
    private long id;
    private String country;
    private String city;
    private String street;
    private int number;
}
