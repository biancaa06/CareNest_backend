package nl.fontys.s3.carenestproject.persistance.repoInterfaces;

import nl.fontys.s3.carenestproject.persistance.entity.ManagerEntity;
import nl.fontys.s3.carenestproject.persistance.entity.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManagerRepo extends JpaRepository<ManagerEntity, Long> {
    ManagerEntity findManagerEntityById(long id);
    List<ManagerEntity> findManagerEntitiesByPosition(PositionEntity position);
}
