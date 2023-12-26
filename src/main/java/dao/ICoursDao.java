package dao;

import dto.CoursDto;
import entity.Cours;
import entity.Etudiant;
import entity.Horaire;

import java.util.List;

public interface ICoursDao {
    Cours coursParId(Long id);

    void creerCours(Cours cours);

    List<Cours> lister();

    void supprimer(Long id);

    void modifier(Long id, Cours cours);

    List<CoursDto> chercher(String mot);

    List<Etudiant> tousLesEtudiants(Long idCours);

    void ajouterHoraire(Long idCours, Horaire horaire);

    void modifierHoraire(Long idHoraire, Horaire horaire);

    void supprimerHoraire(Long idCours, Long idHoraire);
}
