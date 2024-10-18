package nl.fontys.s3.carenestproject.domain.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Availability {
    PART_TIME(1),
    FULL_TIME(2);

    private final int value;
}
