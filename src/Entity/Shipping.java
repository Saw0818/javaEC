package Entity;

public class Shipping {
    private String shippingId;
    private String method;
    private double cost;
    private int estimatedDays;
    private String trackingNumber;
    
    public Shipping() {
        // Default constructor
    }
    
    public Shipping(String method, double cost, int estimatedDays) {
        this.method = method;
        this.cost = cost;
        this.estimatedDays = estimatedDays;
    }
    
    // Getters and setters
    public String getShippingId() {
        return shippingId;
    }
    
    public void setShippingId(String shippingId) {
        this.shippingId = shippingId;
    }
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    
    public double getCost() {
        return cost;
    }
    
    public void setCost(double cost) {
        this.cost = cost;
    }
    
    public int getEstimatedDays() {
        return estimatedDays;
    }
    
    public void setEstimatedDays(int estimatedDays) {
        this.estimatedDays = estimatedDays;
    }
    
    public String getTrackingNumber() {
        return trackingNumber;
    }
    
    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
    
    // Helper methods
    public void setEstimatedDays(String days) {
        try {
            this.estimatedDays = Integer.parseInt(days);
        } catch (NumberFormatException e) {
            this.estimatedDays = 3; // Default to 3 days if parsing fails
        }
    }
    
    @Override
    public String toString() {
        return method + " (" + estimatedDays + " days) - RM" + String.format("%.2f", cost);
    }
}