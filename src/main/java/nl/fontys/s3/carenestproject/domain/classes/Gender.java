package nl.fontys.s3.carenestproject.domain.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
    MALE(1),
    FEMALE(2),
    OTHER(3);

    private final int value;
}
