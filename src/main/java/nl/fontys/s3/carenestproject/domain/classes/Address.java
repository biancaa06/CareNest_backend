package nl.fontys.s3.carenestproject.domain.classes;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Address {
    private long id;
    private String Country;
    private String City;
    private String Street;
    private int number;
}
