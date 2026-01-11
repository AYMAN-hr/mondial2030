package com.mondial.ticket.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Entité représentant un ticket acheté par un utilisateur.
 * Persistée en base de données.
 */
public class PurchasedTicket {
    private int id;
    private String ticketId;
    private String username;
    private String matchName;
    private String category;
    private double price;
    private LocalDateTime purchaseDate;
    private String qrCode;
    private String status; // VALID, USED, REFUNDED, WON (tirage au sort)
    private int originalTicketId; // Reference to the original ticket

    public PurchasedTicket() {
        this.purchaseDate = LocalDateTime.now();
        this.status = "VALID";
    }

    public PurchasedTicket(String username, Ticket ticket) {
        this.ticketId = "TKT-" + System.currentTimeMillis();
        this.username = username;
        this.matchName = ticket.getNomMatch();
        this.category = ticket.getCategorie();
        this.price = ticket.getPrix();
        this.purchaseDate = LocalDateTime.now();
        this.qrCode = generateQRCode();
        this.status = "VALID";
        this.originalTicketId = ticket.getId();
    }

    private String generateQRCode() {
        return "QR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getMatchName() { return matchName; }
    public void setMatchName(String matchName) { this.matchName = matchName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public LocalDateTime getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDateTime purchaseDate) { this.purchaseDate = purchaseDate; }

    public String getPurchaseDateFormatted() {
        return purchaseDate != null ?
            purchaseDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "";
    }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getOriginalTicketId() { return originalTicketId; }
    public void setOriginalTicketId(int originalTicketId) { this.originalTicketId = originalTicketId; }

    @Override
    public String toString() {
        return ticketId + " | " + matchName + " [" + category + "] - " + price + "€ (" + status + ")";
    }
}

