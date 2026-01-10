package com.mondial.ticket.repository;

import com.mondial.ticket.exception.TicketException;
import com.mondial.ticket.model.Ticket;
import com.mondial.ticket.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository JDBC pour les tickets du Mondial 2030.
 */
public class TicketJDBCRepository implements Repository<Ticket> {

    @Override
    public void ajouter(Ticket ticket) {
        String sql = "INSERT INTO tickets (nom_match, categorie, prix) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ticket.getNomMatch());
            pstmt.setString(2, ticket.getCategorie());
            pstmt.setDouble(3, ticket.getPrix());
            pstmt.executeUpdate();
            System.out.println("JDBC: Ajout de ticket pour " + ticket.getNomMatch() + " dans la base de données.");
        } catch (SQLException e) {
            System.err.println("Erreur JDBC lors de l'ajout : " + e.getMessage());
        }
    }

    @Override
    public List<Ticket> listerTout() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT nom_match, categorie, prix FROM tickets";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tickets.add(new Ticket(rs.getString("nom_match"), rs.getString("categorie"), rs.getDouble("prix")));
            }
        } catch (SQLException e) {
            System.err.println("Erreur JDBC lors du listing : " + e.getMessage());
        }
        return tickets;
    }

    @Override
    public Ticket trouverParNom(String nomMatch) throws TicketException {
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
            System.err.println("Erreur JDBC lors de la recherche : " + e.getMessage());
        }
        throw new TicketException("Ticket pour '" + nomMatch + "' introuvable dans la base de données !");
    }

    @Override
    public void mettreAJour(Ticket ticket) throws TicketException {
        String sql = "UPDATE tickets SET prix = ?, categorie = ? WHERE nom_match = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, ticket.getPrix());
            pstmt.setString(2, ticket.getCategorie());
            pstmt.setString(3, ticket.getNomMatch());
            int rows = pstmt.executeUpdate();
            if (rows == 0)
                throw new TicketException("Mise à jour impossible : " + ticket.getNomMatch() + " introuvable.");
            System.out.println("JDBC: Mise à jour de ticket pour " + ticket.getNomMatch());
        } catch (SQLException e) {
            System.err.println("Erreur JDBC lors de la maj : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(String nomMatch) throws TicketException {
        String sql = "DELETE FROM tickets WHERE nom_match = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nomMatch);
            int rows = pstmt.executeUpdate();
            if (rows == 0)
                throw new TicketException("Suppression impossible : " + nomMatch + " introuvable.");
            System.out.println("JDBC: Suppression de ticket pour " + nomMatch);
        } catch (SQLException e) {
            System.err.println("Erreur JDBC lors de la suppression : " + e.getMessage());
        }
    }
}

