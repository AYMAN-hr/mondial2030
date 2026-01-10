package com.mondial.ticket.dao;

import com.mondial.ticket.model.User;
import com.mondial.ticket.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * DAO pour la gestion des utilisateurs dans la base de données.
 */
public class UserDao {

    /**
     * Créer un nouvel utilisateur
     */
    public boolean create(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            System.out.println("Hibernate: Utilisateur créé -> " + user.getUsername());
            return true;
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.err.println("Erreur Hibernate (création utilisateur) : " + e.getMessage());
            return false;
        }
    }

    /**
     * Trouver un utilisateur par son username
     */
    public User findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User where username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
        } catch (Exception e) {
            System.err.println("Erreur Hibernate (recherche utilisateur) : " + e.getMessage());
            return null;
        }
    }

    /**
     * Vérifier si un username existe déjà
     */
    public boolean usernameExists(String username) {
        return findByUsername(username) != null;
    }

    /**
     * Récupérer tous les utilisateurs
     */
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        } catch (Exception e) {
            System.err.println("Erreur Hibernate (liste utilisateurs) : " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Mettre à jour un utilisateur
     */
    public boolean update(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            System.out.println("Hibernate: Utilisateur mis à jour -> " + user.getUsername());
            return true;
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.err.println("Erreur Hibernate (maj utilisateur) : " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprimer un utilisateur
     */
    public boolean delete(String username) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = findByUsername(username);
            if (user != null) {
                session.remove(session.merge(user));
                transaction.commit();
                System.out.println("Hibernate: Utilisateur supprimé -> " + username);
                return true;
            }
            transaction.rollback();
            return false;
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.err.println("Erreur Hibernate (suppression utilisateur) : " + e.getMessage());
            return false;
        }
    }

    /**
     * Authentifier un utilisateur
     */
    public User authenticate(String username, String password) {
        User user = findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    /**
     * Initialiser les utilisateurs par défaut si la table est vide
     */
    public void initializeDefaultUsers() {
        if (findAll().isEmpty()) {
            System.out.println("Initialisation des utilisateurs par défaut...");
            create(new User("admin", "admin123", "ADMIN", "Administrateur", "admin@mondial2030.com"));
            create(new User("user", "user123", "USER", "Utilisateur", "user@mondial2030.com"));
            System.out.println("✅ Utilisateurs par défaut créés (admin/admin123, user/user123)");
        }
    }
}

