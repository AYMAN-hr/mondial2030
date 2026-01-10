package com.mondial.ticket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application JavaFX pour la gestion des tickets du Mondial 2030.
 */
public class JavaFXApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Démarrer avec l'écran de connexion
        Parent root = FXMLLoader.load(getClass().getResource("/com/mondial/ticket/view/LoginView.fxml"));
        primaryStage.setTitle("🏆 Mondial 2030 - Connexion");
        primaryStage.setScene(new Scene(root, 500, 650));
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

