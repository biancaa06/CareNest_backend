package nl.fontys.s3.carenestproject.persistance.repo;

import nl.fontys.s3.carenestproject.persistance.entity.SicknessEntity;
import nl.fontys.s3.carenestproject.service.repoInterfaces.SicknessRepo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class SicknessRepoImpl implements SicknessRepo {

    private List<SicknessEntity> sicknesses;
    private static long NEXT_ID = 4;

    public SicknessRepoImpl() {
        sicknesses = new ArrayList<>();
        sicknesses.add(SicknessEntity.builder()
                .id(1)
                .name("fever").build()
        );
        sicknesses.add(SicknessEntity.builder()
                .id(2)
                .name("heart condition").build()
        );
        sicknesses.add(SicknessEntity.builder()
                .id(3)
                .name("muscle pain").build()
        );
    }

    @Override
    public SicknessEntity getSicknessById(long id) {
        return sicknesses.stream().filter(sickness -> sickness.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<SicknessEntity> getAllSicknesses() {

        return Collections.unmodifiableList(sicknesses);
    }

    @Override
    public SicknessEntity createSickness(SicknessEntity sickness) {
        if(checkExistingSickness(sickness)) {
            sickness.setId(NEXT_ID);
            sicknesses.add(sickness);
            NEXT_ID++;
            return sickness;
        }
        return null;
    }
    private boolean checkExistingSickness(SicknessEntity sickness) {
        for (SicknessEntity sicknessEntity : sicknesses) {
            if(sicknessEntity.getName().equals(sickness.getName())) {return true;}
        }
        return false;
    }

    @Override
    public void deleteSicknessById(long id) {
        sicknesses.removeIf(sickness -> sickness.getId() == id);
    }
}
