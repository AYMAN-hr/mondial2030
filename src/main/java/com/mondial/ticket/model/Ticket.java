package com.mondial.ticket.model;

/**
 * Modèle représentant un ticket pour un match du Mondial 2030.
 * Contient les informations essentielles : nom du match, catégorie, prix.
 */
public class Ticket {
    private int id;
    private String nomMatch;
    private String categorie; // VIP, Standard, Tribune
    private double prix;
    private Match match;
    private String status; // DISPONIBLE, VENDU, RESERVE
    private String acheteur; // Buyer name

    // Constructeur par défaut requis par Hibernate
    public Ticket() {
        this.status = "DISPONIBLE";
    }

    public Ticket(String nomMatch, String categorie, double prix) {
        this.nomMatch = nomMatch;
        this.categorie = categorie;
        this.prix = prix;
        this.status = "DISPONIBLE";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomMatch() {
        return nomMatch;
    }

    public void setNomMatch(String nomMatch) {
        this.nomMatch = nomMatch;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAcheteur() {
        return acheteur;
    }

    public void setAcheteur(String acheteur) {
        this.acheteur = acheteur;
    }

    @Override
    public String toString() {
        return nomMatch + " [" + categorie + "] - " + prix + "€ (" + status + ")";
    }
}

