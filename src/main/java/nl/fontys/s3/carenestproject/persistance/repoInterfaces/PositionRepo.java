package nl.fontys.s3.carenestproject.persistance.repoInterfaces;

import nl.fontys.s3.carenestproject.persistance.entity.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepo extends JpaRepository<PositionEntity, Long> {
}
