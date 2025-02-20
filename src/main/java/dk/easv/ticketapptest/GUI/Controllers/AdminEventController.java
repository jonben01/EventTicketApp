package dk.easv.ticketapptest.GUI.Controllers;

import dk.easv.ticketapptest.BE.Event2;
import dk.easv.ticketapptest.GUI.TemporaryDataClass;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminEventController implements Initializable {

    @FXML public TableView<Event2> tblEvents;
    @FXML private TableColumn<Event2, String> clnEventName;
    @FXML private TableColumn<Event2, String> clnDateTime;
    @FXML private TableColumn<Event2, String> clnLocation;
    @FXML private TableColumn<Event2, Void> clnStatus;
    @FXML private TableColumn<Event2, Button> clnActions;

    private TemporaryDataClass tdc;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //todo make tdc a singleton or use setTDC methods in parent (cba doing it now)
        // right now all changes get reset, cause its two different observable lists.
        tdc = new TemporaryDataClass();

        clnEventName.setCellValueFactory(new PropertyValueFactory<>("title"));

        clnDateTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        clnLocation.setCellValueFactory(new PropertyValueFactory<>("location"));

        clnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        clnActions.setCellValueFactory(param -> {
            Event2 event = param.getValue();

            Button deleteButton = new Button("Delete");

            deleteButton.setOnAction(e -> {
                //tblEvents.getItems().remove(event);
                tdc.deleteEvent(event);
            });
            return new SimpleObjectProperty<>(deleteButton);
        });
        


        tblEvents.setItems(tdc.getEvents());


    }
}
