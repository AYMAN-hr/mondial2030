package com.mondial.ticket.dao;

import com.mondial.ticket.exception.TicketException;
import java.util.List;

/**
 * Interface DAO générique pour les opérations CRUD.
 * @param <T> Le type d'entité
 */
public interface IDao<T> {
    void create(T element);

    List<T> readAll();

    T readByName(String nom) throws TicketException;

    void update(T element) throws TicketException;

    void delete(String nom) throws TicketException;
}

