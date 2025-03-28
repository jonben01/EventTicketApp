package dk.easv.ticketapptest.BLL.util;

import dk.easv.ticketapptest.BE.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class UserSearcher {



    public ObservableList<User> searchUser(List<User> searchBase, String searchQuery) {
        List<User> searchResults = new ArrayList<>();

        for (User user : searchBase) {
            if (compareToFirstName(searchQuery, user) ||
                    compareToLastName(searchQuery, user) ||
                    compareToEmail(searchQuery, user) ||
                    compareToPhone(searchQuery, user) ||
                    compareToUsername(searchQuery, user)) {

                searchResults.add(user);

            }
        }
        return FXCollections.observableList(searchResults);
    }


    public boolean compareToFirstName(String searchQuery, User user) {
        return user.getFirstName().toLowerCase().contains(searchQuery.toLowerCase());
    }
    public boolean compareToLastName(String searchQuery, User user) {
        return user.getLastName().toLowerCase().contains(searchQuery.toLowerCase());
    }
    public boolean compareToUsername(String searchQuery, User user) {
        return user.getUsername().toLowerCase().contains(searchQuery.toLowerCase());
    }

    //TODO should potentially only return if its right from left to right, and not just contained.
    public boolean compareToEmail(String searchQuery, User user) {
        return user.getEmail().toLowerCase().contains(searchQuery.toLowerCase());
    }
    //TODO should potentially only return if its right from left to right, and not just contained.
    public boolean compareToPhone(String searchQuery, User user) {
        return user.getPhone().toLowerCase().contains(searchQuery.toLowerCase());
    }

}
