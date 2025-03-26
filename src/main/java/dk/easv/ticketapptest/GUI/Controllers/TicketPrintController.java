package dk.easv.ticketapptest.GUI.Controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Location;
import dk.easv.ticketapptest.BE.Ticket;
import dk.easv.ticketapptest.BLL.PdfGeneratorUtil;
import dk.easv.ticketapptest.BLL.QRImageUtil;
import dk.easv.ticketapptest.DAL.TicketDataStore;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private TableView<Ticket> tblTicket;
    @FXML
    private TableColumn<Ticket, String> colName;
    @FXML
    private TableColumn<Ticket, String> colDescription;
    @FXML
    private TableColumn<Ticket, Double> colPrice;
    @FXML
    private TextField txtCustomerFirstName;
    @FXML
    private TextField txtCustomerLastName;
    @FXML
    private TextField txtCustomerEmail;
    @FXML
    private TextField txtCustomerPhone;

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

    private Event2 selectedEvent;


    @FXML
    public void initialize() {

        tblTicket.getStylesheets().add("css/admineventstyle.css");
        tblTicket.getStyleClass().add("table-view");


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

        tblTicket.setItems(TicketDataStore.getInstance().getTickets());
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTicketName()));
        colDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        colPrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());

    }

    public void setEventDetails(Event2 event){
        this.lblTitle.setText(event.getTitle());
        this.lblLocationTicket.setText(event.getLocation().getAddress() + ", " + event.getLocation().getCity());
        this.lblDateTicket.setText(event.getStartDate() + " - " + event.getEndDate());
        this.lblTimeTicket.setText(event.getStartTime() + " - " + event.getEndTime());
        this.selectedEvent = event;

        for(Ticket ticket : selectedEvent.getTicketTypes()){
            tblTicket.getItems().add(ticket);
        }

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


    public Event2 getSelectedEvent(){
        return selectedEvent;
    }


    public void addTicket(Ticket ticket) {
        tblTicket.getItems().add(ticket);
    }

    public void onHandlePrintTicket(ActionEvent actionEvent) {
        Ticket selectedTicket = tblTicket.getSelectionModel().getSelectedItem();

        if(selectedTicket != null && txtCustomerFirstName.getText() != null && txtCustomerLastName.getText() != null && txtCustomerEmail.getText() != null && txtCustomerPhone.getText() != null) {

            System.out.println("Ticket Printed: " + selectedTicket.getTicketName() + " for " + txtCustomerFirstName.getText());
            try {
                String rndString = generateRandomString();
                String qrFilePath = "src/main/resources/images/" + rndString + ".png";
                // THIS IS GOD: PdfGeneratorUtil.generateTicket("C:/Users/melon/Downloads/test.pdf", "test");

                generateQRCode(rndString, qrFilePath);
                PdfGeneratorUtil.generateTicket("target/PrintedTickets/" + rndString + ".pdf", "This is a cool ticket.", qrFilePath);
                File file = new File(qrFilePath);
                file.delete();
            }
            catch (Exception e) {
                System.err.println("Problem printing PDF: " + e);
            }
        }
    }
        //TODO: TEMP KLASSE. MAKE THIS STUFF ACTUALLY GOOD.
    public static String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        int length = random.nextInt(150);
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }

        return randomString.toString();
    }

    private void generateQRCode(String data, String filePath) {
        File file = new File(filePath);
        file.getParentFile().mkdirs(); // Creates the parent directories if they don't exist

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);
            ImageIO.write(QRImageUtil.toBufferedImage(bitMatrix), "PNG", file);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }




}

