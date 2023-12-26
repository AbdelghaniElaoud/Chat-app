package dao;

import Util.HibernateUtil;
import entity.Absence;
import entity.Cours;
import org.hibernate.Session;

public class AbsenceDao implements IAbsenceDao {
    public Absence chercherAbsence(Long idEtudiant, Long idCours) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Absence absence = (Absence) session.createQuery("FROM Absence a WHERE a.etudiant = :id_etudiant AND a.cours = :id_cours")
                    .setParameter("id_etudiant", idEtudiant)
                    .setParameter("id_cours", idCours)
                    .uniqueResult();
            session.getTransaction().commit();
            return absence;
        }
    }

    public void ajouterAbsence(Long idEtudiant, Long idCours) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Absence absence = new Absence(idEtudiant, idCours);
            session.save(absence);
            session.getTransaction().commit();
        }
    }

    public void modifierAbsence(Long idEtudiant, Long idCours, Long newIdCours) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Absence absence = chercherAbsence(idEtudiant, idCours);

            // Check if a Cours entity with newIdCours exists
            Cours newCours = session.get(Cours.class, newIdCours);
            if (newCours != null) {
                absence.setCours(newCours.getIdCours()

                );
                session.merge(absence);
            } else {
                System.out.println("Il n'y a pas de cours avec cet ID !!");
            }

            session.getTransaction().commit();
        }
    }

    public void supprimerAbsence(Long idEtudiant, Long idCours) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createQuery("DELETE FROM Absence a where a.etudiant = :id_etudiant AND a.cours = :id_cours")
                    .setParameter("id_etudiant", idEtudiant)
                    .setParameter("id_cours", idCours)
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }
}
