package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Role;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.GUI.TemporaryDataClass;
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


        //TODO make sure the setting of styles here, doesnt actually fuck with the eventual CSS
        //brother this is the most cooked code i have ever made.

        if (txtUsername.getText().isEmpty() ||
                txtPassword.getText().isEmpty() ||
                txtFirstName.getText().isEmpty() ||
                txtLastName.getText().isEmpty() ||
                txtEmail.getText().isEmpty() ||
                txtPhone.getText().isEmpty() ) {
            VBox.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            return;
        } else {
            VBox.setStyle("");
        }
        if (!chkAdmin.isSelected() && !chkCoordinator.isSelected()) {
            chkAdmin.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            chkCoordinator.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            return;
        } else {
            chkAdmin.setStyle("");
            chkCoordinator.setStyle("");
        }
        if (chkCoordinator.isSelected()) {
            newUser = new User(txtUsername.getText(), txtPassword.getText(), txtFirstName.getText(), txtLastName.getText(), txtEmail.getText(), txtPhone.getText(), Role.COORDINATOR);
        } else if (chkAdmin.isSelected()) {
            newUser = new User(txtUsername.getText(), txtPassword.getText(), txtFirstName.getText(), txtLastName.getText(), txtEmail.getText(), txtPhone.getText(), Role.ADMIN);
        }
        setCreatedUser(newUser);
        Stage stage = (Stage) btnCreateNewUser.getScene().getWindow();
        stage.close();
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
