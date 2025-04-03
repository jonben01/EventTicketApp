package dk.easv.ticketapptest.GUI;
//java imports
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class AlertClass {

    public static Optional<ButtonType> showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    public static void alertInfo(String title, String message) {
        showAlert(Alert.AlertType.INFORMATION, title, message);
    }

    public static Optional<ButtonType> alertConfirmation(String title, String message) {
        return showAlert(Alert.AlertType.CONFIRMATION, title, message);
    }

    public static void alertWarning(String title, String message) {
        showAlert(Alert.AlertType.WARNING, title, message);
    }

    public static void alertError(String title, String message) {
        showAlert(Alert.AlertType.ERROR, title, message);
    }


}
