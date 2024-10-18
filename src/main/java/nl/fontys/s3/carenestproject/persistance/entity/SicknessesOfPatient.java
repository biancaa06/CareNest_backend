package nl.fontys.s3.carenestproject.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.carenestproject.persistance.entity.compositeKeys.SicknessesOfPatient_CPK;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="SicknessesOfPatient")
@IdClass(SicknessesOfPatient_CPK.class)
public class SicknessesOfPatient {

    @Id
    @JoinColumn(name="sickness_id")
    @ManyToOne
    @NotNull
    private SicknessEntity sicknessId;

    @Id
    @JoinColumn(name="patient_id")
    @ManyToOne
    @NotNull
    private PatientEntity patientId;
}
