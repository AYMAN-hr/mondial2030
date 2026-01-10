package com.mondial.ticket.dao;

import com.mondial.ticket.exception.TicketException;
import com.mondial.ticket.model.Ticket;
import com.mondial.ticket.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation JDBC du DAO pour les tickets.
 */
public class TicketDaoImpl implements IDao<Ticket> {

    @Override
    public void create(Ticket ticket) {
        String sql = "INSERT INTO tickets (nom_match, categorie, prix) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ticket.getNomMatch());
            pstmt.setString(2, ticket.getCategorie());
            pstmt.setDouble(3, ticket.getPrix());
            pstmt.executeUpdate();
            System.out.println("DAO: Ticket créé -> " + ticket.getNomMatch() + " [" + ticket.getCategorie() + "]");
        } catch (SQLException e) {
            System.err.println("Erreur DAO (insertion) : " + e.getMessage());
        }
    }

    @Override
    public List<Ticket> readAll() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT nom_match, categorie, prix FROM tickets";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tickets.add(new Ticket(rs.getString("nom_match"), rs.getString("categorie"), rs.getDouble("prix")));
            }
        } catch (SQLException e) {
            System.err.println("Erreur DAO (lecture) : " + e.getMessage());
        }
        return tickets;
    }

    @Override
    public Ticket readByName(String nomMatch) throws TicketException {
        String sql = "SELECT nom_match, categorie, prix FROM tickets WHERE nom_match = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nomMatch);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Ticket(rs.getString("nom_match"), rs.getString("categorie"), rs.getDouble("prix"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur DAO (recherche) : " + e.getMessage());
        }
        throw new TicketException("Ticket pour le match '" + nomMatch + "' introuvable via DAO.");
    }

    @Override
    public void update(Ticket ticket) throws TicketException {
        String sql = "UPDATE tickets SET prix = ?, categorie = ? WHERE nom_match = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, ticket.getPrix());
            pstmt.setString(2, ticket.getCategorie());
            pstmt.setString(3, ticket.getNomMatch());
            int rows = pstmt.executeUpdate();
            if (rows == 0)
                throw new TicketException("Mise à jour DAO impossible : " + ticket.getNomMatch() + " inconnu.");
            System.out.println("DAO: Ticket mis à jour -> " + ticket.getNomMatch());
        } catch (SQLException e) {
            System.err.println("Erreur DAO (maj) : " + e.getMessage());
        }
    }

    @Override
    public void delete(String nomMatch) throws TicketException {
        String sql = "DELETE FROM tickets WHERE nom_match = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nomMatch);
            int rows = pstmt.executeUpdate();
            if (rows == 0)
                throw new TicketException("Suppression DAO impossible : " + nomMatch + " inconnu.");
            System.out.println("DAO: Ticket supprimé -> " + nomMatch);
        } catch (SQLException e) {
            System.err.println("Erreur DAO (suppression) : " + e.getMessage());
        }
    }
}

