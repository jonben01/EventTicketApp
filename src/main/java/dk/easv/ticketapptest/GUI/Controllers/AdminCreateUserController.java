package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Role;
import dk.easv.ticketapptest.BE.User;
import dk.easv.ticketapptest.BLL.Exceptions.EasvTicketException;
import dk.easv.ticketapptest.GUI.AlertClass;
import dk.easv.ticketapptest.GUI.Models.ImageUploader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminCreateUserController implements Initializable {
    @FXML
    public javafx.scene.layout.VBox VBox;
    @FXML
    public ImageView imgCreateProfile;
    @FXML
    public Button btnPicture;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPhone;
    @FXML
    private Button btnCreateNewUser;
    @FXML
    private CheckBox chkAdmin;
    @FXML
    private CheckBox chkCoordinator;

    private User newUser;

    private String imagePath = "src/main/resources/userImages/defaultImage.png";
    private ImageUploader imageUploader;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageUploader = new ImageUploader();
        makeImageViewCircular();
        String defaultImagePath = Objects.requireNonNull(getClass().getResource("/defaultImage.png")).toExternalForm();
        imgCreateProfile.setImage(new Image(defaultImagePath));

        txtUsername.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 30) {
                txtUsername.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
            } else {
                txtUsername.setStyle("");
            }
        });
    }

    /**
     * method to make the user profile image view circular
     */
    private void makeImageViewCircular() {
        double radius = 40;
        imgCreateProfile.setFitHeight(radius * 2);
        imgCreateProfile.setFitWidth(radius * 2);
        imgCreateProfile.setPreserveRatio(false);
        imgCreateProfile.setSmooth(true);

        //shape
        Circle circle = new Circle(radius, radius, radius);
        imgCreateProfile.setClip(circle);
    }

    /**
     * creates a new user object if input is valid
     * @param actionEvent button press
     */
    public void handleCreateNewUser(ActionEvent actionEvent) {

        //input validation
        if(!highlightFields()) {
            return;
        }
        Role role = getRole();
        //forces user to pick a role
        if (role == null) {
            AlertClass.alertInfo("Missing information", "Please select a role");
            return;
        }
        String username = this.txtUsername.getText().trim();
        String password = this.txtPassword.getText().trim();
        String firstName = this.txtFirstName.getText().trim();
        String lastName = this.txtLastName.getText().trim();
        String email = this.txtEmail.getText().trim();
        String phone = this.txtPhone.getText().trim();

        //try to upload the selected image file path
        try {
            imagePath = imageUploader.uploadFile(imagePath);
        } catch (EasvTicketException e) {
            e.printStackTrace();
            AlertClass.alertError("Error", "Failed to upload file");
            return;
        }
        //the new user
        newUser = new User(username, password, firstName, lastName, email, phone, role, imagePath);
        //set the new user to be created??? maybe this should just do that on its own?
        setCreatedUser(newUser);
        Stage stage = (Stage) btnCreateNewUser.getScene().getWindow();
        stage.close();
    }

    /**
     *  Method to visually show the user a missing role, or to pass a selected role to the new user
     * @return a role for the new user object
     */
    private Role getRole() {
        //visual input validation
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

    /**
     * checks if user input is valid, shows a custom alert if it isn't
     * @return boolean check
     */
    private boolean highlightFields() {
        //pretty bloated but i am not smart enough to make a flexible method instead apparently :) - jonas 03/04
        boolean allValid = true;
        String style = "-fx-border-color: red; -fx-border-width: 1px;";
        StringBuilder errorMessages = new StringBuilder();

        if(txtUsername.getText().trim().isEmpty() || txtUsername.getText().contains(" ") || txtUsername.getText().length() > 30) {
            txtUsername.setStyle(style);
            allValid = false;
            if (txtUsername.getText().trim().isEmpty()) {
                errorMessages.append("Username is required\n");
            }
            if (txtUsername.getText().contains(" ")) {
                errorMessages.append("Username cannot contain spaces\n");
            }
            if (txtUsername.getText().length() > 30) {
                errorMessages.append("Username cannot be longer than 30 characters\n");
            }
        } else {
            txtUsername.setStyle("");
        }
        if(txtPassword.getText().trim().isEmpty() || txtPassword.getText().contains(" ")) {
            txtPassword.setStyle(style);
            allValid = false;
            if (txtPassword.getText().trim().isEmpty()) {
                errorMessages.append("Password is required\n");
            }
            if (txtPassword.getText().contains(" ")) {
                errorMessages.append("Password cannot contain spaces\n");
            }
        } else {
            txtPassword.setStyle("");
        }
        if(txtFirstName.getText().trim().isEmpty()) {
            txtFirstName.setStyle(style);
            allValid = false;
            if (txtFirstName.getText().trim().isEmpty()) {
                errorMessages.append("First name is required\n");
            }
        } else {
            txtFirstName.setStyle("");
        }
        if(txtLastName.getText().trim().isEmpty()) {
            txtLastName.setStyle(style);
            allValid = false;
            if (txtLastName.getText().trim().isEmpty()) {
                errorMessages.append("Last name is required\n");
            }
        } else {
            txtLastName.setStyle("");
        }
        if(txtEmail.getText().trim().isEmpty() || txtEmail.getText().contains(" ")) {
            txtEmail.setStyle(style);
            allValid = false;
            if (txtEmail.getText().trim().isEmpty()) {
                errorMessages.append("Email is required\n");
            }
            if (txtEmail.getText().contains(" ")) {
                errorMessages.append("Email cannot contain spaces\n");
            }
        } else {
            txtEmail.setStyle("");
        }
        if(txtPhone.getText().trim().isEmpty()) {
            txtPhone.setStyle(style);
            allValid = false;
            if (txtPhone.getText().trim().isEmpty()) {
                errorMessages.append("Phone is required\n");
            }
        } else {
            txtPhone.setStyle("");
        }
        if (!allValid) {
            AlertClass.alertError("invalid input", errorMessages.toString());
        }
        return allValid;
    }


    private void setCreatedUser(User newUser) {
        this.newUser = newUser;
    }
    public User getCreatedUser() {
        return newUser;
    }

    /**
     * @param actionEvent checkbox for role
     */
    public void handleSetAdmin(ActionEvent actionEvent) {
        if (chkCoordinator.isSelected()) {
            chkCoordinator.setSelected(false);
        }
    }

    /**
     * @param actionEvent checkbox for role
     */
    public void handleSetCoordinator(ActionEvent actionEvent) {
        if (chkAdmin.isSelected()) {
            chkAdmin.setSelected(false);
        }
    }

    /**
     * misleading name, this just runs a filechooser to select an image, and then makes it circular
     * @param actionEvent filechooser button press
     */
    public void handleUploadPicture(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Picture");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG or JPEG", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {

            //set the path for the new user
            imagePath = file.getAbsolutePath();
            imgCreateProfile.setImage(new Image(file.toURI().toString()));
            makeImageViewCircular();
        }

    }
}
