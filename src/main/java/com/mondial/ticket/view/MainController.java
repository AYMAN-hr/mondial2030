package com.mondial.ticket.view;

import com.mondial.ticket.dao.TicketDaoHibernate;
import com.mondial.ticket.model.Ticket;
import com.mondial.ticket.model.User;
import com.mondial.ticket.service.AuthService;
import com.mondial.ticket.service.LanguageService;
import com.mondial.ticket.service.NotificationService;
import com.mondial.ticket.service.TicketService;
import com.mondial.ticket.service.UserTicketService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Contrôleur principal de l'interface JavaFX.
 */
public class MainController {

    @FXML private CheckMenuItem darkModeToggle;
    @FXML private VBox mainContainer;
    @FXML private Label clockLabel;
    @FXML private Label totalTicketsLabel;
    @FXML private Label soldTicketsLabel;
    @FXML private Label availableTicketsLabel;
    @FXML private Label revenueLabel;
    @FXML private Label notificationLabel;
    @FXML private Label userLabel;
    @FXML private Menu adminMenu;
    @FXML private MenuItem exportDataMenuItem;
    @FXML private MenuItem promoCodesMenuItem;

    private TicketService ticketService = new TicketService(new TicketDaoHibernate());
    private AuthService authService = AuthService.getInstance();
    private Map<String, Double> promoCodes = new HashMap<>();
    private boolean isDarkMode = false;

    @FXML
    public void initialize() {
        // Initialize promo codes
        promoCodes.put("MONDIAL2030", 20.0);  // 20% off
        promoCodes.put("VIP50", 50.0);         // 50% off
        promoCodes.put("MAROC10", 10.0);       // 10% off

        // Setup user info
        setupUserAccess();

        // Initialize mock data if empty
        initializeMockDataIfNeeded();

        // Start clock
        startClock();

        // Refresh stats
        handleRefreshStats();

        // Welcome notification
        User user = authService.getCurrentUser();
        if (user != null) {
            showNotification("✨ Bienvenue " + user.getNom() + "!");
        } else {
            showNotification("✨ Bienvenue sur la plateforme Mondial 2030!");
        }
    }

    /**
     * Initialize mock data if database is empty
     */
    private void initializeMockDataIfNeeded() {
        if (ticketService.recupererTout().isEmpty()) {
            System.out.println("🔄 Base de données vide, création des données de test...");
            ticketService.creerDonneesTest();
        }
    }

    private void setupUserAccess() {
        User user = authService.getCurrentUser();

        // Update user label
        if (userLabel != null && user != null) {
            String roleIcon = user.isAdmin() ? "👑" : "👤";
            userLabel.setText(roleIcon + " " + user.getNom());
        }

        boolean isAdmin = authService.isAdmin();

        // Show/hide admin menu based on role
        if (adminMenu != null) {
            adminMenu.setVisible(isAdmin);
        }

        // Hide export data for non-admin
        if (exportDataMenuItem != null) {
            exportDataMenuItem.setVisible(isAdmin);
        }

        // Hide promo codes management for non-admin
        if (promoCodesMenuItem != null) {
            promoCodesMenuItem.setVisible(isAdmin);
        }
    }

