package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BLL.SessionManager;
import dk.easv.ticketapptest.GUI.AlertClass;
import dk.easv.ticketapptest.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {
    @FXML public BorderPane rootPaneAdmin;
    @FXML public Button btnUserManagement;
    @FXML public Button btnEventHistory;
    @FXML
    public ImageView imgLogoAdmin;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String logoImagePath = Objects.requireNonNull(getClass().getResource("/BASW_logo.png")).toExternalForm();
        imgLogoAdmin.setImage(new Image(logoImagePath));
        imgLogoAdmin.setPreserveRatio(true);
        imgLogoAdmin.setFitWidth(180);
        imgLogoAdmin.setFitHeight(60);
        openUserManagement();

    }

    /**
     * logic refactored to new method, this is the event handler for the user management tab button
     */
    @FXML
    public void handleUserManagement(ActionEvent actionEvent) {
        openUserManagement();
    }

    /**
     * separate method to open the user management view, this allows the window to be opened by default in initialize
     */
    public void openUserManagement() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/user-management-view.fxml"));
            Parent adminDashboard = fxmlLoader.load();
            btnUserManagement.setStyle("-fx-background-color: #EFF6FF; -fx-text-fill: #1D4ED8;");
            btnEventHistory.setStyle("");
            rootPaneAdmin.setCenter(adminDashboard);
        } catch (IOException e) {
            e.printStackTrace();
            AlertClass.alertError("Failed to load", "An error occurred while loading the user management window");
        }
    }

    /**
     * opens event view
     * @param actionEvent press on button
     */
    @FXML
    public void handleEventHistory(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/admin-event-view.fxml"));
            Parent adminDashboard = fxmlLoader.load();
            AdminEventController controller = fxmlLoader.getController();
            controller.setRootPane(rootPaneAdmin);
            btnEventHistory.setStyle("-fx-background-color: #EFF6FF; -fx-text-fill: #1D4ED8;");
            btnUserManagement.setStyle("");
            rootPaneAdmin.setCenter(adminDashboard);
        } catch (IOException e) {
            e.printStackTrace();
            AlertClass.alertError("Failed to load", "An error occurred while opening the event window");
        }
    }

    /**
     * logs the current user out, and switches to login view
     * @param actionEvent
     */
    @FXML
    private void onLogOut(ActionEvent actionEvent) {
        try {
            SessionManager.getInstance().logout();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/views/login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Stage stage = (Stage) rootPaneAdmin.getScene().getWindow();

            stage.setFullScreen(false);
            stage.setMaximized(false);
            stage.show();

            stage.setScene(scene);
            //OS decorations apparently count for stage dimension :)
            double decoWidth = stage.getWidth() - scene.getWidth();
            double decoHeight = stage.getHeight() - scene.getHeight();
            double minHeight = 500;
            double minWidth = 700;
            stage.setMinWidth(minWidth + decoWidth);
            stage.setMinHeight(minHeight + decoHeight);
            stage.setWidth(minWidth + decoWidth);
            stage.setHeight(minHeight + decoHeight);

            stage.sizeToScene();
            stage.setResizable(false);
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
            AlertClass.alertError("Failed to load", "An error occurred while logging out");
        }
    }


}
