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
import java.util.HashMap;
import java.util.Map;
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
    @FXML private Button btnSaveEditUser;

    private TemporaryDataClass tdc;
    private UserManagementModel model;
    private User selectedUser;
    private Map<TextField, String> originalValues = new HashMap<>();
    private final String PASSWORD_PLACEHOLDER = "*****";


    public AdminUserManagementController() {
        try {
            model = new UserManagementModel();
            //TODO IMPLEMENT BETTER EXCEPTION HANDLING HERE.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tdc = new TemporaryDataClass();
        populateUserList();

        btnSaveEditUser.setVisible(false);

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
            if (selectedUser != null) {
                selectedUser.setUsername(newValue);
                checkIfChanged();
            }
        });
        txtPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedUser != null) {
                // Only update the password if the user has typed something new
                if (!newValue.equals(PASSWORD_PLACEHOLDER) && !newValue.isEmpty()) {
                    selectedUser.setPassword(newValue);
                }
                checkIfChanged();
            }
        });
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedUser != null) {
                selectedUser.setEmail(newValue);
                checkIfChanged();
            }
        });
        txtFirstName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedUser != null) {
                selectedUser.setFirstName(newValue);
                checkIfChanged();
            }
        });
        txtLastName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedUser != null) {
                selectedUser.setLastName(newValue);
                checkIfChanged();
            }
        });
        txtPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedUser != null) {
                selectedUser.setPhone(newValue);
                checkIfChanged();
            }
        });
    }

    private void setUserInfo() {
        selectedUser = lstUsers.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Store the original values
            originalValues.put(txtUsername, selectedUser.getUsername());
            // Store the actual password, not the placeholder
            originalValues.put(txtPassword, selectedUser.getPassword());
            originalValues.put(txtFirstName, selectedUser.getFirstName());
            originalValues.put(txtLastName, selectedUser.getLastName());
            originalValues.put(txtEmail, selectedUser.getEmail());
            originalValues.put(txtPhone, selectedUser.getPhone());

            txtUsername.setText(selectedUser.getUsername());
            // Show the placeholder instead of the actual password
            txtPassword.setText(PASSWORD_PLACEHOLDER);
            txtFirstName.setText(selectedUser.getFirstName());
            txtLastName.setText(selectedUser.getLastName());
            txtEmail.setText(selectedUser.getEmail());
            txtPhone.setText(selectedUser.getPhone());
            lblName.setText(selectedUser.getFirstName() + " " + selectedUser.getLastName());
            if (selectedUser.getRole() != null) {
                lblRole.setText(selectedUser.getRole().toString().toLowerCase());
                lblRole.setStyle("-fx-background-color: #DBEBFF");
            } else {
                lblRole.setText("no role");
                lblRole.setStyle("-fx-background-color: #FFE5E5");
            }
            checkIfChanged();
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

    public void handleCreateUser(ActionEvent actionEvent) throws Exception {
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
            //TODO figure out if i am stupid for putting this here and not in the create user window????????????????????? - jonas 18/03
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
        if (chkEditable.isSelected()) {
            txtUsername.setEditable(true);
            txtPassword.setEditable(true);
            txtFirstName.setEditable(true);
            txtLastName.setEditable(true);
            txtEmail.setEditable(true);
            txtPhone.setEditable(true);
        } else {
            txtUsername.setEditable(false);
            txtPassword.setEditable(false);
            txtFirstName.setEditable(false);
            txtLastName.setEditable(false);
            txtEmail.setEditable(false);
            txtPhone.setEditable(false);
            btnSaveEditUser.setVisible(false);
        }
    }

    public void handleSaveEditUser(ActionEvent actionEvent) {
        if (selectedUser != null) {
            String newUsername = txtUsername.getText();
            // Only get the new password if it's not the placeholder
            String newPassword = txtPassword.getText().equals(PASSWORD_PLACEHOLDER) ? originalValues.get(txtPassword) : txtPassword.getText();
            String newFirstName = txtFirstName.getText();
            String newLastName = txtLastName.getText();
            String newEmail = txtEmail.getText();
            String newPhone = txtPhone.getText();

            selectedUser.setUsername(newUsername);
            selectedUser.setPassword(newPassword);
            selectedUser.setFirstName(newFirstName);
            selectedUser.setLastName(newLastName);
            selectedUser.setEmail(newEmail);
            selectedUser.setPhone(newPhone);

            try {
                model.updateUserDB(selectedUser);
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Database Error");
                errorAlert.setHeaderText("Failed to update user in the database.");
                errorAlert.setContentText("An error occurred while trying to update the user's information in the database.");
                errorAlert.showAndWait();
                e.printStackTrace();
                return;
            }

            tdc.updateUser(selectedUser, selectedUser);
            lstUsers.refresh();
            lblName.setText(selectedUser.getFirstName() + " " + selectedUser.getLastName());

            txtUsername.setEditable(false);
            txtPassword.setEditable(false);
            txtFirstName.setEditable(false);
            txtLastName.setEditable(false);
            txtEmail.setEditable(false);
            txtPhone.setEditable(false);
            chkEditable.setSelected(false);
            btnSaveEditUser.setVisible(false);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("User Updated");
            alert.setHeaderText(null);
            alert.setContentText("User information has been updated.");
            alert.showAndWait();
        }

    }
    private void checkIfChanged() {
        if (!chkEditable.isSelected()){
            btnSaveEditUser.setVisible(false);
            return;
        }
        boolean changed = false;
        if (selectedUser != null) {
            if (!txtUsername.getText().equals(originalValues.get(txtUsername))) {
                changed = true;
            }
            if (!txtPassword.getText().equals(originalValues.get(txtPassword))) {
                changed = true;
            }
            if (!txtFirstName.getText().equals(originalValues.get(txtFirstName))) {
                changed = true;
            }
            if (!txtLastName.getText().equals(originalValues.get(txtLastName))) {
                changed = true;
            }
            if (!txtEmail.getText().equals(originalValues.get(txtEmail))) {
                changed = true;
            }
            if (!txtPhone.getText().equals(originalValues.get(txtPhone))) {
                changed = true;
            }
        }
        btnSaveEditUser.setVisible(changed);
    }
}

