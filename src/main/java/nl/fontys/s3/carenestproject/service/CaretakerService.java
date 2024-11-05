package nl.fontys.s3.carenestproject.service;

import nl.fontys.s3.carenestproject.domain.classes.users.Caretaker;
import nl.fontys.s3.carenestproject.service.request.CreateCaretakerAccountRequest;

import java.util.List;

public interface CaretakerService {
    void createCaretakerAccount(CreateCaretakerAccountRequest request);
    List<Caretaker> getCaretakers();
}
