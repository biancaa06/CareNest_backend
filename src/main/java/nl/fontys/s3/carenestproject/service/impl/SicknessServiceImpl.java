package nl.fontys.s3.carenestproject.service.impl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.domain.classes.Sickness;
import nl.fontys.s3.carenestproject.persistance.entity.SicknessEntity;
import nl.fontys.s3.carenestproject.service.SicknessService;
import nl.fontys.s3.carenestproject.service.mapping.SicknessConverter;
import nl.fontys.s3.carenestproject.service.repoInterfaces.SicknessRepo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class SicknessServiceImpl implements SicknessService {

    private SicknessRepo sicknessRepo;

    @Override
    public Sickness getSicknessById(long id) {
        return SicknessConverter.convertFromEntityToBase(sicknessRepo.getSicknessById(id));
    }

    @Override
    public List<Sickness> getAllSicknesses() {
        List<SicknessEntity> sicknessEntities = sicknessRepo.getAllSicknesses();
        List<Sickness> sicknessList = new ArrayList<>();
        for (SicknessEntity sicknessEntity : sicknessEntities) {
            sicknessList.add(SicknessConverter.convertFromEntityToBase(sicknessEntity));
        }
        return sicknessList;
    }

    @Override
    public Sickness createSickness(Sickness sickness) {
        SicknessEntity sicknessEntity = sicknessRepo.createSickness(SicknessConverter.convertFromBaseToEntity(sickness));
        return SicknessConverter.convertFromEntityToBase(sicknessEntity);
    }

    @Override
    public void deleteSickness(Sickness sickness) {
        sicknessRepo.deleteSicknessById(sickness.getId());
    }
}
