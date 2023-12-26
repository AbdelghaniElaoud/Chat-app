package dao;

import Util.HibernateUtil;
import dto.CoursDto;
import entity.Cours;
import entity.Etudiant;
import entity.Horaire;
import org.hibernate.Session;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Transactional
public class CoursDao implements ICoursDao {
    Session session;

    public CoursDao() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
    }

    @Override
    public Cours coursParId(Long id) {

//        session.getTransaction().begin();
//        Cours c=session.find(Cours.class,id);
//        session.getTransaction().commit();

        return session.find(Cours.class, id);

    }

    @Override
    public void creerCours(Cours cours) {
        session.getTransaction().begin();
        session.save(cours);
        session.getTransaction().commit();

    }

    @Override
    public List<Cours> lister() {
        session.beginTransaction();
        List<Cours> list = session.createQuery("from Cours ").list();
        session.getTransaction().commit();
        return list;
    }

    @Override
    public void supprimer(Long id) {
        session.beginTransaction();
        session.delete(session.get(Cours.class, id));
        session.getTransaction().commit();

    }

    @Override
    public void modifier(Long id, Cours cours) {
        try {
            session.getTransaction().begin();

            Cours cours1 = session.find(Cours.class, id);

            if (cours1 != null) {
                // Modify the properties of the student
                cours1.setNom(cours.getNom());
                cours1.setCoefficient(cours.getCoefficient());
                cours1.setSigle(cours.getSigle());


                // Use merge to update the entity
                session.merge(cours1);

                // Commit the transaction to persist the changes
                session.getTransaction().commit();

                System.out.println("Student updated successfully: " + cours1);
            } else {
                System.out.println("Student not found with ID: " + id);
            }
        } finally {
            session.close();
        }

    }

    @Override
    public List<CoursDto> chercher(String mot) {
        session.getTransaction().begin();
        List<Cours> list = session.createQuery("from Cours where nom like :mot")
                .setParameter("mot", "%" + mot + "%").list();
        List<Cours> list1 = session.createQuery("from Cours where sigle like :mot")
                .setParameter("mot", "%" + mot + "%").list();

        List<CoursDto> coursDtoList1 = new ArrayList<>();
        List<CoursDto> coursDtoList2 = new ArrayList<>();

        for (Cours cours : list) {
            CoursDto coursDto = CoursDto.fromCours(cours);
            coursDtoList1.add(coursDto);
        }
        for (Cours cours : list1) {
            CoursDto coursDto = CoursDto.fromCours(cours);
            coursDtoList2.add(coursDto);
        }

        List<CoursDto> concatenatedList = new ArrayList<>(coursDtoList1);
        concatenatedList.addAll(coursDtoList2);

        HashSet<CoursDto> setWithoutDuplicates = new HashSet<>(concatenatedList);
        List<CoursDto> listWithoutDuplicates = new ArrayList<>(setWithoutDuplicates);

        session.getTransaction().commit();


        return listWithoutDuplicates;
    }

    @Override
    public List<Etudiant> tousLesEtudiants(Long idCours) {
        session.getTransaction().begin();
        List<Etudiant> etudiants = session.createQuery("FROM Etudiant ").list();
        List<Etudiant> etudiants1 = new ArrayList<>();
        Cours cours = session.find(Cours.class, idCours);
        for (Etudiant e : etudiants) {
            if (e.getCours().contains(cours)) {
                etudiants1.add(e);
            }
        }
        session.getTransaction().commit();
        return etudiants1;
    }

    @Override
    public void ajouterHoraire(Long idCours, Horaire horaire) {
        session.beginTransaction();
        Cours cours = session.get(Cours.class, idCours);
        session.save(horaire);
        cours.addHoraire(horaire);
        session.getTransaction().commit();
    }

    @Override
    public void modifierHoraire(Long idHoraire, Horaire horaire) {
        session.beginTransaction();
        Horaire horaire1 = session.get(Horaire.class, idHoraire);
        if (horaire1 != null) {
            horaire1.setDateDebut(horaire.getDateDebut());
            horaire1.setDateFin(horaire.getDateFin());
            horaire1.setDays(horaire.getDays());
        }
        session.getTransaction().commit();
    }

    @Override
    public void supprimerHoraire(Long idCours, Long idHoraire) {
        session.beginTransaction();
        Cours cours = session.get(Cours.class, idCours);
        Horaire horaire = session.get(Horaire.class, idHoraire);
        cours.removeHoraire(horaire);
        session.remove(horaire);

        session.getTransaction().commit();
    }
}
