package nl.fontys.s3.carenestproject.persistance.repoInterfaces;

import nl.fontys.s3.carenestproject.persistance.entity.CaretakerEntity;
import nl.fontys.s3.carenestproject.persistance.entity.SicknessEntity;
import nl.fontys.s3.carenestproject.persistance.entity.SicknessesForCaretaker;
import nl.fontys.s3.carenestproject.persistance.entity.compositeKeys.SicknessesForCaretaker_CPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SicknessOfCaretakerRepo extends JpaRepository<SicknessesForCaretaker, SicknessesForCaretaker_CPK> {

    @Query("SELECT sfc.sickness FROM SicknessesForCaretaker sfc WHERE sfc.caretaker = :caretaker")
    List<SicknessEntity> findSicknessesForCaretakersByCaretaker(@Param("caretaker") CaretakerEntity caretaker);

}
