package com.mondial.ticket.repository;

import com.mondial.ticket.exception.TicketException;
import java.util.List;

/**
 * Interface générique définissant le contrat de persistance pour le Mondial 2030.
 * @param <T> Le type d'entité géré par le repository
 */
public interface Repository<T> {
    void ajouter(T element);

    List<T> listerTout();

    T trouverParNom(String nom) throws TicketException;

    void mettreAJour(T element) throws TicketException;

    void supprimer(String nom) throws TicketException;
}

