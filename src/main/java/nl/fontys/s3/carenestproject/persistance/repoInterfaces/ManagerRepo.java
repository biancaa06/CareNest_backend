package nl.fontys.s3.carenestproject.persistance.repoInterfaces;

import nl.fontys.s3.carenestproject.persistance.entity.ManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepo extends JpaRepository<ManagerEntity, Long> {
    public ManagerEntity getManagerEntityById(long id);
    //public ManagerEntity getManagerEntityByEmail(String email);
    //public ManagerEntity addManager(ManagerEntity manager);
    //public ManagerEntity updateManager(ManagerEntity manager);
    //public boolean deleteManagerById(long id);
    //public int countManagers();
}
