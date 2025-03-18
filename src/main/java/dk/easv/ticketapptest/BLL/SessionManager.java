package dk.easv.ticketapptest.BLL;


//Thread-safe singleton user session manager class

public class SessionManager {
    private static volatile SessionManager instance;
    private String currentUser;

    private SessionManager() {}

    //Double-checked locking to ensure thread safety. APPARENTLY BILL PUGH SINGLETONS ARE GOOD TOO
    //But I think this is neat
    public static SessionManager getInstance() {
        //TODO add more comments based on https://www.youtube.com/watch?v=tSZn4wkBIu8 to show a better understanding
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }

        }
        return instance;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
    public String getCurrentUser() {
        return currentUser;
    }

    //TODO remove souts when this works
    public void logout() {
        System.out.println(currentUser + " logged out");
        currentUser = null;
        System.out.println("Logged out successfully");

    }
}
