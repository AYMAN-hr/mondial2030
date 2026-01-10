package com.mondial.ticket;

import com.mondial.ticket.dao.TicketDaoImpl;
import com.mondial.ticket.dao.TicketDaoHibernate;
import com.mondial.ticket.dao.MatchDaoHibernate;
import com.mondial.ticket.exception.TicketException;
import com.mondial.ticket.model.Ticket;
import com.mondial.ticket.model.Match;
import com.mondial.ticket.service.TicketService;
import com.mondial.ticket.service.MatchService;
import com.mondial.ticket.service.GestionnaireTickets;
import com.mondial.ticket.service.VenteSimulationService;
import com.mondial.ticket.util.HibernateUtil;

/**
 * Point d'entrée principal de l'application Mondial 2030.
 * Démontre toutes les fonctionnalités : mémoire, JDBC, Hibernate, Threads.
 */
public class MainApp {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║   PLATEFORME INTELLIGENTE DE GESTION DES TICKETS             ║");
        System.out.println("║                    MONDIAL 2030                              ║");
        System.out.println("║         Maroc - Espagne - Portugal                           ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");

        // 1. ANCIENNE APPROCHE (Mémoire)
        System.out.println("\n=== 1. MODE MÉMOIRE (Gestionnaire en RAM) ===");
        GestionnaireTickets<Ticket> gestionnaireMemoire = new GestionnaireTickets<>();
        gestionnaireMemoire.ajouter(new Ticket("Maroc vs Espagne", "VIP", 500.0));
        gestionnaireMemoire.ajouter(new Ticket("Portugal vs France", "Standard", 150.0));
        gestionnaireMemoire.ajouter(new Ticket("Brésil vs Argentine", "Tribune", 75.0));

        System.out.println("Tickets VIP en mémoire : " + gestionnaireMemoire.compterTicketsVIP());
        System.out.println("Tickets >= 100€ : " + gestionnaireMemoire.filtrerParPrixMin(100.0));

        // 2. ARCHITECTURE JDBC (DAO Classique)
        System.out.println("\n=== 2. ARCHITECTURE JDBC (DAO Classique) ===");
        TicketService serviceJdbc = new TicketService(new TicketDaoImpl());
        serviceJdbc.enregistrerTicket(new Ticket("Allemagne vs Italie", "VIP", 450.0));
        serviceJdbc.recupererTout().forEach(System.out::println);

        // 3. MAPPING OBJET RELATIONNEL (Hibernate XML)
        System.out.println("\n=== 3. MAPPING OBJET RELATIONNEL (Hibernate XML) ===");
        try {
            TicketService serviceHibernate = new TicketService(new TicketDaoHibernate());

            // Test CRUD via Hibernate
            serviceHibernate.enregistrerTicket(new Ticket("Finale Mondial 2030", "VIP", 1500.0));
            serviceHibernate.enregistrerTicket(new Ticket("Demi-finale 1", "Standard", 350.0));
            serviceHibernate.modifierPrix("Finale Mondial 2030", 1800.0);

            System.out.println("Tickets via Hibernate :");
            serviceHibernate.recupererTout().forEach(t -> System.out.println("  " + t));

            // Statistiques riches (Streams)
            serviceHibernate.afficherStatistiques();

            // 4. TEST RELATION ONE-TO-MANY (Match -> Tickets)
            System.out.println("\n=== 4. TEST RELATION ONE-TO-MANY (Match -> Tickets) ===");
            MatchService matchService = new MatchService(new MatchDaoHibernate());

            Match matchInaugural = new Match("Maroc", "Portugal", "Grand Stade de Casablanca", "Casablanca", "Maroc", "Groupe");
            matchInaugural.ajouterTicket(new Ticket("Maroc vs Portugal - VIP", "VIP", 600.0));
            matchInaugural.ajouterTicket(new Ticket("Maroc vs Portugal - Standard", "Standard", 200.0));

            matchService.creerMatch(matchInaugural);
            matchService.afficherTousLesMatchs();

            // 5. TEST PROGRAMMATION CONCURRENTE (Threads)
            System.out.println("\n=== 5. TEST PROGRAMMATION CONCURRENTE (Vente automatique) ===");
            VenteSimulationService simulation = new VenteSimulationService(serviceHibernate);
            Thread t = new Thread(simulation);
            t.start(); // Lance le processus en arrière-plan

            System.out.println("[Main] Le thread de simulation de vente tourne en parallèle...");

            // On attend un peu pour voir le thread travailler avant de fermer l'app
            Thread.sleep(12000);
            simulation.stopSimulation();
            t.join(); // Attend la fin du thread proprement

            // Affichage final
            System.out.println("\n=== RÉSUMÉ FINAL ===");
            serviceHibernate.afficherStatistiques();

        } catch (TicketException e) {
            System.err.println("Erreur ORM : " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Erreur de thread : " + e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            // Fermeture propre de la SessionFactory Hibernate
            HibernateUtil.shutdown();
        }

        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║              FIN DE LA DÉMONSTRATION                         ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }
}

