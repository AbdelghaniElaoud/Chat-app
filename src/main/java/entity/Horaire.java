package entity;

import enums.Days;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "horaires")
public class Horaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horaire")
    private Long idHoraire;
    @Column(name = "datedebut", nullable = false, columnDefinition = "varchar(8)")
    private LocalTime dateDebut;
    @Column(name = "datefin", nullable = false, columnDefinition = "varchar(8)")
    private LocalTime dateFin;

    @ManyToMany(mappedBy = "horaires", fetch = FetchType.LAZY)
    private Set<Cours> cours;

    private Days days;

    public Horaire(LocalTime dateDebut, LocalTime dateFin, Days days) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.days = days;

    }

    @Override
    public String toString() {
        return "Horaire{" +
                "idHoraire=" + idHoraire +
                ", dateDebut=" + dateDebut +
                '}';
    }
}
