package Util;

import dao.AbsenceDao;
import dao.CoursDao;
import dao.EtudiantDao;
import entity.Horaire;
import enums.Days;

import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
//        EtudiantDao etudiantDao = new EtudiantDao();
//        Etudiant etudiant = new Etudiant("ELAOUD","Abdlghani", LocalDate.parse("2002-11-11"));
//        etudiantDao.creer(etudiant);
//        etudiantDao.lister().forEach(System.out::println);
//        etudiantDao.supprimer(1L);
//        etudiantDao.modifier(2L,etudiant);
//        Etudiant etudiant = new Etudiant("ELAOUD","Abdelghani", LocalDate.parse("2002-12-11"));
//        etudiantDao.chercher("aou").forEach(System.out::println);
//        etudiantDao.etudiantParId(2L);
//        Cours cours = new Cours("Amazon Cloud Service",5,"AWS-1100");
//        coursDao.creerCours(cours);
//        coursDao.lister().forEach(c ->System.out.println(c));
//        coursDao.supprimer(3L);
//        Cours cours = new Cours("Amazon Cloud Service",5,"AS-1100");
//        coursDao.creerCours(cours);
//        Cours cours = new Cours("Amazon Cloud Service",5,"AWS-1100");
//        coursDao.modifier(4L,cours);
//        coursDao.chercher("AWS").forEach(System.out::println);
//        Cours cours = new Cours("Base de donnees",4,"BSD-3000",null);
//        coursDao.creerCours(cours);
//        coursDao.tousLesEtudiants(1L).forEach(System.out::println);
//        EtudiantDao etudiantDao = new EtudiantDao();
//        CoursDao coursDao = new CoursDao();
//        Etudiant etudiant = new Etudiant("ELAOUD","ahmed", LocalDate.now());
//        etudiantDao.modifier(2L,etudiant);
//        etudiantDao.inscrire(2L, 3L);
        AbsenceDao absenceDao = new AbsenceDao();
        EtudiantDao etudiantDao = new EtudiantDao();
        CoursDao coursDao = new CoursDao();
        Horaire horaire = new Horaire(LocalTime.of(11, 45), LocalTime.of(12, 30), Days.FRIDAY);
//        coursDao.ajouterHoraire(1L,horaire);
        etudiantDao.emplois(2L)
                .forEach((key, value) -> System.out.println(key + " : " + value));

        HibernateUtil.shutdown();
    }
}
