package dk.easv.ticketapptest.BLL.util;

import dk.easv.ticketapptest.BE.Event2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class AdminEventSearcher {

    public ObservableList<Event2> searchEvents(List<Event2> searchBase, String searchQuery) {
        List<Event2> searchResults = new ArrayList<Event2>();

        for (Event2 event : searchBase) {
            if (compareToTitle(searchQuery, event) ||
                    compareToLocation(searchQuery, event) ||
                    compareToStatus(searchQuery, event) ||
                    compareToDate(searchQuery, event)) {
                searchResults.add(event);
            }
        }
        return FXCollections.observableList(searchResults);
    }


    private boolean compareToTitle (String searchQuery, Event2 event) {
        return event.getTitle().toLowerCase().contains(searchQuery.toLowerCase());
    }

    private boolean compareToLocation (String searchQuery, Event2 event) {
        return event.getLocation().toString().toLowerCase().contains(searchQuery.toLowerCase());
    }

    private boolean compareToStatus (String searchQuery, Event2 event) {
        return event.getStatus().toLowerCase().contains(searchQuery.toLowerCase());
    }

    private boolean compareToDate(String searchQuery, Event2 event) {
        return event.getStartDate().toString().toLowerCase().contains(searchQuery.toLowerCase());
    }


}
