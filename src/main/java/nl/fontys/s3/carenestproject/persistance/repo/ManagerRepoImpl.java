package nl.fontys.s3.carenestproject.persistance.repo;

import nl.fontys.s3.carenestproject.persistance.entity.ManagerEntity;
import nl.fontys.s3.carenestproject.service.repoInterfaces.ManagerRepo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ManagerRepoImpl implements ManagerRepo {

    private List<ManagerEntity> managers;
    private UserRepoImpl userRepo;

    public ManagerRepoImpl() {
        managers = new ArrayList<>();
        userRepo = new UserRepoImpl();

        managers.add(ManagerEntity.builder()
                .baseUser(userRepo.getUserById(1))
                .position("PR").build());
        managers.add(ManagerEntity.builder()
                .baseUser(userRepo.getUserById(2))
                .position("MEDICAL").build());
    }


    @Override
    public ManagerEntity getManagerById(long id) {
        for (ManagerEntity manager : managers) {
            if(manager.getBaseUser().getId() == id) return manager;
        }
        return null;
    }

    @Override
    public ManagerEntity getManagerByEmail(String email) {
        for (ManagerEntity manager : managers) {
            if(manager.getBaseUser().getEmail().equals(email)) return manager;
        }
        return null;
    }

    @Override
    public boolean addManager(ManagerEntity manager) {
        managers.add(manager);
        return true;
    }

    @Override
    public ManagerEntity updateManager(ManagerEntity manager) {
        ManagerEntity oldManager = getManagerById(manager.getBaseUser().getId());
        oldManager.setBaseUser(manager.getBaseUser());
        oldManager.setPosition(manager.getPosition());

        return oldManager;
    }

    @Override
    public boolean deleteManagerById(long id) {
        return managers.removeIf(manager -> manager.getBaseUser().getId() == id);
    }

    @Override
    public int countManagers() {
        return managers.size();
    }
}
