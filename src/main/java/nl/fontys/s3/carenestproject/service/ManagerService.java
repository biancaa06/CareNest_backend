package nl.fontys.s3.carenestproject.service;

import nl.fontys.s3.carenestproject.domain.classes.users.Manager;
import nl.fontys.s3.carenestproject.service.request.CreateManagerAccountRequest;

public interface ManagerService {
    Manager getManagerById(long id);
    void createManagerAccount(CreateManagerAccountRequest request);
}
