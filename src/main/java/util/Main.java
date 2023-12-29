package util;

import dao.AbsenceDao;
import dao.CoursDao;
import dao.EtudiantDao;
import entity.Cours;
import entity.Horaire;
import enums.Days;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
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
//        Cours cours = new Cours("sport",99,"SPR-9999");
//        coursDao.creerCours(cours);
//        etudiantDao.inscrire(2L,7L);
//        etudiantDao.inscrire(2L,5L);
//        Horaire horaire = new Horaire(LocalTime.of(13, 30), LocalTime.of(15, 30), Days.TUESDAY);
//        coursDao.ajouterHoraire(7L,horaire);
//        etudiantDao.emplois(2L)
//                .forEach((key, value) -> System.out.println(key + " : " + value));
        Map<String, List<Horaire>> schedule = etudiantDao.emplois(2L);

        formatAndPrintSchedule(schedule);

        HibernateUtil.shutdown();
    }

    private static void formatAndPrintSchedule(Map<String, List<Horaire>> schedule) throws IOException {
        try (FileWriter myWriter = new FileWriter("Emplois.txt");){
            for (Map.Entry<String, List<Horaire>> entry : schedule.entrySet()) {

                myWriter.write(" Les horaires de " + entry.getKey()+" :" );


                List<Horaire> sortedHoraires = new ArrayList<>(entry.getValue());
                Collections.sort(sortedHoraires, Comparator.comparing(Horaire::getDays));

                for (Horaire horaire : sortedHoraires) {
                    myWriter.write(formatHoraire(horaire));
                }
                myWriter.write('\n');
            }
            System.out.println("L'emplois est pret!!!");
        }catch (IOException e){
            System.out.println("Il y a un probleme!!");
            e.printStackTrace();
        }
    }


    private static String formatHoraire(Horaire horaire) {
        return String.format(
                "   %s: %s - %s",
                horaire.getDays(), horaire.getDateDebut(), horaire.getDateFin()
        );
    }
}
