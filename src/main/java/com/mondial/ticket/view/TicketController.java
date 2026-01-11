package com.mondial.ticket.view;

import com.mondial.ticket.dao.MatchDaoHibernate;
import com.mondial.ticket.dao.TicketDaoHibernate;
import com.mondial.ticket.exception.TicketException;
import com.mondial.ticket.model.Match;
import com.mondial.ticket.model.PurchasedTicket;
import com.mondial.ticket.model.Ticket;
import com.mondial.ticket.service.AuthService;
import com.mondial.ticket.service.MatchService;
import com.mondial.ticket.service.TicketService;
import com.mondial.ticket.service.UserTicketService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur pour la gestion des tickets dans l'interface JavaFX.
 */
public class TicketController {
    @FXML
    private ComboBox<String> adminMatchCombo;
    @FXML
    private ComboBox<String> categorieCombo;
    @FXML
    private ComboBox<String> matchCombo;
    @FXML
    private ComboBox<String> userCategorieCombo;
    @FXML
    private TextField prixField;
    @FXML
    private TextField searchField;
    @FXML
    private TextField acheteurField;
    @FXML
    private TableView<Ticket> ticketTable;
    @FXML
    private TableColumn<Ticket, String> colNomMatch;
    @FXML
    private TableColumn<Ticket, String> colCategorie;
    @FXML
    private TableColumn<Ticket, Double> colPrix;
    @FXML
    private TableColumn<Ticket, String> colStatus;
    @FXML
    private TableColumn<Ticket, String> colAcheteur;
    @FXML
    private Label statusLabel;
    @FXML
    private Label acheteurLabel;
    @FXML
    private Button addTicketBtn;
    @FXML
    private Button deleteTicketBtn;
    @FXML
    private Button exportAllBtn;
    @FXML
    private Button statsBtn;
    @FXML
    private HBox adminAddSection;

