    package nl.fontys.s3.carenestproject.service.impl;

    import jakarta.transaction.Transactional;
    import lombok.AllArgsConstructor;
    import nl.fontys.s3.carenestproject.domain.classes.Sickness;
    import nl.fontys.s3.carenestproject.persistance.entity.SicknessEntity;
    import nl.fontys.s3.carenestproject.service.SicknessService;
    import nl.fontys.s3.carenestproject.service.mapping.SicknessConverter;
    import nl.fontys.s3.carenestproject.persistance.repoInterfaces.SicknessRepo;
    import nl.fontys.s3.carenestproject.service.request.CreateSicknessRequest;
    import nl.fontys.s3.carenestproject.service.request.UpdateSicknessRequest;
    import nl.fontys.s3.carenestproject.service.response.CreateSicknessResponse;
    import nl.fontys.s3.carenestproject.service.response.UpdateSicknessResponse;
    import org.springframework.stereotype.Service;

    import java.util.ArrayList;
    import java.util.List;

    @Service
    @AllArgsConstructor
    public class SicknessServiceImpl implements SicknessService {

        private SicknessRepo sicknessRepo;

        @Override
        public Sickness getSicknessById(long id) {
            SicknessEntity sicknessEntity =sicknessRepo.findSicknessEntityById(id);
            if(sicknessEntity == null) return null;
            else return SicknessConverter.convertFromEntityToBase(sicknessEntity);
        }

        @Override
        public List<Sickness> getAllSicknesses() {
            List<SicknessEntity> sicknessEntities = sicknessRepo.findAll();
            List<Sickness> sicknessList = new ArrayList<>();
            for (SicknessEntity sicknessEntity : sicknessEntities) {
                sicknessList.add(SicknessConverter.convertFromEntityToBase(sicknessEntity));
            }
            return sicknessList;
        }

        @Override
        public CreateSicknessResponse createSickness(CreateSicknessRequest request) {

            if (request.getName() == null) {
                throw new IllegalArgumentException("Sickness name cannot be null");
            }

            SicknessEntity sicknessEntity = SicknessEntity.builder().name(request.getName()).build();
            sicknessEntity = sicknessRepo.save(sicknessEntity);
            return CreateSicknessResponse.builder().id(sicknessEntity.getId()).name(sicknessEntity.getName()).build();
        }

        @Override
        @Transactional
        public void deleteSicknessById(long id) {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid sickness input.");
            }
            sicknessRepo.deleteSicknessEntityById(id);
        }

        @Override
        public UpdateSicknessResponse updateSickness(long id, UpdateSicknessRequest request) {
            if(id <=0 || request.getNewSicknessName()==null){
                throw new IllegalArgumentException("Invalid sickness input.");
            }
            else if(checkExistingSickness(request.getNewSicknessName())){
                throw new IllegalArgumentException("Sickness already exists.");
            }

            SicknessEntity existingSicknessEntity = sicknessRepo.findSicknessEntityById(id);
            existingSicknessEntity.setName(request.getNewSicknessName());

            sicknessRepo.save(existingSicknessEntity);

            return UpdateSicknessResponse.builder()
                    .id(id)
                    .newName(request.getNewSicknessName())
                    .build();
        }

        private boolean checkExistingSickness(String name){
            List<Sickness> sicknesses = getAllSicknesses();
            for(Sickness s: sicknesses){
                if(s.getName().equals(name)){
                    return true;
                }
            }
            return false;
        }
    }
