package dk.easv.ticketapptest.BLL.Exceptions;

public class EasvTicketException extends Exception {

    public EasvTicketException(String message) {
        super(message);
    }

    public EasvTicketException(String message, Throwable cause) {
        super(message, cause);
    }
}