    private TicketService ticketService = new TicketService(new TicketDaoHibernate());
    private MatchService matchService = new MatchService(new MatchDaoHibernate());
    private AuthService authService = AuthService.getInstance();
    private ObservableList<Ticket> ticketList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configuration des colonnes
        colNomMatch.setCellValueFactory(new PropertyValueFactory<>("nomMatch"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colAcheteur.setCellValueFactory(new PropertyValueFactory<>("acheteur"));

        // Restrict admin-only features
        setupAdminRestrictions();

        // Style pour la colonne statut
        colStatus.setCellFactory(column -> new TableCell<Ticket, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    if ("DISPONIBLE".equals(status)) {
                        setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    } else if ("VENDU".equals(status)) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
                    }
                }
            }
        });

        // Double-click on ticket to buy it quickly
        ticketTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Ticket selected = ticketTable.getSelectionModel().getSelectedItem();
                if (selected != null && "DISPONIBLE".equals(selected.getStatus())) {
                    handleQuickBuy(selected);
                }
            }
        });

        // When selecting a ticket, auto-fill the match combo
        ticketTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && matchCombo != null) {
                matchCombo.setValue(newVal.getNomMatch());
                if (userCategorieCombo != null) {
                    userCategorieCombo.setValue(newVal.getCategorie());
                }
            }
        });

        // Configuration du ComboBox des catégories (admin)
        if (categorieCombo != null) {
            categorieCombo.setItems(FXCollections.observableArrayList("VIP", "Standard", "Tribune"));
            categorieCombo.setValue("Standard");
        }

        // Charger les matchs existants dans le combo box admin
        loadAdminMatchCombo();

        // Configuration du ComboBox des catégories (user)
        if (userCategorieCombo != null) {
            userCategorieCombo.setItems(FXCollections.observableArrayList("Tous", "VIP", "Standard", "Tribune"));
            userCategorieCombo.setValue("Tous");

            // When category changes, filter tickets
            userCategorieCombo.setOnAction(e -> filterTicketsByMatchAndCategory());
        }

        // Charger les matchs dans le combo box pour les utilisateurs
        loadMatchesIntoCombo();

        // Add listener to filter tickets when match is selected
        if (matchCombo != null) {
            matchCombo.setOnAction(e -> filterTicketsByMatchAndCategory());
        }

        // Add dynamic search - filter as you type
        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                performDynamicSearch(newValue);
            });
        }

        // Show logged-in user's name
        if (acheteurLabel != null && authService.getCurrentUser() != null) {
            String nom = authService.getCurrentUser().getNom();
            if (nom == null || nom.isEmpty()) {
                nom = authService.getCurrentUser().getUsername();
            }
            acheteurLabel.setText("(Connecte en tant que: " + nom + ")");
        }

        handleRefresh();
        updateStatus("Bienvenue! " + ticketList.size() + " tickets disponibles.");
    }

    /**
     * Perform dynamic search as user types
     */
    private void performDynamicSearch(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            // Show all tickets when search is empty
            ticketList.setAll(ticketService.recupererTout());
            ticketTable.setItems(ticketList);
            updateStatus("🎫 " + ticketList.size() + " tickets affichés");
            return;
        }

        String term = searchTerm.toLowerCase().trim();
        List<Ticket> filtered = ticketService.recupererTout().stream()
                .filter(t -> t.getNomMatch().toLowerCase().contains(term)
                        || t.getCategorie().toLowerCase().contains(term)
                        || (t.getStatus() != null && t.getStatus().toLowerCase().contains(term))
                        || (t.getAcheteur() != null && t.getAcheteur().toLowerCase().contains(term))
                        || String.valueOf(t.getPrix()).contains(term))
                .collect(Collectors.toList());

        ticketList.setAll(filtered);
        ticketTable.setItems(ticketList);
        updateStatus("🔍 " + filtered.size() + " résultat(s) pour '" + searchTerm + "'");
    }

    /**
     * Filtrer les tickets par match et catégorie sélectionnés
     */
    private void filterTicketsByMatchAndCategory() {
        String selectedMatch = (matchCombo != null) ? matchCombo.getValue() : null;
        String selectedCategory = (userCategorieCombo != null) ? userCategorieCombo.getValue() : null;

        if (selectedMatch == null || selectedMatch.isEmpty()) {
            // Show all tickets if no match selected
            handleRefresh();
            return;
        }

        List<Ticket> allTickets = ticketService.recupererTout();
        List<Ticket> filtered;

        // Check if category is "Tous" or null - show all categories for this match
        if (selectedCategory == null || selectedCategory.isEmpty() || "Tous".equals(selectedCategory)) {
            // Filter by match only
            filtered = allTickets.stream()
                .filter(t -> t.getNomMatch().equals(selectedMatch))
                .collect(Collectors.toList());
        } else {
            // Filter by both match and category
            filtered = allTickets.stream()
                .filter(t -> t.getNomMatch().equals(selectedMatch) && t.getCategorie().equals(selectedCategory))
                .collect(Collectors.toList());
        }

        ticketList.setAll(filtered);
        ticketTable.setItems(ticketList);

        long available = filtered.stream().filter(t -> "DISPONIBLE".equals(t.getStatus())).count();
        updateStatus("🎫 " + selectedMatch + ": " + available + " ticket(s) disponible(s) sur " + filtered.size());
    }

    /**
     * Charger les matchs disponibles dans le combo box (depuis la table Match)
     */
    private void loadMatchesIntoCombo() {
        if (matchCombo != null) {
            // Get match names from Match table (created by admin)
            List<String> matchNames = matchService.listerMatchs().stream()
                .map(Match::getNomComplet)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

            String currentSelection = matchCombo.getValue();
            matchCombo.setItems(FXCollections.observableArrayList(matchNames));

            // Restore selection if it still exists
            if (currentSelection != null && matchNames.contains(currentSelection)) {
                matchCombo.setValue(currentSelection);
            }
            // Don't auto-select first match - let user choose
        }
    }

    /**
     * Charger les matchs existants dans le combo box admin (pour ajouter des tickets)
     */
    private void loadAdminMatchCombo() {
        if (adminMatchCombo != null) {
            // Get match names from Match table (created by admin)
            List<String> matchNames = matchService.listerMatchs().stream()
                .map(Match::getNomComplet)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

            String currentSelection = adminMatchCombo.getValue();
            adminMatchCombo.setItems(FXCollections.observableArrayList(matchNames));

            // Restore selection if it still exists
            if (currentSelection != null && matchNames.contains(currentSelection)) {
                adminMatchCombo.setValue(currentSelection);
            }
        }
    }

    /**
     * Configure les restrictions selon le rôle de l'utilisateur
     */
    private void setupAdminRestrictions() {
        boolean isAdmin = authService.isAdmin();

        // Masquer la section d'ajout complète pour les utilisateurs normaux
        if (adminAddSection != null) {
            adminAddSection.setVisible(isAdmin);
            adminAddSection.setManaged(isAdmin);
        }

        // Masquer le bouton supprimer pour les utilisateurs normaux
        if (deleteTicketBtn != null) {
            deleteTicketBtn.setVisible(isAdmin);
            deleteTicketBtn.setManaged(isAdmin);
        }

        // Masquer le bouton exporter tous pour les utilisateurs normaux
        if (exportAllBtn != null) {
            exportAllBtn.setVisible(isAdmin);
            exportAllBtn.setManaged(isAdmin);
        }

        // Masquer le bouton statistiques pour les utilisateurs normaux
        if (statsBtn != null) {
            statsBtn.setVisible(isAdmin);
            statsBtn.setManaged(isAdmin);
        }
    }

    @FXML
    public void handleAjouterTicket() {
        // Admin only
        if (!authService.isAdmin()) {
            showAlert("Accès refusé", "Seul l'administrateur peut ajouter des tickets.");
            return;
        }

        try {
            String nomMatch = adminMatchCombo.getValue();
            String categorie = categorieCombo.getValue();
            double prix = Double.parseDouble(prixField.getText());

            if (nomMatch == null || nomMatch.isEmpty()) {
                showAlert("Erreur", "Veuillez sélectionner un match existant.");
                return;
            }

            Ticket ticket = new Ticket(nomMatch, categorie, prix);
            ticketService.enregistrerTicket(ticket);
            handleRefresh();

            // Clear fields
            prixField.clear();
            updateStatus("✅ Ticket ajouté avec succès: " + nomMatch + " [" + categorie + "]");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Prix invalide. Veuillez entrer un nombre.");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    @FXML
    public void handleRefresh() {
        ticketList.setAll(ticketService.recupererTout());
        ticketTable.setItems(ticketList);
        loadMatchesIntoCombo();
        loadAdminMatchCombo();
        updateStatus("🔄 Liste actualisée - " + ticketList.size() + " tickets");
    }

    @FXML
    public void handleShowAll() {
        // Reset category to "Tous" and show all tickets
        if (userCategorieCombo != null) {
            userCategorieCombo.setValue("Tous");
        }
        if (matchCombo != null) {
            matchCombo.setValue(null);
        }
        handleRefresh();
    }

    @FXML
    public void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase().trim();
        if (searchTerm.isEmpty()) {
            handleRefresh();
            return;
        }

        List<Ticket> filtered = ticketService.recupererTout().stream()
                .filter(t -> t.getNomMatch().toLowerCase().contains(searchTerm)
                        || t.getCategorie().toLowerCase().contains(searchTerm)
                        || (t.getStatus() != null && t.getStatus().toLowerCase().contains(searchTerm)))
                .collect(Collectors.toList());

        ticketList.setAll(filtered);
        ticketTable.setItems(ticketList);
        updateStatus("🔍 " + filtered.size() + " résultat(s) pour '" + searchTerm + "'");
    }

    @FXML
    public void handleBuySelectedTicket() {
        Ticket selected = ticketTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attention", "Veuillez sélectionner un ticket dans la liste.");
            return;
        }

        if (!"DISPONIBLE".equals(selected.getStatus())) {
            showAlert("Erreur", "Ce ticket n'est plus disponible!");
            return;
        }

        // Use logged-in user's info
        if (authService.getCurrentUser() == null) {
            showAlert("Erreur", "Vous devez être connecté pour acheter un ticket.");
            return;
        }

        String acheteur = authService.getCurrentUser().getNom();
        if (acheteur == null || acheteur.isEmpty()) {
            acheteur = authService.getCurrentUser().getUsername();
        }

        // Buy the selected ticket
        buyTicket(selected, acheteur);

        // Refresh the filtered view
        filterTicketsByMatchAndCategory();
    }

    @FXML
    public void handleAcheterTicket() {
        String acheteur = acheteurField.getText().trim();
        if (acheteur.isEmpty()) {
            showAlert("Attention", "Veuillez entrer votre nom.");
            return;
        }

        // Try to get match from combo box first (user flow)
        if (matchCombo != null && matchCombo.getValue() != null && !matchCombo.getValue().isEmpty()) {
            final String selectedMatch = matchCombo.getValue();
            final String selectedCategorie = (userCategorieCombo != null && userCategorieCombo.getValue() != null)
                ? userCategorieCombo.getValue() : "Standard";

            // Find an available ticket for this match and category
            List<Ticket> availableTickets = ticketService.recupererTout().stream()
                .filter(t -> t.getNomMatch().equals(selectedMatch)
                        && t.getCategorie().equals(selectedCategorie)
                        && "DISPONIBLE".equals(t.getStatus()))
                .collect(Collectors.toList());

            if (availableTickets.isEmpty()) {
                showAlert("Désolé", "Aucun ticket disponible pour:\n" + selectedMatch + " (" + selectedCategorie + ")");
                return;
            }

            // Buy the first available ticket
            Ticket ticketToBuy = availableTickets.get(0);
            buyTicket(ticketToBuy, acheteur);
            return;
        }

        // Fallback: try to get from table selection
        Ticket selected = ticketTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attention", "Veuillez sélectionner un match ou un ticket.");
            return;
        }

        if ("VENDU".equals(selected.getStatus())) {
            showAlert("Erreur", "Ce ticket est déjà vendu!");
            return;
        }

        try {
            ticketService.acheterTicket(selected.getNomMatch(), acheteur);

            // Record purchase for user
            UserTicketService userTicketService = UserTicketService.getInstance();
            PurchasedTicket purchase = userTicketService.recordPurchase(acheteur, selected);

            handleRefresh();
            acheteurField.clear();

            // Show confirmation with QR code
            showInfo("🎫 Achat confirmé!",
                    "Félicitations " + acheteur + "!\n\n" +
                    "Vous avez acheté:\n" +
                    "Match: " + selected.getNomMatch() + "\n" +
                    "Catégorie: " + selected.getCategorie() + "\n" +
                    "Prix: " + selected.getPrix() + "€\n\n" +
                    "🎟️ ID Ticket: " + purchase.getTicketId() + "\n" +
                    "📱 Code QR: " + purchase.getQrCode() + "\n\n" +
                    "Merci pour votre achat! 🏆");

            updateStatus("🎫 Ticket vendu à " + acheteur);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'achat: " + e.getMessage());
        }
    }

    /**
     * Acheter un ticket spécifique
     */
    private void buyTicket(Ticket ticket, String acheteur) {
        try {
            ticketService.acheterTicket(ticket.getNomMatch(), acheteur);

            // Record purchase for the logged-in user (not the buyer name)
            UserTicketService userTicketService = UserTicketService.getInstance();
            String username = authService.getCurrentUser() != null ?
                authService.getCurrentUser().getUsername() : acheteur;
            PurchasedTicket purchase = userTicketService.recordPurchase(username, ticket);

            handleRefresh();
            loadMatchesIntoCombo(); // Refresh matches

            // Show confirmation with QR code
            showInfo("Achat confirme!",
                    "Felicitations " + acheteur + "!\n\n" +
                    "Vous avez achete:\n" +
                    "Match: " + ticket.getNomMatch() + "\n" +
                    "Categorie: " + ticket.getCategorie() + "\n" +
                    "Prix: " + ticket.getPrix() + " EUR\n\n" +
                    "ID Ticket: " + purchase.getTicketId() + "\n" +
                    "Code QR: " + purchase.getQrCode() + "\n\n" +
                    "Consultez 'Mon Compte - Mes Tickets' pour voir vos achats.\n\n" +
                    "Merci pour votre achat!");

            updateStatus("Ticket vendu a " + acheteur);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'achat: " + e.getMessage());
        }
    }

    /**
     * Achat rapide par double-clic sur un ticket
     */
    private void handleQuickBuy(Ticket ticket) {
        // Use logged-in user's info
        if (authService.getCurrentUser() == null) {
            showAlert("Erreur", "Vous devez être connecté pour acheter un ticket.");
            return;
        }

        String acheteur = authService.getCurrentUser().getNom();
        if (acheteur == null || acheteur.isEmpty()) {
            acheteur = authService.getCurrentUser().getUsername();
        }

        // Show confirmation dialog
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmer l'achat");
        confirm.setHeaderText("Acheter ce ticket?");
        confirm.setContentText(
            "Match: " + ticket.getNomMatch() + "\n" +
            "Catégorie: " + ticket.getCategorie() + "\n" +
            "Prix: " + ticket.getPrix() + "€\n\n" +
            "Acheteur: " + acheteur
        );

        java.util.Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            buyTicket(ticket, acheteur);
        }
    }

    @FXML
    public void handleSupprimerTicket() {
        // Admin only
        if (!authService.isAdmin()) {
            showAlert("Accès refusé", "Seul l'administrateur peut supprimer des tickets.");
            return;
        }

        Ticket selected = ticketTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                ticketService.retirerTicket(selected.getNomMatch());
                handleRefresh();
                updateStatus("🗑️ Ticket supprimé: " + selected.getNomMatch());
            } catch (Exception e) {
                showAlert("Erreur", "Erreur suppression: " + e.getMessage());
            }
        } else {
            showAlert("Attention", "Veuillez sélectionner un ticket à supprimer.");
        }
    }

    @FXML
    public void handleStatistiques() {
        ticketService.afficherStatistiques();

        // Show in dialog
        List<Ticket> tickets = ticketService.recupererTout();
        double total = tickets.stream().mapToDouble(Ticket::getPrix).sum();
        long vendus = tickets.stream().filter(t -> "VENDU".equals(t.getStatus())).count();
        long disponibles = tickets.stream().filter(t -> "DISPONIBLE".equals(t.getStatus())).count();

        String stats = String.format(
                "📊 STATISTIQUES MONDIAL 2030\n\n" +
                "Total tickets: %d\n" +
                "Tickets vendus: %d\n" +
                "Tickets disponibles: %d\n" +
                "Valeur totale: %.2f€\n" +
                "Revenu (vendus): %.2f€",
                tickets.size(), vendus, disponibles, total,
                tickets.stream().filter(t -> "VENDU".equals(t.getStatus())).mapToDouble(Ticket::getPrix).sum()
        );

        showInfo("Statistiques", stats);
    }

    @FXML
    public void handleExportReceipt() {
        Ticket selected = ticketTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attention", "Veuillez sélectionner un ticket.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder le reçu");
        fileChooser.setInitialFileName("recu_" + selected.getNomMatch().replace(" ", "_") + ".txt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier texte", "*.txt"));

        File file = fileChooser.showSaveDialog(ticketTable.getScene().getWindow());
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("═══════════════════════════════════════════");
                writer.println("        🏆 MONDIAL 2030 - REÇU TICKET 🏆    ");
                writer.println("═══════════════════════════════════════════");
                writer.println();
                writer.println("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                writer.println();
                writer.println("Match: " + selected.getNomMatch());
                writer.println("Catégorie: " + selected.getCategorie());
                writer.println("Prix: " + selected.getPrix() + "€");
                writer.println("Statut: " + (selected.getStatus() != null ? selected.getStatus() : "DISPONIBLE"));
                if (selected.getAcheteur() != null && !selected.getAcheteur().isEmpty()) {
                    writer.println("Acheteur: " + selected.getAcheteur());
                }
                writer.println();
                writer.println("═══════════════════════════════════════════");
                writer.println("   Merci d'avoir choisi le Mondial 2030!   ");
                writer.println("═══════════════════════════════════════════");

                updateStatus("📄 Reçu exporté: " + file.getName());
                showInfo("Succès", "Reçu exporté avec succès!");
            } catch (Exception e) {
                showAlert("Erreur", "Erreur lors de l'export: " + e.getMessage());
            }
        }
    }

    @FXML
    public void handleExportAll() {
        // Admin only
        if (!authService.isAdmin()) {
            showAlert("Accès refusé", "Seul l'administrateur peut exporter tous les tickets.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter tous les tickets");
        fileChooser.setInitialFileName("tickets_mondial2030_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")) + ".csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier CSV", "*.csv"));

        File file = fileChooser.showSaveDialog(ticketTable.getScene().getWindow());
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                // Header
                writer.println("Match,Catégorie,Prix,Statut,Acheteur");

                // Data
                for (Ticket t : ticketService.recupererTout()) {
                    writer.printf("%s,%s,%.2f,%s,%s%n",
                            t.getNomMatch(),
                            t.getCategorie(),
                            t.getPrix(),
                            t.getStatus() != null ? t.getStatus() : "DISPONIBLE",
                            t.getAcheteur() != null ? t.getAcheteur() : ""
                    );
                }

                updateStatus("📁 Export CSV: " + file.getName());
                showInfo("Succès", "Tous les tickets ont été exportés!");
            } catch (Exception e) {
                showAlert("Erreur", "Erreur lors de l'export: " + e.getMessage());
            }
        }
    }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

