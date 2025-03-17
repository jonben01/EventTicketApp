package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Role;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.GUI.Models.UserManagementModel;
import dk.easv.ticketapptest.GUI.TemporaryDataClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
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
    private UserManagementModel model;


    public AdminUserManagementController() {
        model = new UserManagementModel();
    }

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
                lblRole.setText(user.getRole().toString().toLowerCase());
                lblRole.setStyle("-fx-background-color: #DBEBFF");
            } else {
                lblRole.setText("no role");
                lblRole.setStyle("-fx-background-color: #FFE5E5");
            }
        }
    }

    //TODO instead of setting style on the labels, make two different style classes
    // because it is VERY annoying to change anything now.

    public void populateUserList() {
        lstUsers.setItems(tdc.getUsers());
        lstUsers.setCellFactory(param -> new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);

                //clear nodes, in case refreshing leaves visual artifacts.
                setText(null);
                setGraphic(null);

                if (empty || item == null) {
                    return;
                }

                //create labels for full name and user role
                Label nameLabel = new Label(item.getFirstName() + " " + item.getLastName());
                Label roleLabel;
                if (item.getRole() != null) {
                    roleLabel = new Label(item.getRole().toString().toLowerCase());

                } else {
                    roleLabel = new Label("no role");
                    roleLabel.setStyle("-fx-background-color: #FFE5E5 ;");
                }


                nameLabel.getStyleClass().add("name-label");

                roleLabel.getStyleClass().add("role-label");

                //create a vbox containing both labels.
                VBox vbox = new VBox(nameLabel, roleLabel);
                vbox.setSpacing(2);

                setText(null);
                //used to display a node inside the table cell.
                setGraphic(vbox);

                if (getIndex() == 0) {
                    if (!getStyleClass().contains("first-visible")) {
                        getStyleClass().add("first-visible");
                    }
                } else {
                    getStyleClass().remove("first-visible");
                }
            }
        });
    }

    public void handleSetEventCoordinator(ActionEvent actionEvent) {
        User user = lstUsers.getSelectionModel().getSelectedItem();
        if (user != null) {
            if (user.getRole() == Role.COORDINATOR) {
                user.setRole(null);
                lblRole.setText("no role");
                lblRole.setStyle("-fx-background-color: #FFE5E5 ;");
                lstUsers.refresh();
            } else if (user.getRole() == Role.ADMIN) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Set this user to COORDINATOR instead of ADMIN?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    user.setRole(Role.COORDINATOR);
                    lblRole.setText(user.getRole().toString().toLowerCase());
                    lblRole.setStyle("-fx-background-color: #DBEBFF ;");
                    lstUsers.refresh();
                }
            } else {
                user.setRole(Role.COORDINATOR);
                lblRole.setText(user.getRole().toString().toLowerCase());
                lblRole.setStyle("-fx-background-color: #DBEBFF ;");
                lstUsers.refresh();
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
                    lblRole.setText("no role");
                    lblRole.setStyle("-fx-background-color: #FFE5E5 ;");
                    lstUsers.refresh();
                }
            } else if (user.getRole() == Role.COORDINATOR) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Set this user to ADMIN instead of COORDINATOR?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    user.setRole(Role.ADMIN);
                    lblRole.setText(user.getRole().toString().toLowerCase());
                    lblRole.setStyle("-fx-background-color: #DBEBFF ;");
                    lstUsers.refresh();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to make this user ADMIN?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    user.setRole(Role.ADMIN);
                    lblRole.setText(user.getRole().toString().toLowerCase());
                    lblRole.setStyle("-fx-background-color: #DBEBFF");
                    lstUsers.refresh();
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
            model.createUserDB(newUser);
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

}
