package nl.fontys.s3.carenestproject.service.mapping;

import nl.fontys.s3.carenestproject.domain.classes.Availability;
import nl.fontys.s3.carenestproject.persistance.entity.AvailabilityEntity;

public final class AvailabilityConverter {
    private AvailabilityConverter() {}

    public static Availability convertFromEntityToBase(AvailabilityEntity entity) {
        return Availability.valueOf(entity.getAvailabilityName());
    }
    public static AvailabilityEntity convertFromBaseToEntity(Availability base) {
        return AvailabilityEntity.builder()
                .id(base.getValue())
                .availabilityName(base.name())
                .build();
    }
}
