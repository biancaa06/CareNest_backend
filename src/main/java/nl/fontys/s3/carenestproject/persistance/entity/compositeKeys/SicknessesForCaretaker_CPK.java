package nl.fontys.s3.carenestproject.persistance.entity.compositeKeys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.carenestproject.persistance.entity.CaretakerEntity;
import nl.fontys.s3.carenestproject.persistance.entity.SicknessEntity;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SicknessesForCaretaker_CPK implements Serializable {
    private SicknessEntity sickness;
    private CaretakerEntity caretaker;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SicknessesForCaretaker_CPK that = (SicknessesForCaretaker_CPK) o;
        return Objects.equals(sickness, that.sickness) &&
                Objects.equals(caretaker, that.caretaker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sickness, caretaker);
    }
}
