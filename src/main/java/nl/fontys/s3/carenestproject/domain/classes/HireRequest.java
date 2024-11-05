package nl.fontys.s3.carenestproject.domain.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.fontys.s3.carenestproject.domain.classes.users.Caretaker;
import nl.fontys.s3.carenestproject.domain.classes.users.Patient;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HireRequest {
    private long id;
    private Patient patient;
    private Caretaker careTaker;
    private Status status;
}
