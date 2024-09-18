package nl.fontys.s3.carenestproject.service.repoInterfaces;

import nl.fontys.s3.carenestproject.persistance.entity.SicknessEntity;

import java.util.List;

public interface SicknessRepo {
    public SicknessEntity getSicknessById(long id);
    public List<SicknessEntity> getAllSicknesses();
    public SicknessEntity createSickness(SicknessEntity sickness);
    public void deleteSicknessById(long id);

}
