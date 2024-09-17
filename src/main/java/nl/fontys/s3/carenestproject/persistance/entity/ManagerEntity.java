package nl.fontys.s3.carenestproject.persistance.entity;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class ManagerEntity{
    private UserEntity baseUser;
    private String position;
}
