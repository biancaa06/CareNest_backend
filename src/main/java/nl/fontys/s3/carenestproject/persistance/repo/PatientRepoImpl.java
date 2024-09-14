package nl.fontys.s3.carenestproject.persistance.repo;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.carenestproject.service.repoInterfaces.PatientRepo;

@SuperBuilder
@Data
public class PatientRepoImpl extends UserRepoImpl implements PatientRepo {

}
