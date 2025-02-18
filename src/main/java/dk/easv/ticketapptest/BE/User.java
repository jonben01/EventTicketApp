package dk.easv.ticketapptest.BE;





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


    //TODO IMPLEMENT USERNAME AND PASSWORD


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    public User(String username, String password, String firstName, String lastName, String email, String phone, Role role) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
