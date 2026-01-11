package com.mondial.ticket.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utilitaire Hibernate pour la gestion de la SessionFactory.
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Load configuration from hibernate.cfg.xml
            Configuration configuration = new Configuration().configure();

            // Only override URL if DB_HOST environment variable is set (for Docker)
            String dbHost = System.getenv("DB_HOST");
            if (dbHost != null) {
                String url = "jdbc:mariadb://" + dbHost + ":3306/mondial2030";
                configuration.setProperty("hibernate.connection.url", url);
            }
            // Otherwise, use the settings from hibernate.cfg.xml

            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}

