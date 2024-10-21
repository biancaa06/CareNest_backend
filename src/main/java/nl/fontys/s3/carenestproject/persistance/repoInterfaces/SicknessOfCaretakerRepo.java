package nl.fontys.s3.carenestproject.persistance.repoInterfaces;

import nl.fontys.s3.carenestproject.persistance.entity.SicknessesForCaretaker;
import nl.fontys.s3.carenestproject.persistance.entity.compositeKeys.SicknessesForCaretaker_CPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SicknessOfCaretakerRepo extends JpaRepository<SicknessesForCaretaker, SicknessesForCaretaker_CPK> {
}
