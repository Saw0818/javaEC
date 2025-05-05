package Entity;

import java.time.LocalDateTime;

public class Coupon {
    private String couponId;
    private String couponCode;
    private String discountType; // "BIRTHDAY", "FIXED", "FREESHIPPING"
    private LocalDateTime expiry;
    private String status; // "ACTIVE", "USED", "EXPIRED"
    private LocalDateTime createdAt;
    private LocalDateTime redeemedAt;
    private String customerId; // Can be null for general coupons
    
    public Coupon() {
        // Default constructor
    }
    
    public Coupon(String couponId, String couponCode, String discountType, 
                 LocalDateTime expiry, String customerId) {
        this.couponId = couponId;
        this.couponCode = couponCode;
        this.discountType = discountType;
        this.expiry = expiry;
        this.status = "ACTIVE";
        this.createdAt = LocalDateTime.now();
        this.customerId = customerId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDateTime expiry) {
        this.expiry = expiry;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getRedeemedAt() {
        return redeemedAt;
    }

    public void setRedeemedAt(LocalDateTime redeemedAt) {
        this.redeemedAt = redeemedAt;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    /**
     * Calculate discount amount based on coupon type
     * 
     * @param subtotal The cart subtotal
     * @param shippingFee The shipping fee
     * @return The discount amount
     */
    public double calculateDiscount(double subtotal, double shippingFee) {
        switch (discountType) {
            case "BIRTHDAY":
                return subtotal * 0.10; // 10% discount for birthday coupons
            case "FIXED":
                return 5.0; // RM5 fixed discount
            case "FREESHIPPING":
                return shippingFee; // Waive shipping fee
            default:
                return 0.0;
        }
    }
    
    /**
     * Check if this coupon is valid (active and not expired)
     */
    public boolean isValid() {
        return "ACTIVE".equals(status) && 
               LocalDateTime.now().isBefore(expiry);
    }
    
    /**
     * Check if this coupon can be used by a specific customer
     * 
     * @param customerIdToCheck ID of customer attempting to use the coupon
     * @return true if the coupon can be used by this customer
     */
    public boolean isValidForCustomer(String customerIdToCheck) {
        // If coupon is not tied to a specific customer, anyone can use it
        if (customerId == null || customerId.isEmpty()) {
            return true;
        }
        
        // Otherwise, only the specified customer can use it
        return customerId.equals(customerIdToCheck);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(couponCode).append(" - ");
        
        switch (discountType) {
            case "BIRTHDAY":
                sb.append("10% Birthday Discount");
                break;
            case "FIXED":
                sb.append("RM5 Discount");
                break;
            case "FREESHIPPING":
                sb.append("Free Shipping");
                break;
        }
        
        sb.append(" (Expires: ").append(expiry.toLocalDate()).append(")");
        return sb.toString();
    }
}