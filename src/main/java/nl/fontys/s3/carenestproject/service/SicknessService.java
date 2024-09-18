package nl.fontys.s3.carenestproject.service;

import nl.fontys.s3.carenestproject.domain.classes.Sickness;

import java.util.List;

public interface SicknessService {
    public Sickness getSicknessById(long id);
    public List<Sickness> getAllSicknesses();
    public Sickness createSickness(Sickness sickness);
    public void deleteSickness(Sickness sickness);
}
