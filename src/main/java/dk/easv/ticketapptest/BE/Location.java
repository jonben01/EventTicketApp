package dk.easv.ticketapptest.BE;

public class Location {
    private int locationID;
    private String address;
    private String city;
    private int postalCode;

    public Location(String address, String city, int postalCode) {
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
    }

    public Location(int locationID, String address, String city, int postalCode) {
        this.locationID = locationID;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
    }

    public int getLocationID() {
        return locationID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }
}
