package dao;

import entity.Absence;

public interface IAbsenceDao {

    Absence chercherAbsence(Long idEtudiant, Long idCours);

    void ajouterAbsence(Long idEtudiant, Long idCours);

    void modifierAbsence(Long idEtudiant, Long idCours, Long newIdCours);

    void supprimerAbsence(Long idEtudiant, Long idCours);
}
