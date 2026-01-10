package com.mondial.ticket.service;

import com.mondial.ticket.model.Ticket;
import java.util.Random;

/**
 * Service simulant une vente automatique de tickets en arrière-plan.
 * Cette classe implémente Runnable pour être exécutée dans un Thread séparé.
 */
public class VenteSimulationService implements Runnable {
    private TicketService ticketService;
    private volatile boolean running = true;
    private Random random = new Random();

    // Matchs du Mondial 2030
    private static final String[] MATCHS = {
        "Maroc vs Espagne",
        "Portugal vs France",
        "Brésil vs Argentine",
        "Allemagne vs Italie",
        "Angleterre vs Pays-Bas",
        "Belgique vs Croatie"
    };

    private static final String[] CATEGORIES = {"VIP", "Standard", "Tribune"};

    private static final double[] PRIX_BASE = {500.0, 150.0, 75.0};

    public VenteSimulationService(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    public void stopSimulation() {
        this.running = false;
    }

    @Override
    public void run() {
        System.out.println("[Thread Simulation] Démarrage de la vente automatique de tickets...");

        int count = 0;
        while (running && count < 5) { // On limite à 5 tickets pour la démo
            try {
                // Simulation d'un délai d'attente (ex: traitement d'une commande)
                Thread.sleep(2000);

                String match = MATCHS[random.nextInt(MATCHS.length)];
                int categorieIndex = random.nextInt(CATEGORIES.length);
                String categorie = CATEGORIES[categorieIndex];
                double prix = PRIX_BASE[categorieIndex] + random.nextDouble() * 50;

                Ticket t = new Ticket(match + "_" + (count + 1), categorie, Math.round(prix * 100.0) / 100.0);

                // Opération de persistance via le service
                ticketService.enregistrerTicket(t);

                System.out.println("[Thread Simulation] Nouveau ticket vendu : " + t);
                count++;

            } catch (InterruptedException e) {
                System.err.println("[Thread Simulation] Interruption du thread.");
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("[Thread Simulation] Fin de la simulation de vente.");
    }
}

