package nl.fontys.s3.carenestproject.service.impl;

import nl.fontys.s3.carenestproject.domain.classes.users.Manager;
import nl.fontys.s3.carenestproject.persistance.entity.ManagerEntity;
import nl.fontys.s3.carenestproject.persistance.repo.ManagerRepoImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagerServiceImplTest {

    @Test
    void getManagerById_ShouldReturnManager_notNull() {
        ManagerServiceImpl managerService = new ManagerServiceImpl(new ManagerRepoImpl());
        Manager manager = managerService.getManagerById(1);

        assertNotNull(manager);
    }
}
