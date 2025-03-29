package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Customer;
import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.BE.Ticket;
import dk.easv.ticketapptest.BLL.util.Gmailer;
import dk.easv.ticketapptest.BLL.util.PdfGeneratorUtil;
import dk.easv.ticketapptest.BLL.util.QRImageUtil;
import dk.easv.ticketapptest.DAL.TicketDataStore;
import dk.easv.ticketapptest.GUI.AlertClass;
import dk.easv.ticketapptest.GUI.Models.TicketModel;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Random;

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

    private TicketModel ticketModel;

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

    private Gmailer gMailer;

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
    public void initialize() throws IOException, GeneralSecurityException {
        ticketModel = new TicketModel();
        gMailer = new Gmailer();

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
                AlertClass.alertError("Error", "An error occurred while returning");
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

        if (selectedTicket == null) {
            AlertClass.alertInfo("Missing ticket", "Please select a ticket to print");
            return;
        }

        if (txtCustomerFirstName.getText().isEmpty() || txtCustomerLastName.getText().isEmpty() || txtCustomerEmail.getText().isEmpty() || txtCustomerPhone.getText().isEmpty()) {
            AlertClass.alertInfo("Missing information", "Please fill out all customer details.");
            return;
        }

        System.out.println("Ticket Printed: " + selectedTicket.getTicketName() + " for " + txtCustomerFirstName.getText());
        try {
            String rndString = generateRandomString();
            String qrFilePath = "src/main/resources/images/" + rndString + "_qr.png";
            String barcodeFilePath = "src/main/resources/images/" + rndString + "_barcode.png";
            String logoFilePath = "src/main/resources/images/logo.png"; // Replace with your logo path

            // Generate QR code and Barcode
            BufferedImage qrImage = QRImageUtil.generateQRCode(rndString, 100, 100);
            BufferedImage barcodeImage = QRImageUtil.generateBarcode(rndString, 200, 100);

            // Save images
            ImageIO.write(qrImage, "PNG", new File(qrFilePath));
            ImageIO.write(barcodeImage, "PNG", new File(barcodeFilePath));

            // Generate the ticket PDF with both the QR code and barcode
            String ticketPath = "target/PrintedTickets/" + rndString + ".pdf";
            Customer customer = new Customer(txtCustomerFirstName.getText(), txtCustomerLastName.getText(), txtCustomerEmail.getText(), Integer.parseInt(txtCustomerPhone.getText()));
            PdfGeneratorUtil.generateTicket(ticketPath, selectedEvent.getTitle(), selectedEvent.getDescription(), selectedEvent.getLocationGuidance(), selectedEvent.getLocation().getAddress() + ", " + selectedEvent.getLocation().getCity(), selectedEvent.getStartDate().toString(), selectedEvent.getStartTime().toString(), selectedEvent.getEndTime().toString(), selectedTicket.getTicketName(), customer.getFirstName(), qrFilePath, barcodeFilePath, logoFilePath);

            ticketModel.savePrintedTicket(rndString, selectedTicket, selectedEvent, customer);
            gMailer.sendMail("Your tickets for " + selectedEvent.getTitle() + " are here!", "Hello " + customer.getFirstName() + "!\nYour tickets are attached below.\n Your order included:\n" + selectedTicket.getTicketName() +"\n\n\n\n\n If you have any question. I don't know. You can respond to this email i guess, but nobody is checking it.\n Have a ticketTastic day!", txtCustomerEmail.getText(), new File(ticketPath));
            new File(qrFilePath).delete();
            new File(barcodeFilePath).delete();
        } catch (Exception e) {
            AlertClass.alertError("Error", "An error occurred while printing PDF");
            e.printStackTrace();
        }
    }

        //TODO: TEMP KLASSE. MAKE THIS STUFF ACTUALLY GOOD.
    public static String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        int length = random.nextInt(30) + 15;
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }
        return length + "abc" + randomString.toString();
    }
}

