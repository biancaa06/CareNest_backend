package nl.fontys.s3.carenestproject.persistance.repoInterfaces;

import nl.fontys.s3.carenestproject.persistance.entity.SicknessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SicknessRepo extends JpaRepository<SicknessEntity, Long> {
    SicknessEntity findSicknessEntityById(long id);
    void deleteSicknessEntityById(long id);
}
