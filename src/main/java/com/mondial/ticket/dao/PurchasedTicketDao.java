package com.mondial.ticket.dao;

import com.mondial.ticket.model.PurchasedTicket;
import com.mondial.ticket.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * DAO pour la gestion des tickets achetés dans la base de données.
 */
public class PurchasedTicketDao {

    /**
     * Enregistrer un ticket acheté
     */
    public boolean create(PurchasedTicket purchasedTicket) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(purchasedTicket);
            transaction.commit();
            System.out.println("Hibernate: Ticket acheté enregistré -> " + purchasedTicket.getTicketId());
            return true;
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.err.println("Erreur Hibernate (création ticket acheté) : " + e.getMessage());
            return false;
        }
    }

    /**
     * Trouver tous les tickets achetés par un utilisateur
     */
    public List<PurchasedTicket> findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from PurchasedTicket where username = :username", PurchasedTicket.class)
                    .setParameter("username", username)
                    .list();
        } catch (Exception e) {
            System.err.println("Erreur Hibernate (recherche tickets utilisateur) : " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Trouver les tickets valides d'un utilisateur
     */
    public List<PurchasedTicket> findValidByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from PurchasedTicket where username = :username and status = 'VALID'",
                    PurchasedTicket.class)
                    .setParameter("username", username)
                    .list();
        } catch (Exception e) {
            System.err.println("Erreur Hibernate (recherche tickets valides) : " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Trouver un ticket acheté par son ticketId
     */
    public PurchasedTicket findByTicketId(String ticketId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from PurchasedTicket where ticketId = :ticketId", PurchasedTicket.class)
                    .setParameter("ticketId", ticketId)
                    .uniqueResult();
        } catch (Exception e) {
            System.err.println("Erreur Hibernate (recherche par ticketId) : " + e.getMessage());
            return null;
        }
    }

    /**
     * Mettre à jour un ticket acheté (ex: changer le statut)
     */
    public boolean update(PurchasedTicket purchasedTicket) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(purchasedTicket);
            transaction.commit();
            System.out.println("Hibernate: Ticket acheté mis à jour -> " + purchasedTicket.getTicketId());
            return true;
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.err.println("Erreur Hibernate (maj ticket acheté) : " + e.getMessage());
            return false;
        }
    }

    /**
     * Récupérer tous les tickets achetés
     */
    public List<PurchasedTicket> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from PurchasedTicket", PurchasedTicket.class).list();
        } catch (Exception e) {
            System.err.println("Erreur Hibernate (liste tickets achetés) : " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Calculer le total dépensé par un utilisateur
     */
    public double getTotalSpentByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Double total = session.createQuery(
                    "select sum(price) from PurchasedTicket where username = :username and status != 'REFUNDED'",
                    Double.class)
                    .setParameter("username", username)
                    .uniqueResult();
            return total != null ? total : 0.0;
        } catch (Exception e) {
            System.err.println("Erreur Hibernate (calcul total dépensé) : " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * Compter les tickets achetés par un utilisateur
     */
    public long countByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                    "select count(*) from PurchasedTicket where username = :username",
                    Long.class)
                    .setParameter("username", username)
                    .uniqueResult();
            return count != null ? count : 0L;
        } catch (Exception e) {
            System.err.println("Erreur Hibernate (comptage tickets) : " + e.getMessage());
            return 0L;
        }
    }
}

