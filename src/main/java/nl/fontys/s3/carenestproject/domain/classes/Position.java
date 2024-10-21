package nl.fontys.s3.carenestproject.domain.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Position {
    PR(1),
    MEDICAL(2);

    private final int value;

    public static Position fromNumericValue(long value) {
        for (Position position : Position.values()) {
            if (position.getValue() == value) {
                return position;
            }
        }
        throw new IllegalArgumentException("Invalid position value: " + value);
    }
}
