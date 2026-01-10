package com.mondial.ticket.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Modèle représentant un match du Mondial 2030.
 * Contient les informations du match et la collection de tickets associés.
 */
public class Match {
    private int id;
    private String equipe1;
    private String equipe2;
    private String stade;
    private String ville;
    private String pays; // Maroc, Espagne, Portugal
    private LocalDateTime dateHeure;
    private String phase; // Groupe, Huitième, Quart, Demi-finale, Finale
    private Set<Ticket> tickets = new HashSet<>();

    // Constructeur par défaut pour Hibernate
    public Match() {
    }

    public Match(String equipe1, String equipe2, String stade, String ville, String pays, String phase) {
        this.equipe1 = equipe1;
        this.equipe2 = equipe2;
        this.stade = stade;
        this.ville = ville;
        this.pays = pays;
        this.phase = phase;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEquipe1() {
        return equipe1;
    }

    public void setEquipe1(String equipe1) {
        this.equipe1 = equipe1;
    }

    public String getEquipe2() {
        return equipe2;
    }

    public void setEquipe2(String equipe2) {
        this.equipe2 = equipe2;
    }

    public String getStade() {
        return stade;
    }

    public void setStade(String stade) {
        this.stade = stade;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    /**
     * Méthode utilitaire pour ajouter un ticket au match
     * en maintenant la cohérence bidirectionnelle.
     */
    public void ajouterTicket(Ticket t) {
        this.tickets.add(t);
        t.setMatch(this);
    }

    public String getNomComplet() {
        return equipe1 + " vs " + equipe2;
    }

    @Override
    public String toString() {
        return "Match{" + "id=" + id + ", " + equipe1 + " vs " + equipe2 +
               ", stade='" + stade + "', ville='" + ville + "', pays='" + pays +
               "', phase='" + phase + "', nbTickets=" + tickets.size() + '}';
    }
}

