package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Role;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.GUI.TemporaryDataClass;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminUserManagementController implements Initializable {

    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtLastName;
    @FXML private TextField txtFirstName;
    @FXML private Label lblRole;
    @FXML private Label lblName;
    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;
    @FXML private CheckBox chkEditable;
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


        //TODO IMPLEMENT USE OF BEANS OR USERDATA FOR ACTUAL PROJECT
        // SO THERE ISNT 6 LISTENERS DOING THE SAME THING
        userChangeListeners();
    }


    //TODO IMPLEMENT USE OF BEANS OR USERDATA FOR ACTUAL PROJECT
    // SO THERE ISNT 6 LISTENERS DOING THE SAME THING
    private void userChangeListeners() {
        txtUsername.textProperty().addListener((observable, oldValue, newValue) -> {
            User user = lstUsers.getSelectionModel().getSelectedItem();
            if (user != null) {
                user.setUsername(newValue);
            }
        });
        txtPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            User user = lstUsers.getSelectionModel().getSelectedItem();
            if (user != null) {
                user.setPassword(newValue);
            }
        });
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            User user = lstUsers.getSelectionModel().getSelectedItem();
            if (user != null) {
                user.setEmail(newValue);
            }
        });
        txtFirstName.textProperty().addListener((observable, oldValue, newValue) -> {
            User user = lstUsers.getSelectionModel().getSelectedItem();
            if (user != null) {
                user.setFirstName(newValue);
            }
        });
        txtLastName.textProperty().addListener((observable, oldValue, newValue) -> {
            User user = lstUsers.getSelectionModel().getSelectedItem();
            if (user != null) {
                user.setLastName(newValue);
            }
        });
        txtPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            User user = lstUsers.getSelectionModel().getSelectedItem();
            if (user != null) {
                user.setPhone(newValue);
            }
        });
    }

    private void setUserInfo() {
        User user = lstUsers.getSelectionModel().getSelectedItem();
        if (user != null) {
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

    public void handleCreateUser(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admin-user-creation-view.fxml"));
        Parent root = loader.load();

        AdminCreateUserController controller = loader.getController();

        Stage stage = new Stage();
        stage.setTitle("Create User");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        User newUser = controller.getCreatedUser();
        if (newUser != null) {
            tdc.createUser(newUser);
            lstUsers.getSelectionModel().select(newUser);
            lstUsers.scrollTo(newUser);
        }
    }

    public void handleDeleteUser(ActionEvent actionEvent) {
        User user = lstUsers.getSelectionModel().getSelectedItem();
        if (user != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Delete this user?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                tdc.deleteUser(user);
            }
        }
    }

    public void handleEditable(ActionEvent actionEvent) {

        //TODO figure out if this could cause errors, i.e. when fields are already un-editable.
        lstUsers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            txtUsername.setEditable(false);
            txtPassword.setEditable(false);
            txtFirstName.setEditable(false);
            txtLastName.setEditable(false);
            txtEmail.setEditable(false);
            txtPhone.setEditable(false);
            chkEditable.setSelected(false);
        });

        if (chkEditable.selectedProperty().getValue()) {
            txtUsername.setEditable(true);
            txtPassword.setEditable(true);
            txtFirstName.setEditable(true);
            txtLastName.setEditable(true);
            txtEmail.setEditable(true);
            txtPhone.setEditable(true);
        }
        if (!chkEditable.selectedProperty().getValue()) {
            txtUsername.setEditable(false);
            txtPassword.setEditable(false);
            txtFirstName.setEditable(false);
            txtLastName.setEditable(false);
            txtEmail.setEditable(false);
            txtPhone.setEditable(false);
        }

    }


    //TODO få add user til at virke


}
