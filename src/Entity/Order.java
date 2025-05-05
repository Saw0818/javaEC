/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Admin
 */

public class Order {

    private String orderId;
    private String customerId;
    private Cart cart;
    private Coupon discount;
    private Double discountAmt;
    private Shipping shipping;
    private Address shippingAddress;
    private LocalDateTime timestamp;
    private Double tax;
    private Double total;
    private String status;
    private String paymentMethodId;
    
    
    public Order() {
        // Default constructor
    }
    
    // Getters and setters
    public String getOrderId() {
        return orderId;
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public Cart getCart() {
        return cart;
    }
    
    public void setCart(Cart cart) {
        this.cart = cart;
    }
    
    public Coupon getDiscount() {
        return discount;
    }
    
    public void setDiscount(Coupon discount) {
        this.discount = discount;
    }
    
    public Double getDiscountAmt() {
        return discountAmt;
    }
    
    public void setDiscountAmt(Double discountAmt) {
        this.discountAmt = discountAmt;
    }
    
    public Shipping getShipping() {
        return shipping;
    }
    
    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }
    
    public Address getShippingAddress() {
        return shippingAddress;
    }
    
    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public Double getTax() {
        return tax;
    }
    
    public void setTax(Double tax) {
        this.tax = tax;
    }
    
    public Double getTotal() {
        return total;
    }
    
    public void setTotal(Double total) {
        this.total = total;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getPaymentMethodId() {
        return paymentMethodId;
    }
    
    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }
    
    // Helper methods for calculations
    public double computeSubtotal() {
        if (cart == null) {
            return 0.0;
        }
        return cart.calculateSubtotal();
    }
    
    public double computeDiscountAmount() {
        if (discountAmt != null) {
            return discountAmt;
        }
        if (discount == null) {
            return 0.0;
        }
        return discount.calculateDiscount(computeSubtotal(), 5);
    }
    
    public double computeTax() {
        if (tax != null) {
            return tax;
        }
        double subtotalAfterDiscount = computeSubtotal() - computeDiscountAmount();
        return subtotalAfterDiscount * 0.06; // 6% tax
    }
    
    public double computeShippingCost() {
        if (shipping == null) {
            return 0.0;
        }
        // Apply free shipping discount if applicable
        if (discount != null && discount.getDiscountType().equals("FREESHIPPING")) {
            return 0.0;
        }
        return shipping.getCost();
    }
    
    public double computeTotal() {
        if (total != null) {
            return total;
        }
        return computeSubtotal() - computeDiscountAmount() + computeTax() + computeShippingCost();
    }
    
    // Update all calculated fields based on current state
    public void updateCalculatedFields() {
        if (discountAmt == null) {
            discountAmt = computeDiscountAmount();
        }
        if (tax == null) {
            tax = computeTax();
        }
        if (total == null) {
            total = computeTotal();
        }
    }
    
    // For consistency with existing code that may use userId
    public String getUserId() {
        return customerId;
    }
    
    public void setUserId(String userId) {
        this.customerId = userId;
    }
    
    // For payment status
    public String getPaymentStatus() {
        return status;
    }
    
    public void setPaymentStatus(String status) {
        this.status = status;
    }
    
    
    
    public void setAddress(Address address){
    this.shippingAddress = address;
    }
    
    public Address getAddress(){
    return shippingAddress;
    }
    
}