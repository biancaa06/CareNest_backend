package nl.fontys.s3.carenestproject.persistance.repoInterfaces;

import nl.fontys.s3.carenestproject.persistance.entity.ManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepo extends JpaRepository<ManagerEntity, Long> {
    public ManagerEntity getManagerEntityById(long id);
}
