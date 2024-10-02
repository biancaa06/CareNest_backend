package nl.fontys.s3.carenestproject.service.impl;

import lombok.Builder;
import nl.fontys.s3.carenestproject.domain.classes.users.Manager;
import nl.fontys.s3.carenestproject.service.ManagerService;
import nl.fontys.s3.carenestproject.service.mapping.ManagerConverter;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.ManagerRepo;
import org.springframework.stereotype.Service;

@Service
@Builder
public class ManagerServiceImpl implements ManagerService {

    private ManagerRepo managerRepo;

    @Override
    public Manager getManagerById(long id) {
        return ManagerConverter.convertFromEntityToBase(managerRepo.getManagerEntityById(id));
    }
}
