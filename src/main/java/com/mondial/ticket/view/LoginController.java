package com.mondial.ticket.view;

import com.mondial.ticket.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Contrôleur pour l'écran de connexion.
 */
public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private AuthService authService = AuthService.getInstance();

    @FXML
    public void initialize() {
        // Permettre la connexion avec Enter
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });

        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                passwordField.requestFocus();
            }
        });
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }

        if (authService.login(username, password)) {
            // Connexion réussie - charger l'application principale
            loadMainApp();
        } else {
            showError("Identifiant ou mot de passe incorrect");
            passwordField.clear();
            passwordField.requestFocus();
        }
    }

    @FXML
    public void handleRegister() {
        // Create registration dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Inscription - Mondial 2030");
        dialog.setHeaderText("Creer un nouveau compte");

        // Create form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField regUsername = new TextField();
        regUsername.setPromptText("Nom d'utilisateur");
        regUsername.setPrefWidth(250);

        PasswordField regPassword = new PasswordField();
        regPassword.setPromptText("Mot de passe");

        PasswordField regConfirmPassword = new PasswordField();
        regConfirmPassword.setPromptText("Confirmer le mot de passe");

        TextField regNom = new TextField();
        regNom.setPromptText("Votre nom complet");

        TextField regEmail = new TextField();
        regEmail.setPromptText("Votre email");

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: red;");

        grid.add(new Label("Nom d'utilisateur:"), 0, 0);
        grid.add(regUsername, 1, 0);
        grid.add(new Label("Mot de passe:"), 0, 1);
        grid.add(regPassword, 1, 1);
        grid.add(new Label("Confirmer:"), 0, 2);
        grid.add(regConfirmPassword, 1, 2);
        grid.add(new Label("Nom complet:"), 0, 3);
        grid.add(regNom, 1, 3);
        grid.add(new Label("Email:"), 0, 4);
        grid.add(regEmail, 1, 4);
        grid.add(statusLabel, 0, 5, 2, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Change OK button text
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("S'inscrire");

        // Validate on OK click
        okButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            String username = regUsername.getText().trim();
            String password = regPassword.getText();
            String confirmPassword = regConfirmPassword.getText();
            String nom = regNom.getText().trim();
            String email = regEmail.getText().trim();

            // Validation
            if (username.isEmpty() || password.isEmpty() || nom.isEmpty() || email.isEmpty()) {
                statusLabel.setText("Veuillez remplir tous les champs");
                event.consume();
                return;
            }

            if (username.length() < 3) {
                statusLabel.setText("Nom d'utilisateur trop court (min 3 caracteres)");
                event.consume();
                return;
            }

            if (password.length() < 4) {
                statusLabel.setText("Mot de passe trop court (min 4 caracteres)");
                event.consume();
                return;
            }

            if (!password.equals(confirmPassword)) {
                statusLabel.setText("Les mots de passe ne correspondent pas");
                event.consume();
                return;
            }

            if (!email.contains("@")) {
                statusLabel.setText("Email invalide");
                event.consume();
                return;
            }

            if (!authService.isUsernameAvailable(username)) {
                statusLabel.setText("Ce nom d'utilisateur est deja pris");
                event.consume();
                return;
            }

            // Try to register
            if (!authService.register(username, password, nom, email)) {
                statusLabel.setText("Erreur lors de l'inscription");
                event.consume();
            }
        });

        java.util.Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Registration successful
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Inscription reussie");
            success.setHeaderText("Bienvenue!");
            success.setContentText("Votre compte a ete cree avec succes.\n\n" +
                    "Vous pouvez maintenant vous connecter avec vos identifiants.");
            success.showAndWait();

            // Pre-fill username
            usernameField.setText(regUsername.getText().trim());
            passwordField.clear();
            passwordField.requestFocus();
            errorLabel.setText("");
        }
    }

    private void loadMainApp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            stage.setScene(scene);
            stage.setTitle("Mondial 2030 - Gestion des Tickets (" +
                          authService.getCurrentUser().getRole() + ")");
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur de chargement de l'application");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }
}

