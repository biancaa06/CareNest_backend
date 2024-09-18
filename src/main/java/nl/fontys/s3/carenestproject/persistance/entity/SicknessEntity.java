package nl.fontys.s3.carenestproject.persistance.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SicknessEntity {
    private long id;
    private String name;
}
