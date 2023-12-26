package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "etudiant")
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etudiant")
    private Long idEtudiant;
    @Column(name = "lastname", nullable = false)
    private String nom;
    @Column(name = "firstname", nullable = false)
    private String prenom;
    @Column(name = "dateNaissance")
    private LocalDate dateNaissance;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "cours_en_cours",
            joinColumns = @JoinColumn(name = "id_etudiant"),
            inverseJoinColumns = @JoinColumn(name = "id_cours")
    )
    private Set<Cours> cours = new HashSet<>();

    public Etudiant(String nom, String prenom, LocalDate dateNaissance, Set<Cours> cours) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.cours = cours;
    }

    public Etudiant(String nom, String prenom, LocalDate dateNaissance) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
    }

    @Override
    public String toString() {
        return "Etudiant{" +
                "idEtudiant=" + idEtudiant +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateNaissance=" + dateNaissance +
                '}';
    }

}
