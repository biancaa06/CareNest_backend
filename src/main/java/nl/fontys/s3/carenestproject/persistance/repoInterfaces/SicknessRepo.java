package nl.fontys.s3.carenestproject.persistance.repoInterfaces;

import nl.fontys.s3.carenestproject.persistance.entity.SicknessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SicknessRepo extends JpaRepository<SicknessEntity, Long> {
    public SicknessEntity findSicknessEntityById(long id);
    //public List<SicknessEntity> getAllSicknesses();
    //public SicknessEntity createSickness(SicknessEntity sickness);
    public void deleteSicknessEntityById(long id);
    //public SicknessEntity updateSickness(SicknessEntity sickness);

}
