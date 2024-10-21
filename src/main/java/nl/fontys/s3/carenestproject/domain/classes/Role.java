package nl.fontys.s3.carenestproject.domain.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    MANAGER(1),
    PATIENT(2),
    CARETAKER(3);

    private final long value;
}
