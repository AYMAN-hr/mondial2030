package com.mondial.ticket.view;

import com.mondial.ticket.dao.MatchDaoHibernate;
import com.mondial.ticket.model.Match;
import com.mondial.ticket.service.AuthService;
import com.mondial.ticket.service.MatchService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

/**
 * Contrôleur pour la gestion des matchs dans l'interface JavaFX.
 */
public class MatchController {
    @FXML
    private TextField equipe1Field;
    @FXML
    private TextField equipe2Field;
    @FXML
    private TextField stadeField;
    @FXML
    private TextField villeField;
    @FXML
    private ComboBox<String> paysCombo;
    @FXML
    private ComboBox<String> phaseCombo;
    @FXML
    private TableView<Match> matchTable;
    @FXML
    private TableColumn<Match, Integer> colId;
    @FXML
    private TableColumn<Match, String> colEquipe1;
    @FXML
    private TableColumn<Match, String> colEquipe2;
    @FXML
    private TableColumn<Match, String> colStade;
    @FXML
    private TableColumn<Match, String> colVille;
    @FXML
    private TableColumn<Match, String> colPays;
    @FXML
    private TableColumn<Match, String> colPhase;
    @FXML
    private HBox adminMatchSection;
    @FXML
    private Button deleteMatchBtn;
    @FXML
    private Button statsMatchBtn;

    private MatchService matchService = new MatchService(new MatchDaoHibernate());
    private AuthService authService = AuthService.getInstance();
    private ObservableList<Match> matchList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configuration des colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEquipe1.setCellValueFactory(new PropertyValueFactory<>("equipe1"));
        colEquipe2.setCellValueFactory(new PropertyValueFactory<>("equipe2"));
        colStade.setCellValueFactory(new PropertyValueFactory<>("stade"));
        colVille.setCellValueFactory(new PropertyValueFactory<>("ville"));
        colPays.setCellValueFactory(new PropertyValueFactory<>("pays"));
        colPhase.setCellValueFactory(new PropertyValueFactory<>("phase"));

        // Configuration des ComboBox
        paysCombo.setItems(FXCollections.observableArrayList("Maroc", "Espagne", "Portugal"));
        paysCombo.setValue("Maroc");

        phaseCombo.setItems(FXCollections.observableArrayList(
            "Groupe", "Huitième", "Quart", "Demi-finale", "Finale"
        ));
        phaseCombo.setValue("Groupe");

        // Setup admin restrictions
        setupAdminRestrictions();

        handleRefresh();
    }

    /**
     * Configure les restrictions selon le rôle de l'utilisateur
     */
    private void setupAdminRestrictions() {
        boolean isAdmin = authService.isAdmin();

        // Hide add match section for non-admin
        if (adminMatchSection != null) {
            adminMatchSection.setVisible(isAdmin);
            adminMatchSection.setManaged(isAdmin);
        }

        // Hide delete button for non-admin
        if (deleteMatchBtn != null) {
            deleteMatchBtn.setVisible(isAdmin);
            deleteMatchBtn.setManaged(isAdmin);
        }

        // Hide stats button for non-admin
        if (statsMatchBtn != null) {
            statsMatchBtn.setVisible(isAdmin);
            statsMatchBtn.setManaged(isAdmin);
        }
    }

    @FXML
    public void handleAjouterMatch() {
        // Admin only
        if (!authService.isAdmin()) {
            showAlert("Accès refusé", "Seul l'administrateur peut ajouter des matchs.");
            return;
        }

        try {
            String equipe1 = equipe1Field.getText();
            String equipe2 = equipe2Field.getText();
            String stade = stadeField.getText();
            String ville = villeField.getText();
            String pays = paysCombo.getValue();
            String phase = phaseCombo.getValue();

            if (equipe1.isEmpty() || equipe2.isEmpty()) {
                showAlert("Erreur", "Veuillez entrer les noms des deux équipes.");
                return;
            }

            Match match = new Match(equipe1, equipe2, stade, ville, pays, phase);
            matchService.creerMatch(match);
            handleRefresh();

            // Clear fields
            equipe1Field.clear();
            equipe2Field.clear();
            stadeField.clear();
            villeField.clear();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur ajout match: " + e.getMessage());
        }
    }

    @FXML
    public void handleSupprimerMatch() {
        // Admin only
        if (!authService.isAdmin()) {
            showAlert("Accès refusé", "Seul l'administrateur peut supprimer des matchs.");
            return;
        }

        Match selected = matchTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Confirmation dialog
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmer la suppression");
            confirm.setHeaderText("Supprimer le match?");
            confirm.setContentText("Voulez-vous vraiment supprimer: " + selected.getNomComplet() + "?");

            java.util.Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    matchService.supprimerMatch(selected.getNomComplet());
                    handleRefresh();
                    showInfo("Succès", "Match supprimé: " + selected.getNomComplet());
                } catch (Exception e) {
                    showAlert("Erreur", "Erreur suppression: " + e.getMessage());
                }
            }
        } else {
            showAlert("Attention", "Veuillez sélectionner un match à supprimer.");
        }
    }

    @FXML
    public void handleRefresh() {
        matchList.setAll(matchService.listerMatchs());
        matchTable.setItems(matchList);
    }

    @FXML
    public void handleVoirDetails() {
        Match selected = matchTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showInfo("Détails du match",
                "Match: " + selected.getNomComplet() + "\n" +
                "Stade: " + selected.getStade() + "\n" +
                "Ville: " + selected.getVille() + ", " + selected.getPays() + "\n" +
                "Phase: " + selected.getPhase() + "\n" +
                "Tickets disponibles: " + selected.getTickets().size());
        } else {
            showAlert("Attention", "Veuillez sélectionner un match.");
        }
    }

    @FXML
    public void handleStatistiquesPays() {
        matchService.afficherStatistiquesParPays();
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

