package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cours")
public class Cours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cours")
    private Long idCours;
    @Column(nullable = false)
    private String nom;
    @Column(nullable = false)
    private int coefficient;
    @Column(nullable = false)
    private String sigle;
    @ManyToMany(mappedBy = "cours", fetch = FetchType.EAGER)
    private List<Etudiant> etudiants = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "Cours_Horaire",
            joinColumns = @JoinColumn(name = "id_cours"),
            inverseJoinColumns = @JoinColumn(name = "id_horaire")
    )
    private List<Horaire> horaires = new ArrayList<>();


    public Cours(String nom, int coefficient, String sigle, List<Etudiant> etudiants) {
        this.nom = nom;
        this.coefficient = coefficient;
        this.sigle = sigle;
        this.etudiants = etudiants;
    }

    public void ajouterEtudiant(Etudiant etudiant) {
        List<Etudiant> etudiants = this.getEtudiants();
        etudiants.add(etudiant);
        this.setEtudiants(etudiants);
    }

    public void addHoraire(Horaire horaire) {
        this.horaires.add(horaire);
    }

    public void removeHoraire(Horaire horaire) {
        this.horaires.remove(horaire);
    }

    public List<Horaire> getHoraires() {
        return horaires;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "idCours=" + idCours +
                ", nom='" + nom + '\'' +
                ", coefficient=" + coefficient +
                '}';
    }
}
