package nl.fontys.s3.carenestproject.persistance.entity;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.carenestproject.domain.classes.Position;

@SuperBuilder
@Data
public class ManagerEntity extends UserEntity {
    private String position;
}
