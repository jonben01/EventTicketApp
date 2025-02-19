package dk.easv.ticketapptest.GUI.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class TicketPrintController {

    BorderPane root;
    TicketManagementController parent;
    @FXML
    private Label lblTitle;
    @FXML
    private Label lblLocationTicket;
    @FXML
    private Label lblDateTicket;
    @FXML
    private Label lblTimeTicket;
    @FXML
    private Label lblTicket;
    @FXML
    private TableView tblTicket;
    @FXML
    private Label lblCoords;
    @FXML
    private ListView lstCoords;
    @FXML
    private Button btnPrintTicket;
    @FXML
    private Button btnReturn;
    @FXML
    private VBox vboxLeft;
    @FXML
    private VBox vboxRight;

    public void setPanel(BorderPane root)
    {
        this.root = root;
    }
    public void setParent(TicketManagementController parent)
    {
     this.parent = parent;
    }

    @FXML
    public void initialize() {

        //TODO: FIND EN BEDRE MÅDE AT GØRE DET HER PÅ.
        lblTitle.getStyleClass().add("h1");
        lblLocationTicket.getStyleClass().add("h2");
        lblDateTicket.getStyleClass().add("h2");
        lblTimeTicket.getStyleClass().add("h2");
        lblTicket.getStyleClass().add("h2");
        lblCoords.getStyleClass().add("h2");
        btnPrintTicket.getStyleClass().add("button2");
        btnReturn.getStyleClass().add("returnButton");
        vboxLeft.getStyleClass().add("vBoxBorder2");
        vboxRight.getStyleClass().add("vBoxBorder2");
        btnReturn.setCursor(javafx.scene.Cursor.HAND);
    }

    public void setEventDetails(String title, String location, String date, String time){
        this.lblTitle.setText(title);
        this.lblLocationTicket.setText(location);
        this.lblDateTicket.setText(date);
        this.lblTimeTicket.setText(time);
    }


    @FXML
    private void handleReturn(ActionEvent actionEvent) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ticket-management-view.fxml"));
                Parent ticketManagementView = fxmlLoader.load();
                TicketManagementController controller = fxmlLoader.getController();
                controller.setPanel(root);
                root.setCenter(ticketManagementView);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @FXML
    private void handleAddTicketType(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/create-ticket-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Create Ticket");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading create-ticket-view.fxml: " + e.getMessage());
        }
    }

}

