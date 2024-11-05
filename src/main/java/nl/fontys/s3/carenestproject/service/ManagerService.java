package nl.fontys.s3.carenestproject.service;

import nl.fontys.s3.carenestproject.domain.classes.users.Manager;
import nl.fontys.s3.carenestproject.service.request.CreateManagerAccountRequest;

import java.util.List;

public interface ManagerService {
    Manager getManagerById(long id);
    void createManagerAccount(CreateManagerAccountRequest request);
    List<Manager> getManagersByPosition(long positionId);
}
