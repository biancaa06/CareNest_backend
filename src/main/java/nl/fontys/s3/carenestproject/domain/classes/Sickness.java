package nl.fontys.s3.carenestproject.domain.classes;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Sickness {
    private long id;
    private String name;
}
