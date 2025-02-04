package dk.easv.ticketapptest.BE;


import javax.management.relation.Role;

public abstract class User {
    private int id;
    private String username;
    private String password;
    private Role role;
}