    @FXML
    public void handleLogout() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Déconnexion");
        confirm.setHeaderText("Voulez-vous vous déconnecter?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            authService.logout();
            loadLoginScreen();
        }
    }

    private void loadLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) mainContainer.getScene().getWindow();
            Scene scene = new Scene(root, 500, 650);
            stage.setScene(scene);
            stage.setTitle("🏆 Mondial 2030 - Connexion");
            stage.setResizable(false);
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleManageUsers() {
        if (!authService.isAdmin()) {
            showAlert("Accès refusé", "Seul l'administrateur peut gérer les utilisateurs.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("👥 Gestion des Utilisateurs");
        dialog.setHeaderText("Gérer les comptes utilisateurs");

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        // List users
        ListView<String> usersList = new ListView<>();
        for (Map.Entry<String, User> entry : authService.getAllUsers().entrySet()) {
            User u = entry.getValue();
            String icon = u.isAdmin() ? "👑" : "👤";
            usersList.getItems().add(icon + " " + u.getUsername() + " - " + u.getNom() + " (" + u.getRole() + ")");
        }

        // Add user form
        GridPane addForm = new GridPane();
        addForm.setHgap(10);
        addForm.setVgap(10);

        TextField newUsername = new TextField();
        newUsername.setPromptText("Identifiant");
        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("Mot de passe");
        TextField newNom = new TextField();
        newNom.setPromptText("Nom complet");
        TextField newEmail = new TextField();
        newEmail.setPromptText("Email");
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("USER", "ADMIN");
        roleCombo.setValue("USER");

        addForm.add(new Label("Identifiant:"), 0, 0);
        addForm.add(newUsername, 1, 0);
        addForm.add(new Label("Mot de passe:"), 0, 1);
        addForm.add(newPassword, 1, 1);
        addForm.add(new Label("Nom:"), 0, 2);
        addForm.add(newNom, 1, 2);
        addForm.add(new Label("Email:"), 0, 3);
        addForm.add(newEmail, 1, 3);
        addForm.add(new Label("Rôle:"), 0, 4);
        addForm.add(roleCombo, 1, 4);

        Button addBtn = new Button("�� Ajouter utilisateur");
        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addBtn.setOnAction(e -> {
            if (authService.addUser(newUsername.getText(), newPassword.getText(),
                    roleCombo.getValue(), newNom.getText(), newEmail.getText())) {
                User u = authService.getAllUsers().get(newUsername.getText());
                String icon = u.isAdmin() ? "👑" : "👤";
                usersList.getItems().add(icon + " " + u.getUsername() + " - " + u.getNom() + " (" + u.getRole() + ")");
                newUsername.clear();
                newPassword.clear();
                newNom.clear();
                newEmail.clear();
                showNotification("✅ Utilisateur " + u.getUsername() + " créé!");
            } else {
                showAlert("Erreur", "Impossible de créer l'utilisateur.");
            }
        });

        content.getChildren().addAll(
            new Label("Utilisateurs existants:"),
            usersList,
            new Separator(),
            new Label("Ajouter un nouvel utilisateur:"),
            addForm,
            addBtn
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.getDialogPane().setPrefWidth(500);
        dialog.showAndWait();
    }

    @FXML
    public void handleResetMockData() {
        if (!authService.isAdmin()) {
            showAlert("Accès refusé", "Seul l'administrateur peut réinitialiser les données.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("🔄 Réinitialiser les données");
        confirm.setHeaderText("Voulez-vous supprimer tous les tickets et créer de nouvelles données de test?");
        confirm.setContentText("Cette action est irréversible!");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ticketService.creerDonneesTest();
            handleRefreshStats();
            showNotification("✅ Données de test réinitialisées!");

            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Succès");
            info.setHeaderText("Données réinitialisées");
            info.setContentText("54 nouveaux tickets ont été créés pour 6 matchs différents.\n\nActualisez l'onglet Tickets pour voir les changements.");
            info.showAndWait();
        }
    }

    @FXML
    public void handleChangePassword() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("🔐 Changer le mot de passe");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        PasswordField oldPass = new PasswordField();
        PasswordField newPass = new PasswordField();
        PasswordField confirmPass = new PasswordField();

        grid.add(new Label("Ancien mot de passe:"), 0, 0);
        grid.add(oldPass, 1, 0);
        grid.add(new Label("Nouveau mot de passe:"), 0, 1);
        grid.add(newPass, 1, 1);
        grid.add(new Label("Confirmer:"), 0, 2);
        grid.add(confirmPass, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (!newPass.getText().equals(confirmPass.getText())) {
                showAlert("Erreur", "Les mots de passe ne correspondent pas.");
                return;
            }
            if (authService.changePassword(oldPass.getText(), newPass.getText())) {
                showNotification("✅ Mot de passe changé avec succès!");
            } else {
                showAlert("Erreur", "Ancien mot de passe incorrect.");
            }
        }
    }

    private void startClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (clockLabel != null) {
                clockLabel.setText("🕐 " + LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            }
        }));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    @FXML
    public void handleRefreshStats() {
        try {
            List<Ticket> tickets = ticketService.recupererTout();
            long total = tickets.size();
            long sold = tickets.stream().filter(t -> "VENDU".equals(t.getStatus())).count();
            long available = total - sold;
            double revenue = tickets.stream()
                    .filter(t -> "VENDU".equals(t.getStatus()))
                    .mapToDouble(Ticket::getPrix)
                    .sum();

            if (totalTicketsLabel != null) totalTicketsLabel.setText("📊 Total: " + total);
            if (soldTicketsLabel != null) soldTicketsLabel.setText("🎫 Vendus: " + sold);
            if (availableTicketsLabel != null) availableTicketsLabel.setText("✅ Disponibles: " + available);
            if (revenueLabel != null) revenueLabel.setText(String.format("💰 Revenus: %.2f€", revenue));
        } catch (Exception e) {
            System.err.println("Erreur refresh stats: " + e.getMessage());
        }
    }

    @FXML
    public void handleLottery() {
        List<Ticket> availableTickets = ticketService.recupererTout().stream()
                .filter(t -> !"VENDU".equals(t.getStatus()))
                .collect(Collectors.toList());

        if (availableTickets.isEmpty()) {
            showAlert("Tirage au sort", "Aucun ticket disponible pour le tirage!");
            return;
        }

        // Create lottery dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("🎲 Tirage au Sort - Mondial 2030");
        dialog.setHeaderText("Gagnez un ticket gratuit!");

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        TextField nameField = new TextField();
        nameField.setPromptText("Entrez votre nom");

        TextField emailField = new TextField();
        emailField.setPromptText("Entrez votre email");

        content.getChildren().addAll(
            new Label("Participez au tirage pour gagner un ticket gratuit!"),
            new Label("Nom:"), nameField,
            new Label("Email:"), emailField
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                showAlert("Erreur", "Veuillez entrer votre nom!");
                return;
            }

            // Random draw animation simulation
            Random random = new Random();

            // 30% chance to win
            if (random.nextInt(100) < 30) {
                Ticket wonTicket = availableTickets.get(random.nextInt(availableTickets.size()));

                Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
                winAlert.setTitle("🎉 FÉLICITATIONS!");
                winAlert.setHeaderText("VOUS AVEZ GAGNÉ!");
                winAlert.setContentText(
                    "Bravo " + name + "! 🎊\n\n" +
                    "Vous avez gagné un ticket pour:\n" +
                    "🏟️ " + wonTicket.getNomMatch() + "\n" +
                    "🎫 Catégorie: " + wonTicket.getCategorie() + "\n" +
                    "💰 Valeur: " + wonTicket.getPrix() + "€\n\n" +
                    "Un email de confirmation sera envoyé!"
                );
                winAlert.showAndWait();

                showNotification("🎉 " + name + " a gagné un ticket!");

                // Mark ticket as won and save to user's tickets
                try {
                    ticketService.acheterTicket(wonTicket.getNomMatch(), name + " (GAGNANT TIRAGE)");

                    // Save to user's purchased tickets
                    User currentUser = authService.getCurrentUser();
                    if (currentUser != null) {
                        UserTicketService userTicketService = UserTicketService.getInstance();
                        userTicketService.recordLotteryWin(
                            currentUser.getUsername(),
                            wonTicket.getNomMatch(),
                            wonTicket.getCategorie(),
                            wonTicket.getPrix()
                        );
                    }

                    handleRefreshStats();
                } catch (Exception e) {
                    System.err.println("Erreur tirage: " + e.getMessage());
                }
            } else {
                Alert loseAlert = new Alert(Alert.AlertType.INFORMATION);
                loseAlert.setTitle("Tirage au Sort");
                loseAlert.setHeaderText("Pas de chance cette fois...");
                loseAlert.setContentText(
                    "Désolé " + name + ", vous n'avez pas gagné cette fois.\n\n" +
                    "Tentez à nouveau votre chance ou achetez un ticket!\n" +
                    "Utilisez le code MONDIAL2030 pour -20%!"
                );
                loseAlert.showAndWait();
            }
        }
    }

    @FXML
    public void handlePaymentSimulation() {
        List<Ticket> availableTickets = ticketService.recupererTout().stream()
                .filter(t -> !"VENDU".equals(t.getStatus()))
                .collect(Collectors.toList());

        if (availableTickets.isEmpty()) {
            showAlert("Paiement", "Aucun ticket disponible à l'achat!");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("💳 Simulation de Paiement");
        dialog.setHeaderText("Achat sécurisé de ticket");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<String> ticketCombo = new ComboBox<>();
        for (Ticket t : availableTickets) {
            ticketCombo.getItems().add(t.getNomMatch() + " - " + t.getCategorie() + " (" + t.getPrix() + "€)");
        }
        ticketCombo.getSelectionModel().selectFirst();

        TextField nameField = new TextField();
        nameField.setPromptText("Nom complet");

        TextField cardField = new TextField();
        cardField.setPromptText("**** **** **** ****");

        TextField promoField = new TextField();
        promoField.setPromptText("Code promo (optionnel)");

        Label discountLabel = new Label("");
        discountLabel.setStyle("-fx-text-fill: green;");

        promoField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (promoCodes.containsKey(newVal.toUpperCase())) {
                discountLabel.setText("✅ Code valide! -" + promoCodes.get(newVal.toUpperCase()) + "%");
            } else if (!newVal.isEmpty()) {
                discountLabel.setText("❌ Code invalide");
                discountLabel.setStyle("-fx-text-fill: red;");
            } else {
                discountLabel.setText("");
            }
        });

        grid.add(new Label("Ticket:"), 0, 0);
        grid.add(ticketCombo, 1, 0);
        grid.add(new Label("Nom:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Carte:"), 0, 2);
        grid.add(cardField, 1, 2);
        grid.add(new Label("Code promo:"), 0, 3);
        grid.add(promoField, 1, 3);
        grid.add(discountLabel, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(
            new ButtonType("💳 Payer", ButtonBar.ButtonData.OK_DONE),
            ButtonType.CANCEL
        );

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            String name = nameField.getText().trim();
            String card = cardField.getText().trim();
            String promo = promoField.getText().trim().toUpperCase();
            int selectedIndex = ticketCombo.getSelectionModel().getSelectedIndex();

            if (name.isEmpty() || card.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs!");
                return;
            }

            Ticket selected = availableTickets.get(selectedIndex);
            double calculatedPrice = selected.getPrix();
            if (promoCodes.containsKey(promo)) {
                double discount = promoCodes.get(promo);
                calculatedPrice = calculatedPrice * (1 - discount / 100);
            }
            final double finalPrice = calculatedPrice;

            // Simulate payment processing
            Alert processing = new Alert(Alert.AlertType.INFORMATION);
            processing.setTitle("Traitement...");
            processing.setHeaderText("💳 Paiement en cours...");
            processing.setContentText("Veuillez patienter...");
            processing.show();

            // Simulate delay
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}

                Platform.runLater(() -> {
                    processing.close();

                    try {
                        ticketService.acheterTicket(selected.getNomMatch(), name);
                        handleRefreshStats();

                        Alert success = new Alert(Alert.AlertType.INFORMATION);
                        success.setTitle("✅ Paiement Réussi!");
                        success.setHeaderText("Votre achat est confirmé!");
                        success.setContentText(
                            "🎫 " + selected.getNomMatch() + "\n" +
                            "📁 Catégorie: " + selected.getCategorie() + "\n" +
                            "💰 Prix payé: " + String.format("%.2f€", finalPrice) + "\n\n" +
                            "Un email de confirmation a été envoyé!\n" +
                            "Numéro de transaction: TRX" + System.currentTimeMillis()
                        );
                        success.showAndWait();

                        showNotification("💳 Paiement reçu de " + name);
                    } catch (Exception e) {
                        showAlert("Erreur", "Erreur de paiement: " + e.getMessage());
                    }
                });
            }).start();
        }
    }

    @FXML
    public void handlePromoCodes() {
        // Admin only
        if (!authService.isAdmin()) {
            showAlert("Accès refusé", "Seul l'administrateur peut gérer les codes promo.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("🎁 Gestion des Codes Promo");
        dialog.setHeaderText("Codes promotionnels actifs");

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        // List existing codes
        ListView<String> codesList = new ListView<>();
        for (Map.Entry<String, Double> entry : promoCodes.entrySet()) {
            codesList.getItems().add(entry.getKey() + " → -" + entry.getValue() + "%");
        }

        // Add new code form
        HBox addForm = new HBox(10);
        TextField codeField = new TextField();
        codeField.setPromptText("Nouveau code");
        TextField discountField = new TextField();
        discountField.setPromptText("% réduction");
        discountField.setPrefWidth(80);
        Button addBtn = new Button("➕ Ajouter");

        addBtn.setOnAction(e -> {
            try {
                String code = codeField.getText().toUpperCase().trim();
                double discount = Double.parseDouble(discountField.getText().trim());
                if (!code.isEmpty() && discount > 0 && discount <= 100) {
                    promoCodes.put(code, discount);
                    codesList.getItems().add(code + " → -" + discount + "%");
                    codeField.clear();
                    discountField.clear();
                    showNotification("🎁 Code promo " + code + " ajouté!");
                }
            } catch (NumberFormatException ex) {
                showAlert("Erreur", "Réduction invalide!");
            }
        });

        addForm.getChildren().addAll(codeField, discountField, addBtn);

        content.getChildren().addAll(
            new Label("Codes actifs:"),
            codesList,
            new Separator(),
            new Label("Ajouter un nouveau code:"),
            addForm
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    @FXML
    public void handleDarkMode() {
        isDarkMode = darkModeToggle.isSelected();

        if (mainContainer != null) {
            if (isDarkMode) {
                mainContainer.setStyle("-fx-background-color: #1E1E1E;");
                showNotification("🌙 Mode sombre activé");
            } else {
                mainContainer.setStyle("-fx-background-color: white;");
                showNotification("☀️ Mode clair activé");
            }
        }
    }

    @FXML
    public void handleExportData() {
        // Admin only
        if (!authService.isAdmin()) {
            showAlert("Accès refusé", "Seul l'administrateur peut exporter les données.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter les données");
        fileChooser.setInitialFileName("mondial2030_backup_" +
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".json");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                List<Ticket> tickets = ticketService.recupererTout();
                writer.println("{");
                writer.println("  \"exportDate\": \"" + LocalDateTime.now() + "\",");
                writer.println("  \"tickets\": [");
                for (int i = 0; i < tickets.size(); i++) {
                    Ticket t = tickets.get(i);
                    writer.printf("    {\"match\": \"%s\", \"categorie\": \"%s\", \"prix\": %.2f, \"status\": \"%s\", \"acheteur\": \"%s\"}%s%n",
                        t.getNomMatch(), t.getCategorie(), t.getPrix(),
                        t.getStatus() != null ? t.getStatus() : "DISPONIBLE",
                        t.getAcheteur() != null ? t.getAcheteur() : "",
                        i < tickets.size() - 1 ? "," : "");
                }
                writer.println("  ]");
                writer.println("}");
                showNotification("📁 Données exportées: " + file.getName());
            } catch (Exception e) {
                showAlert("Erreur", "Erreur d'export: " + e.getMessage());
            }
        }
    }

    @FXML
    public void handleImportData() {
        showAlert("Import", "Fonctionnalité d'import à venir dans la prochaine version!");
    }

    @FXML
    public void handleUserGuide() {
        Alert guide = new Alert(Alert.AlertType.INFORMATION);
        guide.setTitle("📖 Guide Utilisateur");
        guide.setHeaderText("Comment utiliser la plateforme Mondial 2030");
        guide.setContentText(
            "🎟️ TICKETS:\n" +
            "• Ajouter: Remplir le formulaire et cliquer Ajouter\n" +
            "• Acheter: Sélectionner un ticket, entrer nom, cliquer Acheter\n" +
            "• Rechercher: Utiliser la barre de recherche\n\n" +
            "⚽ MATCHS:\n" +
            "• Gérer les matchs du tournoi\n\n" +
            "🎲 TIRAGE AU SORT:\n" +
            "• Menu Outils → Tirage au sort\n" +
            "• 30% de chance de gagner!\n\n" +
            "💳 PAIEMENT:\n" +
            "• Simulation de paiement sécurisé\n" +
            "• Utilisez les codes promo!\n\n" +
            "🎁 CODES PROMO:\n" +
            "• MONDIAL2030 → -20%\n" +
            "• VIP50 → -50%\n" +
            "• MAROC10 → -10%"
        );
        guide.showAndWait();
    }

    @FXML
    public void handleExit() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Quitter");
        confirm.setHeaderText("Voulez-vous vraiment quitter?");
        confirm.setContentText("Toutes les modifications non sauvegardées seront perdues.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        }
    }

    @FXML
    public void handleAbout() {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setTitle("À propos");
        about.setHeaderText("🏆 Mondial 2030 - Gestion des Tickets");
        about.setContentText(
            "Version: 2.0\n\n" +
            "Pays hôtes:\n" +
            "🇲🇦 Maroc\n" +
            "🇪🇸 Espagne\n" +
            "🇵🇹 Portugal\n\n" +
            "Fonctionnalités:\n" +
            "• Gestion des tickets\n" +
            "• Gestion des matchs\n" +
            "• Tirage au sort\n" +
            "• Simulation de paiement\n" +
            "• Codes promo\n" +
            "• Export/Import\n\n" +
            "© 2030 FIFA World Cup"
        );
        about.showAndWait();
    }

    @FXML
    public void handleMyTickets() {
        User user = authService.getCurrentUser();
        if (user == null) return;

        UserTicketService userTicketService = UserTicketService.getInstance();
        List<UserTicketService.PurchasedTicket> myTickets = userTicketService.getUserTickets(user.getUsername());

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("🎫 Mes Tickets");
        dialog.setHeaderText("Vos tickets achetés");

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        if (myTickets.isEmpty()) {
            content.getChildren().add(new Label("Vous n'avez pas encore acheté de tickets."));
        } else {
            ListView<String> ticketList = new ListView<>();
            for (UserTicketService.PurchasedTicket t : myTickets) {
                String status = t.getStatus();
                String icon;
                switch (status) {
                    case "VALID": icon = "✅"; break;
                    case "WON": icon = "🎉"; break;
                    case "REFUNDED": icon = "🔄"; break;
                    default: icon = "❌";
                }
                String statusText = "WON".equals(status) ? " (GAGNÉ AU TIRAGE)" : "";
                ticketList.getItems().add(icon + " " + t.getMatchName() + " [" + t.getCategory() + "] - " +
                    t.getPrice() + "€" + statusText + " | QR: " + t.getQrCode() + " | " + t.getPurchaseDate());
            }

            double totalSpent = userTicketService.getTotalSpent(user.getUsername());
            Label totalLabel = new Label("💰 Total dépensé: " + String.format("%.2f€", totalSpent));
            totalLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            Label countLabel = new Label("🎫 Nombre de tickets: " + myTickets.size());
            countLabel.setStyle("-fx-font-size: 12px;");

            content.getChildren().addAll(ticketList, countLabel, totalLabel);
        }

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.getDialogPane().setPrefWidth(600);
        dialog.showAndWait();
    }

    @FXML
    public void handleFavorites() {
        UserTicketService userTicketService = UserTicketService.getInstance();
        Set<String> favorites = userTicketService.getFavorites();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("⭐ Mes Favoris");
        dialog.setHeaderText("Matchs favoris");

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        if (favorites.isEmpty()) {
            content.getChildren().add(new Label("Aucun match en favori.\n\nAstuce: Cliquez sur ⭐ à côté d'un match pour l'ajouter!"));
        } else {
            ListView<String> favList = new ListView<>();
            for (String match : favorites) {
                favList.getItems().add("⭐ " + match);
            }
            content.getChildren().add(favList);
        }

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    @FXML
    public void handleRefundRequest() {
        User user = authService.getCurrentUser();
        if (user == null) return;

        UserTicketService userTicketService = UserTicketService.getInstance();
        List<UserTicketService.PurchasedTicket> validTickets = userTicketService.getValidTickets(user.getUsername());

        if (validTickets.isEmpty()) {
            showAlert("Remboursement", "Vous n'avez aucun ticket valide à rembourser.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("🔄 Demande de Remboursement");
        dialog.setHeaderText("Sélectionnez un ticket à rembourser");

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        ComboBox<String> ticketCombo = new ComboBox<>();
        for (UserTicketService.PurchasedTicket t : validTickets) {
            ticketCombo.getItems().add(t.getTicketId() + " - " + t.getMatchName() + " (" + t.getPrice() + "€)");
        }
        ticketCombo.getSelectionModel().selectFirst();

        TextArea reasonField = new TextArea();
        reasonField.setPromptText("Raison du remboursement (optionnel)");
        reasonField.setPrefRowCount(3);

        content.getChildren().addAll(
            new Label("Ticket à rembourser:"),
            ticketCombo,
            new Label("Raison:"),
            reasonField,
            new Label("⚠️ Le remboursement sera traité sous 5-7 jours ouvrés.")
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(
            new ButtonType("🔄 Demander remboursement", ButtonBar.ButtonData.OK_DONE),
            ButtonType.CANCEL
        );

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            int selectedIndex = ticketCombo.getSelectionModel().getSelectedIndex();
            UserTicketService.PurchasedTicket selected = validTickets.get(selectedIndex);

            if (userTicketService.requestRefund(user.getUsername(), selected.getTicketId())) {
                showNotification("✅ Demande de remboursement soumise pour " + selected.getMatchName());
                NotificationService.getInstance().success("Remboursement demandé: " + selected.getMatchName());
            }
        }
    }

    @FXML
    public void handleNotifications() {
        NotificationService notifService = NotificationService.getInstance();
        List<NotificationService.Notification> notifications = notifService.getAllNotifications();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("🔔 Centre de Notifications");
        dialog.setHeaderText("Historique des notifications (" + notifService.getUnreadCount() + " non lues)");

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        if (notifications.isEmpty()) {
            content.getChildren().add(new Label("Aucune notification."));
        } else {
            ListView<String> notifList = new ListView<>();
            for (NotificationService.Notification n : notifications) {
                notifList.getItems().add(n.toString());
            }

            Button clearBtn = new Button("🗑️ Effacer tout");
            clearBtn.setOnAction(e -> {
                notifService.clearAll();
                notifList.getItems().clear();
            });

            content.getChildren().addAll(notifList, clearBtn);
        }

        notifService.markAllAsRead();

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.getDialogPane().setPrefWidth(500);
        dialog.showAndWait();
    }

    @FXML
    public void handleChatSupport() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("💬 Chat Support");
        dialog.setHeaderText("Assistance en direct - Mondial 2030");

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setPrefRowCount(10);
        chatArea.appendText("🤖 Bot: Bonjour! Je suis l'assistant virtuel du Mondial 2030.\n");
        chatArea.appendText("🤖 Bot: Comment puis-je vous aider aujourd'hui?\n\n");

        TextField messageField = new TextField();
        messageField.setPromptText("Tapez votre message...");

        Button sendBtn = new Button("📤 Envoyer");
        sendBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        sendBtn.setOnAction(e -> {
            String msg = messageField.getText().trim();
            if (!msg.isEmpty()) {
                chatArea.appendText("👤 Vous: " + msg + "\n");
                messageField.clear();

                // Simulate bot response
                String response = getChatBotResponse(msg);
                new Thread(() -> {
                    try { Thread.sleep(1000); } catch (InterruptedException ex) {}
                    Platform.runLater(() -> chatArea.appendText("🤖 Bot: " + response + "\n\n"));
                }).start();
            }
        });

        messageField.setOnAction(e -> sendBtn.fire());

        HBox inputBox = new HBox(10, messageField, sendBtn);
        HBox.setHgrow(messageField, Priority.ALWAYS);

        content.getChildren().addAll(chatArea, inputBox);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.getDialogPane().setPrefWidth(500);
        dialog.showAndWait();
    }

    private String getChatBotResponse(String message) {
        String msg = message.toLowerCase();

        if (msg.contains("ticket") || msg.contains("billet")) {
            return "Pour acheter un ticket, allez dans l'onglet Tickets, sélectionnez un match et cliquez sur 'Acheter'.";
        } else if (msg.contains("prix") || msg.contains("cout") || msg.contains("combien")) {
            return "Les prix varient selon la catégorie:\n• VIP: 500-1500€\n• Standard: 100-300€\n• Tribune: 50-100€";
        } else if (msg.contains("remboursement") || msg.contains("rembourser")) {
            return "Pour demander un remboursement, allez dans Mon Compte → Demander remboursement.";
        } else if (msg.contains("promo") || msg.contains("code") || msg.contains("reduction")) {
            return "Utilisez les codes: MONDIAL2030 (-20%), VIP50 (-50%), MAROC10 (-10%)";
        } else if (msg.contains("match") || msg.contains("calendrier") || msg.contains("date")) {
            return "Consultez l'onglet Matchs pour voir tous les matchs programmés.";
        } else if (msg.contains("contact") || msg.contains("telephone") || msg.contains("email")) {
            return "📧 support@mondial2030.com\n📞 +212 5XX XXX XXX\n🌐 www.mondial2030.com";
        } else if (msg.contains("bonjour") || msg.contains("salut") || msg.contains("hello")) {
            return "Bonjour! Comment puis-je vous aider?";
        } else if (msg.contains("merci") || msg.contains("thanks")) {
            return "Je vous en prie! N'hésitez pas si vous avez d'autres questions.";
        } else {
            return "Je ne suis pas sûr de comprendre. Pouvez-vous reformuler? Ou tapez 'aide' pour voir les options.";
        }
    }

    @FXML
    public void handleLanguageFR() {
        LanguageService.getInstance().setLanguage("FR");
        showNotification("🇫🇷 Langue changée en Français");
    }

    @FXML
    public void handleLanguageEN() {
        LanguageService.getInstance().setLanguage("EN");
        showNotification("🇬🇧 Language changed to English");
    }

    @FXML
    public void handleLanguageAR() {
        LanguageService.getInstance().setLanguage("AR");
        showNotification("🇲🇦 تم تغيير اللغة إلى العربية");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showNotification(String message) {
        // Also add to notification center
        NotificationService.getInstance().info(message);

        if (notificationLabel != null) {
            notificationLabel.setText(message);

            // Auto-clear after 5 seconds
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    Platform.runLater(() -> notificationLabel.setText(""));
                } catch (InterruptedException e) {}
            }).start();
        }
        System.out.println("📢 " + message);
    }
}

