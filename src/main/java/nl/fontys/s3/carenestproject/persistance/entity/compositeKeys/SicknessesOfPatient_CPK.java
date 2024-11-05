package nl.fontys.s3.carenestproject.persistance.entity.compositeKeys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.carenestproject.persistance.entity.PatientEntity;
import nl.fontys.s3.carenestproject.persistance.entity.SicknessEntity;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SicknessesOfPatient_CPK implements Serializable {

    private SicknessEntity sicknessId;
    private PatientEntity patientId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SicknessesOfPatient_CPK that = (SicknessesOfPatient_CPK) o;
        return Objects.equals(sicknessId, that.sicknessId) &&
                Objects.equals(patientId, that.patientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sicknessId, patientId);
    }
}
