package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "absence")
public class Absence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_absence")
    private Long idAbsence;

    @Column(name = "id_etudiant", nullable = false)
    private Long etudiant;

    @Column(name = "id_cours", nullable = false)
    private Long cours;

    public Absence(Long etudiant, Long cours) {
        this.etudiant = etudiant;
        this.cours = cours;
    }
}
