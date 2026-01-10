package com.mondial.ticket.service;

import com.mondial.ticket.model.Ticket;
import com.mondial.ticket.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service pour gérer l'historique des achats des utilisateurs.
 */
public class UserTicketService {

    private static UserTicketService instance;
    private Map<String, List<PurchasedTicket>> userPurchases = new HashMap<>();
    private Set<String> favoriteMatches = new HashSet<>();

    public static class PurchasedTicket {
        private String ticketId;
        private String matchName;
        private String category;
        private double price;
        private String purchaseDate;
        private String qrCode;
        private String status; // VALID, USED, REFUNDED

        public PurchasedTicket(Ticket ticket) {
            this.ticketId = "TKT-" + System.currentTimeMillis();
            this.matchName = ticket.getNomMatch();
            this.category = ticket.getCategorie();
            this.price = ticket.getPrix();
            this.purchaseDate = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            this.qrCode = generateQRCode();
            this.status = "VALID";
        }

        private String generateQRCode() {
            // Simulate QR code as a unique string
            return "QR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }

        public String getTicketId() { return ticketId; }
        public String getMatchName() { return matchName; }
        public String getCategory() { return category; }
        public double getPrice() { return price; }
        public String getPurchaseDate() { return purchaseDate; }
        public String getQrCode() { return qrCode; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        @Override
        public String toString() {
            return ticketId + " | " + matchName + " [" + category + "] - " + price + "€";
        }
    }

    private UserTicketService() {}

    public static UserTicketService getInstance() {
        if (instance == null) {
            instance = new UserTicketService();
        }
        return instance;
    }

    /**
     * Enregistrer un achat de ticket pour un utilisateur
     */
    public PurchasedTicket recordPurchase(String username, Ticket ticket) {
        PurchasedTicket purchase = new PurchasedTicket(ticket);

        userPurchases.computeIfAbsent(username, k -> new ArrayList<>());
        userPurchases.get(username).add(purchase);

        return purchase;
    }

    /**
     * Obtenir tous les tickets achetés par un utilisateur
     */
    public List<PurchasedTicket> getUserTickets(String username) {
        return userPurchases.getOrDefault(username, new ArrayList<>());
    }

    /**
     * Obtenir les tickets valides d'un utilisateur
     */
    public List<PurchasedTicket> getValidTickets(String username) {
        return getUserTickets(username).stream()
            .filter(t -> "VALID".equals(t.getStatus()))
            .collect(Collectors.toList());
    }

    /**
     * Demander un remboursement
     */
    public boolean requestRefund(String username, String ticketId) {
        List<PurchasedTicket> tickets = getUserTickets(username);
        for (PurchasedTicket t : tickets) {
            if (t.getTicketId().equals(ticketId) && "VALID".equals(t.getStatus())) {
                t.setStatus("REFUNDED");
                return true;
            }
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
        return getUserTickets(username).stream()
            .filter(t -> !"REFUNDED".equals(t.getStatus()))
            .mapToDouble(PurchasedTicket::getPrice)
            .sum();
    }

    /**
     * Enregistrer un ticket gagné au tirage au sort
     */
    public PurchasedTicket recordLotteryWin(String username, String matchName, String category, double price) {
        Ticket fakeTicket = new Ticket(matchName, category, price);
        PurchasedTicket purchase = new PurchasedTicket(fakeTicket);
        purchase.setStatus("WON"); // Mark as won from lottery

        userPurchases.computeIfAbsent(username, k -> new ArrayList<>());
        userPurchases.get(username).add(purchase);

        return purchase;
    }
}

