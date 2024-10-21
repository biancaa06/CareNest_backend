package nl.fontys.s3.carenestproject.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.carenestproject.persistance.entity.compositeKeys.SicknessesForCaretaker_CPK;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="SicknessesForCaretaker")
@IdClass(SicknessesForCaretaker_CPK.class)
public class SicknessesForCaretaker {
    @Id
    @JoinColumn(name="sickness_id")
    @ManyToOne
    @NotNull
    private SicknessEntity sickness;

    @Id
    @JoinColumn(name="caretaker_id")
    @ManyToOne
    @NotNull
    private CaretakerEntity caretaker;
}
