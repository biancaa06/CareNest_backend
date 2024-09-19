package nl.fontys.s3.carenestproject.service;

import nl.fontys.s3.carenestproject.domain.classes.Sickness;
import nl.fontys.s3.carenestproject.service.request.CreateSicknessRequest;
import nl.fontys.s3.carenestproject.service.response.CreateSicknessResponse;

import java.util.List;

public interface SicknessService {
    public Sickness getSicknessById(long id);
    public List<Sickness> getAllSicknesses();
    public CreateSicknessResponse createSickness(CreateSicknessRequest request);
    public void deleteSickness(Sickness sickness);
}
