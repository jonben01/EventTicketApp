package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Role;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;
import dk.easv.ticketapptest.BLL.SessionManager;
import dk.easv.ticketapptest.GUI.AlertClass;
import dk.easv.ticketapptest.GUI.Models.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public BorderPane rootPane;
    @FXML
    public ImageView imgLogo;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnTogglePassword;

    private UserModel userModel;
    @FXML
    private TextField txtPasswordVisible;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtPasswordVisible.setMaxWidth(-30);
        try {
            userModel = new UserModel();
        } catch (EasvTicketException e) {
            //I am uncertain how best to deal with this. Maybe a runtime exception is better here?
            AlertClass.alertError("Error", "An error occurred while starting the application");
        }
        String logoImagePath = Objects.requireNonNull(getClass().getResource("/BASW_logo.png")).toExternalForm();
        imgLogo.setImage(new Image(logoImagePath));
        imgLogo.setPreserveRatio(true);
        imgLogo.setFitWidth(350);
        imgLogo.setFitHeight(180);

        btnTogglePassword.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/OpenEye.png")))));

    }

    public void handleAdminDash() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/admin-dashboard-view.fxml"));
        try {
            Parent adminDashboard = fxmlLoader.load();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(adminDashboard));
            stage.centerOnScreen();
        } catch (IOException e) {
            AlertClass.alertError("Failed to load", "An error occurred while loading the admin dashboard");
        }
    }

    public void handleEventDash() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/event-dashboard-view.fxml"));

        try {
            Parent eventDashboard = fxmlLoader.load();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = new Scene(eventDashboard);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/Base-stylesheet.css")).toExternalForm());
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            AlertClass.alertError("Failed to load", "An error occurred while loading the event dashboard");
        }
    }

    public void handleLogin(ActionEvent actionEvent) {

        String password = "hello";
        if(txtPassword.isVisible()) {
            password = txtPassword.getText();
        }
        else if(txtPasswordVisible.isVisible()) {
            password = txtPasswordVisible.getText();
        }

        String username = txtUsername.getText();

        if (username.isEmpty()) {
            txtUsername.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            return;
        }
        if (password.isEmpty()) {
            txtPassword.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            return;
        }
        try {
            User user = userModel.getUserByUsername(username);
            if (user == null) {
                AlertClass.alertError("Login Error", "User not found");
                return;
            }

            String hashedPassword = userModel.getPassword(username);
            if (hashedPassword == null) {
                AlertClass.alertError("Login Error", "Invalid username or password");
                return;
            }

            boolean verified = userModel.verifyPassword(password, hashedPassword);
            if (verified) {
                SessionManager.getInstance().setCurrentUser(user);

                if (Objects.requireNonNull(user).getRole() == Role.ADMIN) {
                    handleAdminDash();
                } else if (user.getRole() == Role.COORDINATOR) {
                    handleEventDash();
                }
            } else {
                AlertClass.alertError("Input Error", "Invalid username or password");
            }
        } catch (EasvTicketException e) {
            AlertClass.alertError("Login Error", "An unexpected error occurred while logging in");
        }
    }

    @FXML
    private void handleTogglePassword(ActionEvent actionEvent) {
        if (txtPassword.isVisible()) {
            txtPassword.setVisible(false);
            txtPasswordVisible.setVisible(true);
            txtPassword.setMaxWidth(0);
            txtPasswordVisible.setMaxWidth(750);
            txtPasswordVisible.setText(txtPassword.getText());
            btnTogglePassword.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/ClosedEye.png")))));

        }
        else if(!txtPassword.isVisible()) {
            txtPassword.setVisible(true);
            txtPasswordVisible.setVisible(false);
            txtPassword.setMaxWidth(750);
            txtPasswordVisible.setMaxWidth(0);
            txtPasswordVisible.setText(txtPassword.getText());
            btnTogglePassword.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/OpenEye.png")))));

        }
    }
}
