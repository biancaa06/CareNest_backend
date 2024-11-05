package nl.fontys.s3.carenestproject.service.mapping;

import nl.fontys.s3.carenestproject.domain.classes.Sickness;
import nl.fontys.s3.carenestproject.domain.classes.users.Caretaker;
import nl.fontys.s3.carenestproject.persistance.entity.AvailabilityEntity;
import nl.fontys.s3.carenestproject.persistance.entity.CaretakerEntity;
import nl.fontys.s3.carenestproject.persistance.entity.SicknessEntity;

import java.util.ArrayList;
import java.util.List;

public final class CaretakerConverter {
    private CaretakerConverter() {}
    
    public static Caretaker convertFromEntityToBase(CaretakerEntity entity, List<SicknessEntity> sicknessEntities) {
        List<Sickness> sicknesses = new ArrayList<Sickness>();
        for(SicknessEntity sicknessEntity : sicknessEntities){
            sicknesses.add(SicknessConverter.convertFromEntityToBase(sicknessEntity));
        }
        return Caretaker.builder()
                .baseUser(BaseUserConverter.convertFromEntityToBase(entity.getBaseUser()))
                .availability(AvailabilityConverter.convertFromEntityToBase(entity.getAvailability()))
                .personalDescription(entity.getPersonalDescription())
                .salaryPerHour(entity.getSalaryPerHour())
                .specialisations(sicknesses)
                .build();
    }
    public static CaretakerEntity convertFromBaseToEntity(Caretaker entity) {
        return CaretakerEntity.builder()
                .baseUser(BaseUserConverter.convertFromBaseToEntity(entity.getBaseUser()))
                .availability(AvailabilityConverter.convertFromBaseToEntity(entity.getAvailability()))
                .personalDescription(entity.getPersonalDescription())
                .salaryPerHour(entity.getSalaryPerHour())
                .build();
    }
}
