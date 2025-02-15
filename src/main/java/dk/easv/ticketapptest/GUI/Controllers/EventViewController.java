package dk.easv.ticketapptest.GUI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class EventViewController {
    BorderPane root;
    @FXML
    private Label lblTitle;
    @FXML
    private Label lblLocation;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblTime;
    @FXML
    private Label lblDesc;
    @FXML
    private Button btnEditDesc;
    @FXML
    private TextField txtDesc;
    @FXML
    private Label lblTicket;
    @FXML
    private Button btnAddTicket;
    @FXML
    private TableView tblTicket;
    @FXML
    private Label lblCoords;
    @FXML
    private ListView lstCoords;
    @FXML
    private Button btnAddCoord;
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

        @FXML
        public void initialize() {

        //TODO: FIND EN BEDRE MÅDE AT GØRE DET HER PÅ.
            lblTitle.getStyleClass().add("h1");
            lblLocation.getStyleClass().add("h2");
            lblDate.getStyleClass().add("h2");
            lblTime.getStyleClass().add("h2");
            lblDesc.getStyleClass().add("h3");
            lblTicket.getStyleClass().add("h2");
            lblCoords.getStyleClass().add("h2");
            btnEditDesc.getStyleClass().add("button2");
            btnAddTicket.getStyleClass().add("button2");
            btnAddCoord.getStyleClass().add("button2");
            btnReturn.getStyleClass().add("returnButton");
            vboxLeft.getStyleClass().add("vBoxBorder2");
            vboxRight.getStyleClass().add("vBoxBorder2");
    }
}
