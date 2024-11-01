package nl.fontys.s3.carenestproject.service.mapping;

import nl.fontys.s3.carenestproject.domain.classes.Position;
import nl.fontys.s3.carenestproject.persistance.entity.PositionEntity;

public final class PositionConverter {
    private PositionConverter() {}

    public static Position convertFromEntityToBase(PositionEntity entity) {
        return Position.fromNumericValue(entity.getId());
    }

    public static PositionEntity convertFromBaseToEntity(Position position) {
        return PositionEntity.builder()
                .id(position.getValue())
                .positionName(position.name())
                .build();
    }
}
