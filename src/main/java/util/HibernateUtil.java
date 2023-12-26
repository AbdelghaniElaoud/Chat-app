package util;

import entity.Absence;
import entity.Cours;
import entity.Etudiant;
import entity.Horaire;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    @Getter
    private static SessionFactory sessionFactory;

    static {
        Configuration cfg = new Configuration().configure();
        cfg.addAnnotatedClass(Cours.class);
        cfg.addAnnotatedClass(Etudiant.class);
        cfg.addAnnotatedClass(Absence.class);
        cfg.addAnnotatedClass(Horaire.class);
        cfg.configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(cfg.getProperties());
        sessionFactory = cfg.buildSessionFactory(builder.build());
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}

