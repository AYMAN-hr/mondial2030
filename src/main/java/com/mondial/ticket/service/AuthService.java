package com.mondial.ticket.service;

import com.mondial.ticket.dao.UserDao;
import com.mondial.ticket.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service d'authentification pour gérer les utilisateurs et sessions.
 * Utilise la base de données pour la persistance.
 */
public class AuthService {

    private static AuthService instance;
    private UserDao userDao = new UserDao();
    private User currentUser = null;

    private AuthService() {
        // Initialiser les utilisateurs par défaut dans la BD
        userDao.initializeDefaultUsers();
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    /**
     * Authentifier un utilisateur
     */
    public boolean login(String username, String password) {
        User user = userDao.authenticate(username, password);
        if (user != null) {
            currentUser = user;
            System.out.println("✅ Connexion réussie: " + user.getNom() + " (" + user.getRole() + ")");
            return true;
        }
        System.out.println("❌ Échec de connexion pour: " + username);
        return false;
    }

    /**
     * Déconnecter l'utilisateur actuel
     */
    public void logout() {
        if (currentUser != null) {
            System.out.println("👋 Déconnexion: " + currentUser.getNom());
            currentUser = null;
        }
    }

    /**
     * Obtenir l'utilisateur connecté
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Vérifier si un utilisateur est connecté
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Vérifier si l'utilisateur actuel est admin
     */
    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    /**
     * Inscrire un nouvel utilisateur (registration)
     */
    public boolean register(String username, String password, String nom, String email) {
        // Vérifier si l'username existe déjà
        if (userDao.usernameExists(username)) {
            System.out.println("❌ Nom d'utilisateur déjà pris: " + username);
            return false;
        }

        // Créer le nouvel utilisateur avec le rôle USER
        User newUser = new User(username, password, "USER", nom, email);
        boolean success = userDao.create(newUser);

        if (success) {
            System.out.println("✅ Inscription réussie: " + username);
        }
        return success;
    }

    /**
     * Ajouter un nouvel utilisateur (admin seulement)
     */
    public boolean addUser(String username, String password, String role, String nom, String email) {
        if (!isAdmin()) {
            System.out.println("❌ Seul l'admin peut ajouter des utilisateurs");
            return false;
        }
        if (userDao.usernameExists(username)) {
            System.out.println("❌ Utilisateur existe déjà: " + username);
            return false;
        }
        User newUser = new User(username, password, role, nom, email);
        return userDao.create(newUser);
    }

    /**
     * Supprimer un utilisateur (admin seulement)
     */
    public boolean removeUser(String username) {
        if (!isAdmin()) {
            return false;
        }
        if ("admin".equals(username)) {
            System.out.println("❌ Impossible de supprimer l'admin principal");
            return false;
        }
        return userDao.delete(username);
    }

    /**
     * Obtenir tous les utilisateurs
     */
    public Map<String, User> getAllUsers() {
        Map<String, User> userMap = new HashMap<>();
        List<User> users = userDao.findAll();
        for (User u : users) {
            userMap.put(u.getUsername(), u);
        }
        return userMap;
    }

    /**
     * Changer le mot de passe
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (currentUser != null && currentUser.getPassword().equals(oldPassword)) {
            currentUser.setPassword(newPassword);
            return userDao.update(currentUser);
        }
        return false;
    }

    /**
     * Vérifier si un nom d'utilisateur est disponible
     */
    public boolean isUsernameAvailable(String username) {
        return !userDao.usernameExists(username);
    }
}

