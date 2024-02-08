package util;

import entities.*;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    @Getter
    private static SessionFactory sessionFactory;

    static {
        Configuration cfg = new Configuration().configure();
        cfg.addAnnotatedClass(User.class);
        cfg.addAnnotatedClass(Conversation.class);
        cfg.addAnnotatedClass(InvitationRequest.class);
        cfg.addAnnotatedClass(Message.class);
        cfg.addAnnotatedClass(UserConversation.class);
        cfg.addAnnotatedClass(UserInvitationRequest.class);
        cfg.addAnnotatedClass(User.class);
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

