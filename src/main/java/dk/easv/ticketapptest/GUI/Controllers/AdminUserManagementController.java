package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Role;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.GUI.TemporaryDataClass;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminUserManagementController implements Initializable {

    @FXML public TextField txtPhone;
    @FXML public TextField txtEmail;
    @FXML public TextField txtLastName;
    @FXML public TextField txtFirstName;
    @FXML public Label lblRole;
    @FXML public Label lblName;
    @FXML public TextField txtUsername;
    @FXML public TextField txtPassword;
    @FXML private ListView<User> lstUsers;

    private TemporaryDataClass tdc;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tdc = new TemporaryDataClass();
        populateUserList();

        if (lstUsers.getItems() != null) {
            lstUsers.getSelectionModel().select(0);
            setUserInfo();
        }

        lstUsers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setUserInfo();
        });
    }

    private void setUserInfo() {
        User user = lstUsers.getSelectionModel().getSelectedItem();
        txtUsername.setText(user.getUsername());
        txtPassword.setText(user.getPassword());
        txtFirstName.setText(user.getFirstName());
        txtLastName.setText(user.getLastName());
        txtEmail.setText(user.getEmail());
        txtPhone.setText(user.getPhone());
        lblName.setText(user.getFirstName() + " " + user.getLastName());
        if (user.getRole() != null) {
            lblRole.setText(user.getRole().toString());
        } else {
            lblRole.setText("NO ROLE");
        }
    }

    public void populateUserList() {
        lstUsers.setItems(tdc.getUsers());
        lstUsers.setCellFactory(param -> new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getFirstName() + " " + item.getLastName() + "\n" + item.getEmail());
                }
            }
        });
    }

    public void handleSetEventCoordinator(ActionEvent actionEvent) {
        User user = lstUsers.getSelectionModel().getSelectedItem();
        if (user != null) {
            if (user.getRole() == Role.COORDINATOR) {
                user.setRole(null);
                lblRole.setText("NO ROLE");
            } else if (user.getRole() == Role.ADMIN) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Set this user to COORDINATOR instead of ADMIN?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    user.setRole(Role.COORDINATOR);
                    lblRole.setText("COORDINATOR");
                }
            } else {
                user.setRole(Role.COORDINATOR);
                lblRole.setText("COORDINATOR");
            }
        }
    }

    //TODO MAKE AN ALERT METHOD LOL
    public void handleSetAdmin(ActionEvent actionEvent) {
        User user = lstUsers.getSelectionModel().getSelectedItem();
        if (user != null) {
            if (user.getRole() == Role.ADMIN) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Remove ADMIN role from this user?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    user.setRole(null);
                    lblRole.setText("NO ROLE");
                }
            } else if (user.getRole() == Role.COORDINATOR) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Set this user to ADMIN instead of COORDINATOR?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    user.setRole(Role.ADMIN);
                    lblRole.setText("ADMIN");
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to make this user ADMIN?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    user.setRole(Role.ADMIN);
                    lblRole.setText("ADMIN");
                }
            }
        }
    }

    //TODO få user listen til at virke




    //TODO få roller på user til at virke


    //TODO få add user til at virke


    //TODO få delete user til at virke
}
