package com.mondial.ticket.service;

import com.mondial.ticket.dao.PurchasedTicketDao;
import com.mondial.ticket.model.PurchasedTicket;
import com.mondial.ticket.model.Ticket;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service pour gérer l'historique des achats des utilisateurs.
 * Utilise la base de données pour la persistance.
 */
public class UserTicketService {

    private static UserTicketService instance;
    private PurchasedTicketDao purchasedTicketDao = new PurchasedTicketDao();
    private Set<String> favoriteMatches = new HashSet<>();

    private UserTicketService() {}

    public static UserTicketService getInstance() {
        if (instance == null) {
            instance = new UserTicketService();
        }
        return instance;
    }

    /**
     * Enregistrer un achat de ticket pour un utilisateur
     * Les données sont persistées en base de données
     */
    public PurchasedTicket recordPurchase(String username, Ticket ticket) {
        PurchasedTicket purchase = new PurchasedTicket(username, ticket);
        purchasedTicketDao.create(purchase);
        return purchase;
    }

    /**
     * Obtenir tous les tickets achetés par un utilisateur
     */
    public List<PurchasedTicket> getUserTickets(String username) {
        return purchasedTicketDao.findByUsername(username);
    }

    /**
     * Obtenir les tickets valides d'un utilisateur
     */
    public List<PurchasedTicket> getValidTickets(String username) {
        return purchasedTicketDao.findValidByUsername(username);
    }

    /**
     * Demander un remboursement
     */
    public boolean requestRefund(String username, String ticketId) {
        PurchasedTicket ticket = purchasedTicketDao.findByTicketId(ticketId);
        if (ticket != null && ticket.getUsername().equals(username) && "VALID".equals(ticket.getStatus())) {
            ticket.setStatus("REFUNDED");
            return purchasedTicketDao.update(ticket);
        }
        return false;
    }

    /**
     * Ajouter un match aux favoris
     */
    public void addFavorite(String matchName) {
        favoriteMatches.add(matchName);
    }

    /**
     * Retirer un match des favoris
     */
    public void removeFavorite(String matchName) {
        favoriteMatches.remove(matchName);
    }

    /**
     * Vérifier si un match est en favori
     */
    public boolean isFavorite(String matchName) {
        return favoriteMatches.contains(matchName);
    }

    /**
     * Obtenir tous les favoris
     */
    public Set<String> getFavorites() {
        return new HashSet<>(favoriteMatches);
    }

    /**
     * Obtenir le total dépensé par un utilisateur
     */
    public double getTotalSpent(String username) {
        return purchasedTicketDao.getTotalSpentByUsername(username);
    }

    /**
     * Enregistrer un ticket gagné au tirage au sort
     * Les données sont persistées en base de données
     */
    public PurchasedTicket recordLotteryWin(String username, String matchName, String category, double price) {
        Ticket fakeTicket = new Ticket(matchName, category, price);
        PurchasedTicket purchase = new PurchasedTicket(username, fakeTicket);
        purchase.setStatus("WON"); // Mark as won from lottery
        purchasedTicketDao.create(purchase);
        return purchase;
    }

    /**
     * Obtenir le nombre total de tickets achetés par un utilisateur
     */
    public long getTicketCount(String username) {
        return purchasedTicketDao.countByUsername(username);
    }

    /**
     * Obtenir tous les tickets achetés (admin)
     */
    public List<PurchasedTicket> getAllPurchasedTickets() {
        return purchasedTicketDao.findAll();
    }
}

