package dk.easv.ticketapptest.GUI.Controllers;
//project imports
import dk.easv.ticketapptest.BE.Role;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;
import dk.easv.ticketapptest.BLL.Exceptions.UsernameAlreadyExistsException;
import dk.easv.ticketapptest.BLL.SessionManager;
import dk.easv.ticketapptest.GUI.AlertClass;
import dk.easv.ticketapptest.GUI.Models.UserManagementModel;
import dk.easv.ticketapptest.GUI.Models.UserModel;
//java imports
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.net.URL;
import java.util.*;


public class AdminUserManagementController implements Initializable {
    
    @FXML
    public TextField txtUserSearch;
    @FXML
    public ImageView imgProfilePicture;
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
        } catch (EasvTicketException e) {
            e.printStackTrace();
            AlertClass.alertError("Error", "if you see this message, good luck!");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
        });
        
        userChangeListeners();
    }

    private void makeImageViewCircular() {
        double radius = 40;
        imgProfilePicture.setFitHeight(radius * 2);
        imgProfilePicture.setFitWidth(radius * 2);
        imgProfilePicture.setPreserveRatio(false);
        imgProfilePicture.setSmooth(true);

        Circle circle = new Circle(radius, radius, radius);
        imgProfilePicture.setClip(circle);
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

            String relativeImagePath = selectedUser.getImgFilePath();
            String defaultImagePath = Objects.requireNonNull(getClass().getResource("/defaultImage.png")).toExternalForm();
            try {
                File imageFile = new File(System.getProperty("user.dir") + File.separator + relativeImagePath);
                if (!imageFile.exists()) {
                    //might not be optimal, but this throw is handled 4 lines below
                    throw new EasvTicketException("Image file not found");
                }
                imgProfilePicture.setImage(new Image(imageFile.toURI().toString()));

                //throw handled here, sets default image
            } catch (EasvTicketException e) {
                imgProfilePicture.setImage(new Image(defaultImagePath));
            }
            makeImageViewCircular();

            resetFieldStyles();

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

    public void resetFieldStyles() {
        txtUsername.setStyle("");
        txtPassword.setStyle("");
        txtFirstName.setStyle("");
        txtLastName.setStyle("");
        txtEmail.setStyle("");
        txtPhone.setStyle("");
    }

    public void populateUserList() {
        ObservableList<User> userList = FXCollections.observableArrayList();
        try {
            userList = userModel.getUsers();
        } catch (EasvTicketException e) {
            e.printStackTrace();
            AlertClass.alertError("Error", "Error loading user list");
        }

        if (userList == null) {
            AlertClass.alertError("Error", "Error loading user list");
            return;
        }

        //sort list alphabetically by first name -- compares Users by firstname (defaults to alphabetical)
        userList.sort(Comparator.comparing(user -> user.getFirstName().toLowerCase()));

        lstUsers.setItems(userList);
        setupCellFactory();
    }

    public void setupCellFactory() {
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
                double radius = 30;

                ImageView imageView = new ImageView();
                imageView.setFitHeight(radius * 2);
                imageView.setFitWidth(radius * 2);
                imageView.setPreserveRatio(false);
                imageView.setSmooth(true);

                Circle circle = new Circle(radius, radius, radius);
                imageView.setClip(circle);

                String relativeImagePath = item.getImgFilePath();
                //if the filepath is not null create a file, otherwise set the imageFile to null
                //need to get more comfortable using ternary operator, it is super handy
                File imageFile = relativeImagePath != null ? new File(System.getProperty("user.dir"), relativeImagePath) : null;

                if (imageFile != null && imageFile.exists()) {
                    imageView.setImage(new Image(imageFile.toURI().toString()));
                } else {
                    String defaultImagePath = Objects.requireNonNull(getClass().getResource("/defaultImage.png")).toExternalForm();
                    imageView.setImage(new Image(defaultImagePath));
                }

                HBox content = new HBox(imageView, vbox);
                content.setSpacing(10);

                setText(null);
                //used to display a node inside the table cell.
                setGraphic(content);

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

    public void handleCreateUser(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admin-user-creation-view.fxml"));
        User newUser = null;
        try {
            Parent root = loader.load();
            AdminCreateUserController controller = loader.getController();

            Stage stage = new Stage();
            String imgPath = Objects.requireNonNull(getClass().getResource("/BASW_logo2.png")).toExternalForm();
            stage.getIcons().add(new Image(imgPath));
            stage.setTitle("Create User");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            newUser = controller.getCreatedUser();
        } catch (Exception e) {
            e.printStackTrace();
            AlertClass.alertError("Loading crash", "An error occurred while loading popup window");
        }

        //this should probably be implemented in the createUserController instead, but at least it makes updating the list easy :)
        if (newUser != null) {
            try {
                userManagementModel.createUserDB(newUser);
                //catch UserNameAlreadyExists if that's the case, otherwise catch the generic exception
            } catch (UsernameAlreadyExistsException e) {
                e.printStackTrace();
                AlertClass.alertError("Username Already Exists", "An error occurred while creating user\nThe username already exists!");
                return;
            } catch (Exception e) {
                e.printStackTrace();
                AlertClass.alertError("Something went wrong", "An error occurred while creating user");
            }
            lstUsers.getItems().add(newUser);
            //resort the list with the new user
            FXCollections.sort(lstUsers.getItems(), Comparator.comparing(User::getFirstName, String.CASE_INSENSITIVE_ORDER));
            lstUsers.getSelectionModel().select(newUser);
            lstUsers.scrollTo(newUser);
        }
    }

    public void handleDeleteUser(ActionEvent actionEvent) {
        User user = lstUsers.getSelectionModel().getSelectedItem();
        if (user.equals(SessionManager.getInstance().getCurrentUser())) {
            AlertClass.alertWarning("Dont do that", "You cannot delete your own account.");
            return;

        }
        if (user != null) {
            Optional<ButtonType> result = AlertClass.alertConfirmation("Delete User", "Are you sure you want to delete this user: " + user.getUsername());
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    //TODO check if this causes errors on an empty list, not going to try right now -- jonas 25/03
                    userManagementModel.deleteUser(user);
                    lstUsers.getItems().remove(user);
                    if (!lstUsers.getItems().isEmpty()) {
                        lstUsers.getSelectionModel().select(0);
                        User defaultUser = lstUsers.getSelectionModel().getSelectedItem();
                        setUserInfo(defaultUser);
                    }
                } catch (EasvTicketException e) {
                    e.printStackTrace();
                    AlertClass.alertError("Delete Error", "An error occurred while deleting user");
                }

            }
        }
    }

    public void handleSaveEditUser(ActionEvent actionEvent) {
        if (selectedUser != null && hasChanged) {

            if (!highlightFields()) {
                AlertClass.alertWarning("Error", "Please ensure all fields are filled and dont contain spaces");
                return;
            }

            String newUsername = txtUsername.getText();
            String newPassword = txtPassword.getText().equals(PASSWORD_PLACEHOLDER) ? originalValues.get(txtPassword) : txtPassword.getText();
            String newFirstName = txtFirstName.getText();
            String newLastName = txtLastName.getText();
            String newEmail = txtEmail.getText();
            String newPhone = txtPhone.getText();
            int userId = selectedUser.getId();

            selectedUser.setUsername(newUsername);
            selectedUser.setPassword(newPassword);
            selectedUser.setFirstName(newFirstName);
            selectedUser.setLastName(newLastName);
            selectedUser.setEmail(newEmail);
            selectedUser.setPhone(newPhone);

            try {
                userManagementModel.updateUserDB(selectedUser, userId);
                // Update original values after successful update
                originalValues.put(txtUsername, newUsername);
                originalValues.put(txtPassword, newPassword);
                originalValues.put(txtFirstName, newFirstName);
                originalValues.put(txtLastName, newLastName);
                originalValues.put(txtEmail, newEmail);
                originalValues.put(txtPhone, newPhone);
                hasChanged = false;
            } catch (EasvTicketException e) {
                e.printStackTrace();
                AlertClass.alertError("Save Error", "An error occurred while updating user");
                return;
            }

            lstUsers.refresh();
            lblName.setText(selectedUser.getFirstName() + " " + selectedUser.getLastName());

            btnSaveEditUser.setVisible(false);

            AlertClass.alertInfo("User updated", "The user has successfully been updated");
        }
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
        if(txtPhone.getText().trim().isEmpty()) {
            txtPhone.setStyle(style);
            allValid = false;
        }
        return allValid;
    }


    private void checkIfChanged() {
        btnSaveEditUser.setVisible(hasChanged);
    }

    public void searchUser() {

        String searchQuery = txtUserSearch.getText();
        ObservableList<User> users = null;

        if (txtUserSearch.getText().isEmpty()) {
            try {
                users = userModel.getUsers();
                users.sort(Comparator.comparing(user -> user.getFirstName().toLowerCase()));
                lstUsers.setItems(users);
                return;
            } catch (EasvTicketException e) {
                e.printStackTrace();
                AlertClass.alertError("Search Error", "An error occurred while searching users");
            }
        }
        //TODO test if anything here dies if the user list is empty

        //create a new task
        Task<ObservableList<User>> searchTask = new Task<>() {
            @Override
            //tell the task what to do
            protected ObservableList<User> call() throws EasvTicketException {
                return userManagementModel.searchUser(searchQuery);
            }
        };

        //if the task succeeds filter the list view
        searchTask.setOnSucceeded(event -> {
            ObservableList<User> filteredList = searchTask.getValue();
            if (filteredList != null) {
                SortedList<User> sortedList = new SortedList<>(filteredList, Comparator.comparing(user -> user.getFirstName().toLowerCase()));
                lstUsers.setItems(sortedList);
            } else {
                lstUsers.setItems(FXCollections.observableArrayList());
            }
        });

        //if the task fails, show the user theres a db issue.
        searchTask.setOnFailed(event -> {
            Throwable error = searchTask.getException();
            error.printStackTrace();
            AlertClass.alertError("Search Error","An error occurred while searching for users");
        });

        //put the task in a new thread and run it. -- technically should make something to prevent creating loads of threads
        //but the search debounce is probably fine.
        Thread thread = new Thread(searchTask);
        //doesnt block jvm from exiting, despite the task still running.
        thread.setDaemon(true);
        thread.start();
    }
}

