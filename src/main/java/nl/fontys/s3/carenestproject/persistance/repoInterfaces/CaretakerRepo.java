package nl.fontys.s3.carenestproject.persistance.repoInterfaces;

import nl.fontys.s3.carenestproject.persistance.entity.CaretakerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaretakerRepo extends JpaRepository<CaretakerEntity, Long> {
}
