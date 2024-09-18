package nl.fontys.s3.carenestproject.service.mapping;

import nl.fontys.s3.carenestproject.domain.classes.Sickness;
import nl.fontys.s3.carenestproject.persistance.entity.SicknessEntity;

public final class SicknessConverter {
    private SicknessConverter() {}

    public static SicknessEntity convertFromBaseToEntity(Sickness sickness) {
        return SicknessEntity.builder()
                .id(sickness.getId())
                .name(sickness.getName())
                .build();
    }
    public static Sickness convertFromEntityToBase(SicknessEntity sicknessEntity) {
        return Sickness.builder()
                .id(sicknessEntity.getId())
                .name(sicknessEntity.getName())
                .build();
    }
}
