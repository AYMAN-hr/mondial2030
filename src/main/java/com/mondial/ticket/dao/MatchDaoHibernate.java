package com.mondial.ticket.dao;

import com.mondial.ticket.exception.TicketException;
import com.mondial.ticket.model.Match;
import com.mondial.ticket.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

/**
 * Implémentation Hibernate du DAO pour les matchs du Mondial 2030.
 */
public class MatchDaoHibernate implements IDao<Match> {

    @Override
    public void create(Match match) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(match);
            transaction.commit();
            System.out.println("Hibernate: Match persisté -> " + match.getNomComplet());
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.err.println("Erreur Hibernate Match (insertion) : " + e.getMessage());
        }
    }

    @Override
    public List<Match> readAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Match", Match.class).list();
        } catch (Exception e) {
            System.err.println("Erreur Hibernate Match (lecture) : " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public Match readByName(String nomComplet) throws TicketException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Recherche par combinaison équipe1 vs équipe2
            Match m = session.createQuery("from Match where concat(equipe1, ' vs ', equipe2) = :nom", Match.class)
                    .setParameter("nom", nomComplet)
                    .uniqueResult();
            if (m != null)
                return m;
        } catch (Exception e) {
            System.err.println("Erreur Hibernate Match (recherche) : " + e.getMessage());
        }
        throw new TicketException("Match '" + nomComplet + "' introuvable.");
    }

    @Override
    public void update(Match match) throws TicketException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(match);
            transaction.commit();
            System.out.println("Hibernate: Match mis à jour -> " + match.getNomComplet());
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw new TicketException("Erreur lors de la mise à jour du match.");
        }
    }

    @Override
    public void delete(String nomComplet) throws TicketException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // First, delete all tickets associated with this match
            int ticketsDeleted = session.createMutationQuery("delete from Ticket where nomMatch = :nom")
                    .setParameter("nom", nomComplet)
                    .executeUpdate();
            System.out.println("Hibernate: " + ticketsDeleted + " tickets supprimés pour le match -> " + nomComplet);

            // Then delete the match itself
            Match m = session.createQuery("from Match where concat(equipe1, ' vs ', equipe2) = :nom", Match.class)
                    .setParameter("nom", nomComplet)
                    .uniqueResult();
            if (m != null) {
                session.remove(m);
                transaction.commit();
                System.out.println("Hibernate: Match supprimé -> " + nomComplet);
            } else {
                transaction.rollback();
                throw new TicketException("Match '" + nomComplet + "' introuvable.");
            }
        } catch (TicketException e) {
            throw e;
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw new TicketException("Erreur lors de la suppression du match: " + e.getMessage());
        }
    }
}

