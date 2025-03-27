package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Role;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.Exceptions.UsernameAlreadyExistsException;
import dk.easv.ticketapptest.BLL.SessionManager;
import dk.easv.ticketapptest.GUI.Models.UserManagementModel;
import dk.easv.ticketapptest.GUI.Models.UserModel;
import dk.easv.ticketapptest.GUI.TemporaryDataClass;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
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
import javafx.util.Duration;


import java.net.URL;
import java.util.*;

public class AdminUserManagementController implements Initializable {

    @FXML
    public Button btnSwapRole;
    @FXML
    public TextField txtUserSearch;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtFirstName;
    @FXML
    private Label lblRole;
    @FXML
    private Label lblName;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private CheckBox chkEditable;
    @FXML
    private ListView<User> lstUsers;
    @FXML
    private Button btnSaveEditUser;

    private TemporaryDataClass tdc;
    private UserManagementModel userManagementModel;
    private User selectedUser;
    private Map<TextField, String> originalValues = new HashMap<>();
    private final String PASSWORD_PLACEHOLDER = "*****";
    private UserModel userModel;
    private boolean hasChanged = false;

    private PauseTransition searchDebounce;



    public AdminUserManagementController() {
        try {
            userModel = new UserModel();
            userManagementModel = new UserManagementModel();
            //TODO IMPLEMENT BETTER EXCEPTION HANDLING HERE.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //tdc = new TemporaryDataClass();
        populateUserList();

        btnSaveEditUser.setVisible(false);

        if (lstUsers.getItems() != null) {
            lstUsers.getSelectionModel().select(0);
            User user = lstUsers.getSelectionModel().getSelectedItem();
            setUserInfo(user);
        }

        //Debounce the search function -- to stop the user from creating loads of new threads in the search method.
        searchDebounce = new PauseTransition(Duration.millis(200));
        searchDebounce.setOnFinished(event -> {
           searchUser();
        });

        txtUserSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchDebounce.stop();
            searchDebounce.playFromStart();
        });

        lstUsers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setUserInfo(newValue);
            //TODO check if this works
            if (newValue == SessionManager.getInstance().getCurrentUser()) {
                btnSwapRole.setDisable(true);
            } else if (btnSwapRole.isDisable()) {
                btnSwapRole.setDisable(false);
            }
        });


        //TODO IMPLEMENT USE OF BEANS OR USERDATA FOR ACTUAL PROJECT
        // SO THERE ISNT 6 LISTENERS DOING THE SAME THING
        userChangeListeners();
    }



    //TODO REFACTOR userChangeListeners -- helper method that creates a listener for a textfield
    //TODO call it 6 times in userChangeListeners instead.
    /*
    //SETTER SHOULD BE RELEVANT TO THE TEXT FIELD.
    private void addChangeListener(TextField textField, Consumer<String> setter) {
    //listens to the text field for changes
    textField.textProperty().addListener((obs, oldValue, newValue) -> {
        if (selectedUser != null) {
        //use the setter param to set the new value
            setter.accept(newValue);
            checkIfChanged();
        }
    });
}
     */
    private void userChangeListeners() {
        txtUsername.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedUser != null) {
                hasChanged = true;
                checkIfChanged();
            }
        });
        txtPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedUser != null) {
                if (!newValue.equals(PASSWORD_PLACEHOLDER) && !newValue.isEmpty()) {
                    hasChanged = true;
                }
                checkIfChanged();
            }
        });
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedUser != null) {
                hasChanged = true;
                checkIfChanged();
            }
        });
        txtFirstName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedUser != null) {
                hasChanged = true;
                checkIfChanged();
            }
        });
        txtLastName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedUser != null) {
                hasChanged = true;
                checkIfChanged();
            }
        });
        txtPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedUser != null) {
                hasChanged = true;
                checkIfChanged();
            }
        });
    }

    private void setUserInfo(User selectedUser) {
        if (selectedUser != null) {
            this.selectedUser = selectedUser;
            originalValues.clear();
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

            //TODO change this. Not sure what to call it, if role name is wanted, make if statements
            btnSwapRole.setText("PLACEHOLDER :)");

            if (selectedUser.getRole() != null) {
                lblRole.setText(selectedUser.getRole().toString().toLowerCase());
                lblRole.setStyle("-fx-background-color: #DBEBFF");
            } else {
                lblRole.setText("no role");
                lblRole.setStyle("-fx-background-color: #FFE5E5");
            }
            hasChanged = false;
            checkIfChanged();
        }
    }

    //TODO instead of setting style on the labels, make two different style classes
    // because it is VERY annoying to change anything now.

    public void populateUserList() {
        lstUsers.setItems(userModel.getUsers());
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
            try {
                userManagementModel.createUserDB(newUser);
            } catch (UsernameAlreadyExistsException e) {
                //TODO probable use the error message that was bubbled up from DAO.
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Username already exists!");
                alert.showAndWait();
                return;
            }
            lstUsers.getSelectionModel().select(newUser);
            lstUsers.scrollTo(newUser);
        }
    }

    public void handleDeleteUser(ActionEvent actionEvent) throws Exception {
        User user = lstUsers.getSelectionModel().getSelectedItem();
        if (user != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Delete this user?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    //TODO check if this causes errors on an empty list, not going to try right now -- jonas 25/03
                    userManagementModel.deleteUser(user);
                    lstUsers.getItems().remove(user);
                    if (!lstUsers.getItems().isEmpty()) {
                        lstUsers.getSelectionModel().select(0);
                        User defaultUser = lstUsers.getSelectionModel().getSelectedItem();
                        setUserInfo(defaultUser);
                    }


                    //TODO improve exception handling
                } catch (Exception e) {
                    //show alert here
                }

            }
        }
    }
