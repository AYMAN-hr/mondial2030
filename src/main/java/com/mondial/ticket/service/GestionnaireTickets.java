package com.mondial.ticket.service;

import com.mondial.ticket.exception.TicketException;
import com.mondial.ticket.model.Ticket;
import com.mondial.ticket.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Gestionnaire de tickets en mémoire pour le Mondial 2030.
 * Implémente le Repository et utilise les Streams pour le filtrage.
 */
public class GestionnaireTickets<T extends Ticket> implements Repository<T> {
    private List<T> inventaire = new ArrayList<>();

    @Override
    public void ajouter(T element) {
        inventaire.add(element);
        System.out.println("Ajout de ticket : " + element.getNomMatch() + " [" + element.getCategorie() + "]");
    }

    @Override
    public List<T> listerTout() {
        return new ArrayList<>(inventaire);
    }

    @Override
    public T trouverParNom(String nomMatch) throws TicketException {
        return inventaire.stream()
                .filter(t -> t.getNomMatch().equalsIgnoreCase(nomMatch))
                .findFirst()
                .orElseThrow(() -> new TicketException("Ticket pour '" + nomMatch + "' introuvable !"));
    }

    @Override
    public void mettreAJour(T element) throws TicketException {
        T existant = trouverParNom(element.getNomMatch());
        int index = inventaire.indexOf(existant);
        inventaire.set(index, element);
        System.out.println("Mise à jour ticket : " + element.getNomMatch());
    }

    @Override
    public void supprimer(String nomMatch) throws TicketException {
        T existant = trouverParNom(nomMatch);
        inventaire.remove(existant);
        System.out.println("Suppression de ticket : " + nomMatch);
    }

    // Utilisation des Streams pour filtrer par prix minimum
    public List<T> filtrerParPrixMin(double seuil) {
        return inventaire.stream()
                .filter(t -> t.getPrix() >= seuil)
                .collect(Collectors.toList());
    }

    // Filtrer par catégorie
    public List<T> filtrerParCategorie(String categorie) {
        return inventaire.stream()
                .filter(t -> t.getCategorie().equalsIgnoreCase(categorie))
                .collect(Collectors.toList());
    }

    // Statistiques sur les tickets VIP
    public long compterTicketsVIP() {
        return inventaire.stream()
                .filter(t -> "VIP".equalsIgnoreCase(t.getCategorie()))
                .count();
    }
}

