package Entity;

public class Address {
    private String addressId;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String plateNum;
    
    public Address() {
        // Default constructor
    }
    
    public Address(String city, String postalCode) {
        this.city = city;
        this.postalCode = postalCode;
    }
    
    public Address(String street, String city, String state, String postalCode) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }
    
    // Getters and setters
    public String getAddressId() {
        return addressId;
    }
    
    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
    
    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public String getPlateNum() {
        return plateNum;
    }
    
    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (street != null && !street.isEmpty()) {
            sb.append(street).append(", ");
        }
        sb.append(city);
        if (state != null && !state.isEmpty()) {
            sb.append(", ").append(state);
        }
        sb.append(" ").append(postalCode);
        if (plateNum != null && !plateNum.isEmpty()) {
            sb.append(" (").append(plateNum).append(")");
        }
        return sb.toString();
    }
}