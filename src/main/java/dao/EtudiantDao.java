package dao;

import util.HibernateUtil;
import entity.Cours;
import entity.Etudiant;
import entity.Horaire;
import org.hibernate.Session;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
public class EtudiantDao implements IEtudiantDao {

    Session session;

    public EtudiantDao() {
        // TODO Auto-generated constructor stub
        session = HibernateUtil.getSessionFactory().getCurrentSession();
    }


    public void creer(Etudiant e) {
        // TODO Auto-generated method stub
        session.beginTransaction();
        session.save(e);
        session.getTransaction().commit();

    }

    public List<Etudiant> lister() {
        // TODO Auto-generated method stub
        session.beginTransaction();
        List<Etudiant> list = session.createQuery("from Etudiant").list();
        session.getTransaction().commit();

        return list;
    }

    public void supprimer(Long id) {
        // TODO Auto-generated method stub
        session.beginTransaction();
        session.delete(session.get(Etudiant.class, id));
        session.getTransaction().commit();
    }

    public void modifier(Long id, Etudiant e) {
        // TODO Auto-generated method stub
//        if (session.isOpen()){
//			session.getTransaction().commit();
//		}
        try {
            session.getTransaction().begin();

            Etudiant etudiant = session.find(Etudiant.class, id);

            if (etudiant != null) {
                // Modify the properties of the student
                etudiant.setNom(e.getNom());
                etudiant.setPrenom(e.getPrenom());
                etudiant.setDateNaissance(e.getDateNaissance());

                // Use merge to update the entity
                session.merge(etudiant);

                // Commit the transaction to persist the changes
                session.getTransaction().commit();

                System.out.println("Student updated successfully: " + etudiant);
            } else {
                System.out.println("Student not found with ID: " + id);
            }
        } finally {
            session.close();
        }
    }

    public List<Etudiant> chercher(String par) {
        // TODO Auto-generated method stub
        session.getTransaction().begin();
        List<Etudiant> list = session.createQuery("from Etudiant where nom like :par")
                .setParameter("par", "%" + par + "%").list();
        session.getTransaction().commit();


        return list;
    }

    public Etudiant etudiantParId(Long id) {
        // TODO Auto-generated method stub
//		session.getTransaction().begin();
//		Etudiant e=session.find(Etudiant.class,id);
//		session.getTransaction().commit();
//		session.close();

        return session.find(Etudiant.class, id);

    }

    @Override
    public void inscrire(Long idEtudiant, Long idCours) {
        try {
            session.getTransaction().begin();

            EtudiantDao etudiantDao = new EtudiantDao();
            Etudiant etudiant = etudiantDao.etudiantParId(idEtudiant);

            CoursDao coursDao = new CoursDao();
            Cours cours = coursDao.coursParId(idCours);

            cours.ajouterEtudiant(etudiant);
            session.merge(cours);

            Set<Cours> cours1 = etudiant.getCours();
            cours1.add(cours);
//            Etudiant etudiant1 = new Etudiant(etudiant.getNom(), etudiant.getPrenom(), etudiant.getDateNaissance(), cours1);
//            etudiantDao.modifier(etudiant.getIdEtudiant(), etudiant1);
            etudiant.setCours(cours1);
            session.save(etudiant);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();

            e.printStackTrace();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void supprimerCours(Long idEtudiant, Long idCours) {
        session.getTransaction().begin();
        EtudiantDao etudiantDao = new EtudiantDao();
        Etudiant etudiant = etudiantDao.etudiantParId(idEtudiant);
        Set<Cours> cours = etudiant.getCours();
        if (session.find(Etudiant.class, idEtudiant) != null) {
            cours.remove(session.find(Cours.class, idCours));
        }
        etudiant.setCours(cours);
        session.merge(etudiant);
        session.getTransaction().commit();
    }

    @Override
    public Set<Cours> tousLesCours(Long idEtudiant) {
        session.getTransaction().begin();
        Set<Cours> cours = session.find(Etudiant.class, idEtudiant).getCours();
        session.getTransaction().commit();
        return cours;
    }

    @Override
    @Transactional
    public Map<String, List<Horaire>> emplois(Long idEtudiant) {
        session.beginTransaction();
		Set<Cours> cours = session.get(Etudiant.class, idEtudiant).getCours();
        return cours.stream().collect(Collectors.toMap(Cours::getNom, Cours::getHoraires));

        /*List<Horaire> emplois = new ArrayList<>();
		for(Cours c : cours){
			if (!c.getHoraires().isEmpty()){
				emplois.addAll(c.getHoraires());
			}
		}
        session.merge(cours);

        String hql = "SELECT DISTINCT h FROM Horaire h " +
                "JOIN h.cours c " +
                "JOIN c.etudiants e " +
                "WHERE e.idEtudiant = :etudiantId";

        List<Horaire> horaires = session.createQuery(hql, Horaire.class)
                .setParameter("etudiantId", idEtudiant)
                .getResultList();

        session.getTransaction().commit();
        return horaires;*/
    }


}
