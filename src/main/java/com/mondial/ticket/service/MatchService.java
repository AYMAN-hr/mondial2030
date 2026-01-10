package com.mondial.ticket.service;

import com.mondial.ticket.dao.IDao;
import com.mondial.ticket.exception.TicketException;
import com.mondial.ticket.model.Match;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service métier pour la gestion des matchs du Mondial 2030.
 */
public class MatchService {
    private IDao<Match> dao;

    public MatchService(IDao<Match> dao) {
        this.dao = dao;
    }

    public void creerMatch(Match m) {
        dao.create(m);
    }

    public List<Match> listerMatchs() {
        return dao.readAll();
    }

    public Match chercherParNom(String nomComplet) throws TicketException {
        return dao.readByName(nomComplet);
    }

    public void supprimerMatch(String nomComplet) throws TicketException {
        dao.delete(nomComplet);
    }

    public void afficherTousLesMatchs() {
        List<Match> matchs = dao.readAll();
        if (matchs.isEmpty()) {
            System.out.println("Service: Aucun match disponible.");
            return;
        }
        System.out.println("\n--- LISTE DES MATCHS DU MONDIAL 2030 ---");
        matchs.forEach(System.out::println);
    }

    // Statistiques par pays hôte
    public void afficherStatistiquesParPays() {
        List<Match> matchs = dao.readAll();
        Map<String, Long> parPays = matchs.stream()
                .collect(Collectors.groupingBy(Match::getPays, Collectors.counting()));
        System.out.println("\n--- MATCHS PAR PAYS HÔTE ---");
        parPays.forEach((pays, count) -> System.out.println(pays + " : " + count + " match(s)"));
    }

    // Filtrer matchs par phase
    public List<Match> filtrerParPhase(String phase) {
        return dao.readAll().stream()
                .filter(m -> m.getPhase().equalsIgnoreCase(phase))
                .collect(Collectors.toList());
    }

    // Filtrer matchs par ville
    public List<Match> filtrerParVille(String ville) {
        return dao.readAll().stream()
                .filter(m -> m.getVille().equalsIgnoreCase(ville))
                .collect(Collectors.toList());
    }
}

