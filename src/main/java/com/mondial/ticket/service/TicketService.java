package com.mondial.ticket.service;

import com.mondial.ticket.dao.IDao;
import com.mondial.ticket.exception.TicketException;
import com.mondial.ticket.model.Ticket;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service métier pour la gestion des tickets du Mondial 2030.
 */
public class TicketService {
    private IDao<Ticket> dao;

    public TicketService(IDao<Ticket> dao) {
        this.dao = dao;
    }

    public void enregistrerTicket(Ticket t) {
        dao.create(t);
    }

    public List<Ticket> recupererTout() {
        return dao.readAll();
    }

    public void modifierPrix(String nomMatch, double nouveauPrix) throws TicketException {
        Ticket t = dao.readByName(nomMatch);
        dao.update(new Ticket(t.getNomMatch(), t.getCategorie(), nouveauPrix));
    }

    public void retirerTicket(String nomMatch) throws TicketException {
        dao.delete(nomMatch);
    }

    /**
     * Acheter un ticket - marque le ticket comme vendu
     */
    public void acheterTicket(String nomMatch, String acheteur) throws TicketException {
        Ticket t = dao.readByName(nomMatch);
        if (t == null) {
            throw new TicketException("Ticket non trouvé: " + nomMatch);
        }
        if ("VENDU".equals(t.getStatus())) {
            throw new TicketException("Ce ticket est déjà vendu!");
        }
        t.setStatus("VENDU");
        t.setAcheteur(acheteur);
        dao.update(t);

        // Simulate email notification
        System.out.println("\n📧 EMAIL DE CONFIRMATION ENVOYÉ");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("Destinataire: " + acheteur);
        System.out.println("Sujet: Confirmation d'achat - Mondial 2030");
        System.out.println("Match: " + t.getNomMatch());
        System.out.println("Catégorie: " + t.getCategorie());
        System.out.println("Prix: " + t.getPrix() + "€");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
    }

    /**
     * Annuler un achat - remet le ticket en disponible
     */
    public void annulerAchat(String nomMatch) throws TicketException {
        Ticket t = dao.readByName(nomMatch);
        if (t == null) {
            throw new TicketException("Ticket non trouvé: " + nomMatch);
        }
        t.setStatus("DISPONIBLE");
        t.setAcheteur(null);
        dao.update(t);
    }

    /**
     * Récupérer les tickets disponibles uniquement
     */
    public List<Ticket> recupererDisponibles() {
        return dao.readAll().stream()
                .filter(t -> "DISPONIBLE".equals(t.getStatus()) || t.getStatus() == null)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les tickets vendus
     */
    public List<Ticket> recupererVendus() {
        return dao.readAll().stream()
                .filter(t -> "VENDU".equals(t.getStatus()))
                .collect(Collectors.toList());
    }

    // Analyse avec Streams
    public void afficherStatistiques() {
        List<Ticket> tickets = dao.readAll();
        if (tickets.isEmpty()) {
            System.out.println("Service: Aucune donnée pour les statistiques.");
            return;
        }

        System.out.println("\n--- STATISTIQUES TICKETS MONDIAL 2030 ---");
        double total = tickets.stream().mapToDouble(Ticket::getPrix).sum();
        double moyenne = tickets.stream().mapToDouble(Ticket::getPrix).average().orElse(0);
        long nbVIP = tickets.stream().filter(t -> "VIP".equalsIgnoreCase(t.getCategorie())).count();
        long nbStandard = tickets.stream().filter(t -> "Standard".equalsIgnoreCase(t.getCategorie())).count();

        System.out.println("Valeur totale des tickets : " + total + "€");
        System.out.printf("Prix moyen : %.2f€\n", moyenne);
        System.out.println("Nombre de tickets VIP : " + nbVIP);
        System.out.println("Nombre de tickets Standard : " + nbStandard);

        // Groupement par catégorie
        Map<String, Long> parCategorie = tickets.stream()
                .collect(Collectors.groupingBy(Ticket::getCategorie, Collectors.counting()));
        System.out.println("Répartition par catégorie : " + parCategorie);

        // Liste des matchs uniques
        String listeMatchs = tickets.stream()
                .map(Ticket::getNomMatch)
                .distinct()
                .sorted()
                .collect(Collectors.joining(" | "));
        System.out.println("Matchs disponibles : " + listeMatchs);
    }

    // Recherche tickets par pays hôte
    public List<Ticket> rechercherParPaysHote(String pays) {
        return dao.readAll().stream()
                .filter(t -> t.getNomMatch().toLowerCase().contains(pays.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Supprimer tous les tickets
     */
    public void supprimerTout() {
        List<Ticket> tickets = dao.readAll();
        for (Ticket t : tickets) {
            try {
                dao.delete(t.getNomMatch());
            } catch (Exception e) {
                System.err.println("Erreur suppression: " + e.getMessage());
            }
        }
        System.out.println("✅ Tous les tickets ont été supprimés.");
    }

    /**
     * Créer des données de test (mock data)
     */
    public void creerDonneesTest() {
        // Supprimer les anciens tickets
        supprimerTout();

        // Matchs du Mondial 2030 - format: {equipe1, equipe2, stade, ville, pays, phase}
        String[][] matchsData = {
            {"Maroc", "Espagne", "Grand Stade de Casablanca", "Casablanca", "Maroc", "Quart"},
            {"France", "Allemagne", "Santiago Bernabéu", "Madrid", "Espagne", "Demi-finale"},
            {"Bresil", "Argentine", "Estádio da Luz", "Lisbonne", "Portugal", "Finale"},
            {"Portugal", "Angleterre", "Estádio do Dragão", "Porto", "Portugal", "Groupe"},
            {"Maroc", "France", "Complexe Moulay Abdellah", "Rabat", "Maroc", "Demi-finale"},
            {"Espagne", "Italie", "Camp Nou", "Barcelone", "Espagne", "Quart"}
        };

        String[] categories = {"VIP", "Standard", "Tribune"};
        double[] prix = {500.0, 200.0, 80.0};

        int ticketCount = 0;
        for (String[] matchData : matchsData) {
            String nomMatch = matchData[0] + " vs " + matchData[1];

            for (int i = 0; i < categories.length; i++) {
                // Créer 3 tickets par catégorie par match
                for (int j = 0; j < 3; j++) {
                    Ticket t = new Ticket(nomMatch, categories[i], prix[i]);
                    t.setStatus("DISPONIBLE");
                    dao.create(t);
                    ticketCount++;
                }
            }
        }

        System.out.println("✅ " + ticketCount + " tickets de test créés!");
    }
}

