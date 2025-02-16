package dk.easv.ticketapptest.BE;


import javax.management.relation.Role;


//TODO MAKE CLASS ABSTRACT WHEN ACTUAL PROJECT STARTS

public class User {
    private int id;
    private String username;
    private String password;
    private Role role;

    //this will be moved to separate classes later, and this class will be made abstract
    private String firstName;
    private String lastName;
    private String email;
    //should arguably not be a string, but its just easier to work with.
    private String phone;

    public User(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
