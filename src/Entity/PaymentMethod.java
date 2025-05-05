package Entity;

public class PaymentMethod {
    private String methodId;
    private String customerId;
    private String methodType; // e.g., "CREDIT_CARD", "DEBIT_CARD", "PAYPAL", etc.
    private String cardNumber;
    private String expiryDate;
    private String cardholderName;
    
    public PaymentMethod() {
        // Default constructor
    }
    
    public PaymentMethod(String methodType) {
        this.methodType = methodType;
    }
    
    // Getters and setters
    public String getMethodId() {
        return methodId;
    }
    
    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public String getMethodType() {
        return methodType;
    }
    
    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }
    
    public String getCardNumber() {
        return cardNumber;
    }
    
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    public String getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public String getCardholderName() {
        return cardholderName;
    }
    
    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }
    
    // Display a masked card number for security
    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        int length = cardNumber.length();
        return "**** **** **** " + cardNumber.substring(length - 4);
    }
    
    @Override
    public String toString() {
        return methodType + (cardNumber != null ? " (" + getMaskedCardNumber() + ")" : "");
    }
}