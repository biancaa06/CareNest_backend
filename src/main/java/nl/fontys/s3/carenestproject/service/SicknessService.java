package nl.fontys.s3.carenestproject.service;

import nl.fontys.s3.carenestproject.domain.classes.Sickness;
import nl.fontys.s3.carenestproject.service.request.CreateSicknessRequest;
import nl.fontys.s3.carenestproject.service.request.UpdateSicknessRequest;
import nl.fontys.s3.carenestproject.service.response.CreateSicknessResponse;
import nl.fontys.s3.carenestproject.service.response.UpdateSicknessResponse;

import java.util.List;

public interface SicknessService {
    Sickness getSicknessById(long id);
    List<Sickness> getAllSicknesses();
    void deleteSicknessById(long id);
    CreateSicknessResponse createSickness(CreateSicknessRequest request);
    UpdateSicknessResponse updateSickness(long id, UpdateSicknessRequest request);

}
