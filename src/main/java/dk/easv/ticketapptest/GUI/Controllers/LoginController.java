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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
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
            AlertClass.alertError("Error", "An error occurred while starting the application");
        }
        String logoImagePath = Objects.requireNonNull(getClass().getResource("/BASW_logo.png")).toExternalForm();
        imgLogo.setImage(new Image(logoImagePath));
        imgLogo.setPreserveRatio(true);
        imgLogo.setFitWidth(350);
        imgLogo.setFitHeight(180);

        btnTogglePassword.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/OpenEye.png")))));
        enterKeyListeners();
    }

    /**
     * Handles the logic for opening the admin dashboard.
     */
    public void handleAdminDash() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/admin-dashboard-view.fxml"));
        try {
            Parent adminDashboard = fxmlLoader.load();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = new Scene(adminDashboard, 1152,768);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(true);
            stage.show();
            //OS decorations apparently count for stage dimension :)
            double decoWidth = stage.getWidth() - scene.getWidth();
            double decoHeight = stage.getHeight() - scene.getHeight();
            double minHeight = 768;
            double minWidth = 1152;

            stage.setMinWidth(minWidth + decoWidth);
            stage.setMinHeight(minHeight + decoHeight);
        } catch (IOException e) {
            AlertClass.alertError("Failed to load", "An error occurred while loading the admin dashboard");
        }
    }

    /**
     * Just checks if the users presses enter at any point in time...
     */
    private void enterKeyListeners() {
        txtUsername.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                login();
            }
        });
        txtPassword.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                login();
            }
        });
        txtPasswordVisible.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                login();
            }
        });
    }

    /**
     * Handles the logic for opening the Event Dashboard.
     */
    public void handleEventDash() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/event-dashboard-view.fxml"));

        try {
            Parent eventDashboard = fxmlLoader.load();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = new Scene(eventDashboard, 1152, 768);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/Base-stylesheet.css")).toExternalForm());
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(true);
            stage.show();
            //OS decorations apparently count for stage dimension :)
            double decoWidth = stage.getWidth() - scene.getWidth();
            double decoHeight = stage.getHeight() - scene.getHeight();
            double minHeight = 768;
            double minWidth = 1152;

            stage.setMinWidth(minWidth + decoWidth);
            stage.setMinHeight(minHeight + decoHeight);
        } catch (IOException e) {
            AlertClass.alertError("Failed to load", "An error occurred while loading the event dashboard");
        }
    }

    /**
     * Runs whenever the login button is pressed.
     * @param actionEvent
     */
    public void handleLogin(ActionEvent actionEvent) {
        login();
    }

    /**
     * Handles the login validation.
     * Receives the password from the passwordField, and the verifies the password + Username login.
     * In the end runs either the AdminDash or EventDash, depending on the login type.
     */
    private void login() {

        String password = "hello";
        if(txtPassword.isVisible()) {
            password = txtPassword.getText();
        }
        else if(txtPasswordVisible.isVisible()) {
            password = txtPasswordVisible.getText();
        }

        String username = txtUsername.getText();

        String style = "-fx-border-color: red; -fx-border-width: 1px;";
        //set red border if textFields empty
        if (username.isEmpty() || password.isEmpty()) {
            if (username.isEmpty()) {
                txtUsername.setStyle(style);
            } else {
                txtUsername.setStyle("");
            }
            if (txtPassword.getText().isEmpty() && txtPasswordVisible.getText().isEmpty()) {
                txtPassword.setStyle(style);
                txtPasswordVisible.setStyle(style);
            } else {
                txtPasswordVisible.setStyle("");
                txtPassword.setStyle("");
            }
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

    /**
     * Runs whenever the Show/Hide password button is pressed.
     * Has 2 different password field ontop of each other. 1 PasswordField, 1 normal TextField.
     * If one is visible, disable the other. When pressed change visibility for both.
     * @param actionEvent
     */
    @FXML
    private void handleTogglePassword(ActionEvent actionEvent) {
        if (txtPassword.isVisible()) {
            txtPassword.setVisible(false);
            txtPasswordVisible.setVisible(true);
            txtPassword.setMaxWidth(0);
            txtPasswordVisible.setMaxWidth(750);
            txtPasswordVisible.setText(txtPassword.getText());
            btnTogglePassword.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/ClosedEye.png")))));
            btnTogglePassword.setStyle("-fx-background-color: transparent;");

        }
        else if(!txtPassword.isVisible()) {
            txtPassword.setVisible(true);
            txtPasswordVisible.setVisible(false);
            txtPassword.setMaxWidth(750);
            txtPasswordVisible.setMaxWidth(0);
            txtPasswordVisible.setText(txtPassword.getText());
            btnTogglePassword.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/OpenEye.png")))));
            btnTogglePassword.setStyle("-fx-background-color: transparent;");
        }
    }
}
