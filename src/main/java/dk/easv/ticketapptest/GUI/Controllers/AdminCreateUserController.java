package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Role;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.GUI.AlertClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminCreateUserController implements Initializable {
    @FXML public javafx.scene.layout.VBox VBox;
    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private Button btnCreateNewUser;
    @FXML private CheckBox chkAdmin;
    @FXML private CheckBox chkCoordinator;

    private User newUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleCreateNewUser(ActionEvent actionEvent) {

        if(!highlightFields()) {
            AlertClass.alertInfo("Missing information", "Please fill all the fields or correct illegal input, like spaces");
            return;
        }
        Role role = getRole();
        if (role == null) {
            AlertClass.alertInfo("Missing information", "Please select a role");
            return;
        }
        newUser = new User(txtUsername.getText(),
                            txtPassword.getText(),
                            txtFirstName.getText(),
                            txtLastName.getText(),
                            txtEmail.getText(),
                            txtPhone.getText(), role);

        setCreatedUser(newUser);
        Stage stage = (Stage) btnCreateNewUser.getScene().getWindow();
        stage.close();
    }

    private Role getRole() {
        if (!chkAdmin.isSelected() && !chkCoordinator.isSelected()) {
            chkAdmin.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            chkCoordinator.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
        } else {
            chkAdmin.setStyle("");
            chkCoordinator.setStyle("");
        }
        if (chkCoordinator.isSelected()) {
            return Role.COORDINATOR;
        }
        if (chkAdmin.isSelected()) {
            return Role.ADMIN;
        }
        return null;
    }

    private boolean highlightFields() {
        boolean allValid = true;
        String style = "-fx-border-color: red; -fx-border-width: 1px;";

        if(txtUsername.getText().trim().isEmpty() || txtUsername.getText().contains(" ")) {
            txtUsername.setStyle(style);
            allValid = false;
        }
        if(txtPassword.getText().trim().isEmpty() || txtPassword.getText().contains(" ")) {
            txtPassword.setStyle(style);
            allValid = false;
        }
        if(txtFirstName.getText().trim().isEmpty()) {
            txtFirstName.setStyle(style);
            allValid = false;
        }
        if(txtLastName.getText().trim().isEmpty()) {
            txtLastName.setStyle(style);
            allValid = false;
        }
        if(txtEmail.getText().trim().isEmpty() || txtEmail.getText().contains(" ")) {
            txtEmail.setStyle(style);
            allValid = false;
        }
        if(txtPhone.getText().trim().isEmpty() || txtPhone.getText().contains(" ")) {
            txtPhone.setStyle(style);
            allValid = false;
        }
        return allValid;
    }


    private void setCreatedUser(User newUser) {
        this.newUser = newUser;
    }
    public User getCreatedUser() {
        return newUser;
    }

    public void handleSetAdmin(ActionEvent actionEvent) {
        if (chkCoordinator.isSelected()) {
            chkCoordinator.setSelected(false);
        }
    }

    public void handleSetCoordinator(ActionEvent actionEvent) {
        if (chkAdmin.isSelected()) {
            chkAdmin.setSelected(false);
        }
    }

}