/*

    //TODO remove this
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

 */

    public void handleSaveEditUser(ActionEvent actionEvent) {
        if (selectedUser != null && hasChanged) {
            String newUsername = txtUsername.getText();
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
                userManagementModel.updateUserDB(selectedUser);
                // Update original values after successful update
                originalValues.put(txtUsername, newUsername);
                originalValues.put(txtPassword, newPassword);
                originalValues.put(txtFirstName, newFirstName);
                originalValues.put(txtLastName, newLastName);
                originalValues.put(txtEmail, newEmail);
                originalValues.put(txtPhone, newPhone);
                hasChanged = false;
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Database Error");
                errorAlert.setHeaderText("Failed to update user in the database.");
                errorAlert.setContentText("An error occurred while trying to update the user's information in the database.");
                errorAlert.showAndWait();
                e.printStackTrace();
                return;
            }

            lstUsers.refresh();
            lblName.setText(selectedUser.getFirstName() + " " + selectedUser.getLastName());

            btnSaveEditUser.setVisible(false);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("User Updated");
            alert.setHeaderText(null);
            alert.setContentText("User information has been updated.");
            alert.showAndWait();
        }

    }
    private void checkIfChanged() {
        btnSaveEditUser.setVisible(hasChanged);
    }


    //TODO move the btnSwapRole text to setUserInfo instead, so it changes when u press
    public void handleSwapRole(ActionEvent actionEvent) throws Exception {
        User user = lstUsers.getSelectionModel().getSelectedItem();
        System.out.println(user);
        if (user != null && Objects.equals(SessionManager.getInstance().getCurrentUser().getUsername(), user.getUsername())) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("You can't change your own role");
            alert.showAndWait();
            return;
        }

        //TODO use alert class
        if (user != null && !Objects.equals(SessionManager.getInstance().getCurrentUser().getUsername(), user.getUsername())) {
            userModel.editRole(user);
            lstUsers.refresh();
            if (user.getRole() == Role.ADMIN) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to swap " + user.getFirstName() + " " + user.getLastName() + "to Event coordinator?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    user.setRole(Role.COORDINATOR);
                    lblRole.setText("coordinator");
                }

            } else if (user.getRole() == Role.COORDINATOR) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to swap " + user.getFirstName() + " " + user.getLastName() + "to Admin?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    user.setRole(Role.ADMIN);
                    lblRole.setText("admin");
                }
            }
        }
    }

    public void searchUser() {

        String searchQuery = txtUserSearch.getText();

        if (txtUserSearch.getText().isEmpty()) {
            lstUsers.setItems(userModel.getUsers());
            return;
        }
        //create a new task
        Task<ObservableList<User>> searchTask = new Task<>() {
            @Override
            //tell the task what to do
            protected ObservableList<User> call() throws Exception {
                return userManagementModel.searchUser(searchQuery);
            }
        };

        //if the task succeeds filter the list view
        searchTask.setOnSucceeded(event -> {
            ObservableList<User> filteredList = searchTask.getValue();
            if (filteredList != null) {
                SortedList<User> sortedList = new SortedList<>(filteredList);
                lstUsers.setItems(sortedList);
            } else {
                lstUsers.setItems(FXCollections.observableArrayList());
            }
        });

        //TODO change this
        //if the task fails, show the user theres a db issue.
        searchTask.setOnFailed(event -> {
            Throwable error = searchTask.getException();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText(error.getMessage());
            alert.showAndWait();
        });

        //put the task in a new thread and run it. -- technically should make something to prevent creating loads of threads
        //but the search debounce is probably fine.
        Thread thread = new Thread(searchTask);
        //doesnt block jvm from exiting, despite the task still running.
        thread.setDaemon(true);
        thread.start();
    }
}

