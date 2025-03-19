package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Role;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.SessionManager;
import dk.easv.ticketapptest.GUI.Models.UserManagementModel;
import dk.easv.ticketapptest.GUI.Models.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public BorderPane rootPane;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;

    private UserModel userModel;

    public LoginController() throws Exception {
        userModel = new UserModel();
    }

    //todo temp login method. Unused in latest build.
    public void handleAdminDash() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/admin-dashboard-view.fxml"));
        Parent adminDashboard = fxmlLoader.load();

        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setScene(new Scene(adminDashboard));


    }

    //TODO LOGIN SHOULD JUST BE AN IF STATEMENT IF TEXTFIELD CONTAINS ADMIN/PASSWORD - ADMIN SECTION
    // IF TEXTFIELD CONTAINS EVENT/PASSWORD - EVENT SECTION


    //todo temp login method. unused in latest build.
    public void handleEventDash() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/event-dashboard-view.fxml"));
        Parent eventDashboard = fxmlLoader.load();

        Stage stage = (Stage) rootPane.getScene().getWindow();
        Scene scene = new Scene(eventDashboard);
        //TODO probably move style to stylesheet and fxml itself
        scene.getStylesheets().add(getClass().getResource("/css/Base-stylesheet.css").toExternalForm());
        stage.setScene(scene);
    }


    //TODO handle exceptions better, make Alert class, change css in code to being in fxml instead
    public void handleLogin(ActionEvent actionEvent) throws Exception {


        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            return;
            //TODO implement error message here
        }

        //hardcoded login credentials only for showing off application.
        if (username.equals("admin") && password.equals("admin")) {
            handleAdminDash();
        }
        if (username.equals("event") && password.equals("event")) {
            handleEventDash();
        }

        User user = userModel.getUserByUsername(username);

        if (user == null) {
            return;
            //TODO implement gui error here.
        }

        String hashedPassword = userModel.getPassword(username);


        boolean verified = userModel.verifyPassword(password, hashedPassword);
        if (verified) {
            SessionManager.getInstance().setCurrentUser(user);
            if (user.getRole() == Role.ADMIN) {
                handleAdminDash();
            } else if (user.getRole() == Role.COORDINATOR) {
                handleEventDash();
            }
            //if user role = ADMIN send to admin
            //if user role = COORDINATOR send to coordinator.
            //set current user in sessionManager


        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO change how this is written or move it to constructor.
        try {
            UserModel userModel = new UserModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
