package nl.fontys.s3.carenestproject.domain.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Availability {
    FULL_TIME(1),
    PART_TIME(2);

    private final int value;

    public static Availability fromNumericValue(long value) {
        for (Availability availability : Availability.values()) {
            if (availability.getValue() == value) {
                return availability;
            }
        }
        throw new IllegalArgumentException("Invalid availability value: " + value);
    }
}
