package nl.fontys.s3.carenestproject.domain.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.fontys.s3.carenestproject.domain.classes.users.CareTaker;
import nl.fontys.s3.carenestproject.domain.classes.users.Patient;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HireRequest {
    private Patient patient;
    private CareTaker careTaker;
    private Status status;
}
