package dao;

import entity.Cours;
import entity.Etudiant;
import entity.Horaire;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IEtudiantDao {

    void creer(Etudiant e);

    List<Etudiant> lister();

    void supprimer(Long id);

    void modifier(Long id, Etudiant e);

    List<Etudiant> chercher(String par);

    Etudiant etudiantParId(Long id);

    void inscrire(Long idEtudiant, Long idCours);

    void supprimerCours(Long idEtudiant, Long idCours);

    Set<Cours> tousLesCours(Long idEtudiant);

    Map<String, List<Horaire>> emplois(Long idEtudiant);


}
