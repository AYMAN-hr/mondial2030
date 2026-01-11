package com.mondial.ticket.dao;

import com.mondial.ticket.exception.TicketException;
import com.mondial.ticket.model.Ticket;
import com.mondial.ticket.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

/**
 * Implémentation Hibernate du DAO pour les tickets du Mondial 2030.
 */
public class TicketDaoHibernate implements IDao<Ticket> {

    @Override
    public void create(Ticket ticket) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(ticket);
            transaction.commit();
            System.out.println("Hibernate: Ticket persisté -> " + ticket.getNomMatch() + " [" + ticket.getCategorie() + "] ID=" + ticket.getId());
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.err.println("Erreur Hibernate (insertion) : " + e.getMessage());
        }
    }

    @Override
    public List<Ticket> readAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Ticket", Ticket.class).list();
        } catch (Exception e) {
            System.err.println("Erreur Hibernate (lecture) : " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public Ticket readByName(String nomMatch) throws TicketException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Find first available ticket for this match
            Ticket t = session.createQuery("from Ticket where nomMatch = :nom and status = 'DISPONIBLE'", Ticket.class)
                    .setParameter("nom", nomMatch)
                    .setMaxResults(1)
                    .uniqueResult();
            if (t != null)
                return t;
        } catch (Exception e) {
            System.err.println("Erreur Hibernate (recherche) : " + e.getMessage());
        }
        throw new TicketException("Ticket pour '" + nomMatch + "' introuvable via Hibernate.");
    }

    /**
     * Find ticket by ID
     */
    public Ticket readById(int id) throws TicketException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Ticket t = session.get(Ticket.class, id);
            if (t != null)
                return t;
        } catch (Exception e) {
            System.err.println("Erreur Hibernate (recherche par ID) : " + e.getMessage());
        }
        throw new TicketException("Ticket ID=" + id + " introuvable.");
    }

    @Override
    public void update(Ticket ticket) throws TicketException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(ticket);
            transaction.commit();
            System.out.println("Hibernate: Ticket mis à jour -> ID=" + ticket.getId() + " " + ticket.getNomMatch());
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.err.println("Erreur Hibernate (maj) : " + e.getMessage());
            throw new TicketException("Erreur lors de la mise à jour Hibernate.");
        }
    }

    @Override
    public void delete(String nomMatch) throws TicketException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            // Delete first ticket with this match name
            Ticket t = session.createQuery("from Ticket where nomMatch = :nom", Ticket.class)
                    .setParameter("nom", nomMatch)
                    .setMaxResults(1)
                    .uniqueResult();
            if (t != null) {
                session.remove(t);
                System.out.println("Hibernate: Ticket supprimé -> " + nomMatch);
            } else {
                throw new TicketException("Suppression Hibernate impossible : " + nomMatch + " inconnu.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.err.println("Erreur Hibernate (suppression) : " + e.getMessage());
            throw new TicketException("Erreur lors de la suppression Hibernate.");
        }
    }

    /**
     * Delete all tickets
     */
    public void deleteAll() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createMutationQuery("delete from Ticket").executeUpdate();
            transaction.commit();
            System.out.println("Hibernate: Tous les tickets supprimés");
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.err.println("Erreur Hibernate (suppression tout) : " + e.getMessage());
        }
    }

    /**
     * Delete all tickets for a specific match
     */
    public void deleteByMatch(String nomMatch) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            int deleted = session.createMutationQuery("delete from Ticket where nomMatch = :nom")
                    .setParameter("nom", nomMatch)
                    .executeUpdate();
            transaction.commit();
            System.out.println("Hibernate: " + deleted + " tickets supprimés pour le match -> " + nomMatch);
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.err.println("Erreur Hibernate (suppression par match) : " + e.getMessage());
        }
    }
}

