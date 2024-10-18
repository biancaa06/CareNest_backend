package nl.fontys.s3.carenestproject.domain.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Position {
    PR(1),
    MEDICAL(2);

    private final int value;
}
