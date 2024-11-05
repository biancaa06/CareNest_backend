package nl.fontys.s3.carenestproject.persistance.repoInterfaces;

import nl.fontys.s3.carenestproject.persistance.entity.SicknessesOfPatient;
import nl.fontys.s3.carenestproject.persistance.entity.compositeKeys.SicknessesOfPatient_CPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SicknessOfPatientRepo extends JpaRepository<SicknessesOfPatient, SicknessesOfPatient_CPK> {
}
