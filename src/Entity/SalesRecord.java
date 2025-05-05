/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author Admin
 */
public class SalesRecord {
    private int index;
    private String productId;
    private String productName;
    private int soldUnits;
    private double unitPrice;
    private double subtotal;
    private String date;

    // Constructor
    public SalesRecord(String productId, String productName, int soldUnits, double unitPrice ,String date) {
       
        this.productId = productId;
        this.productName = productName;
        this.soldUnits = soldUnits;
        this.unitPrice = unitPrice;
        this.subtotal = soldUnits * unitPrice;
        this.date = date;
    }

    // Getters

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getSoldUnits() {
        return soldUnits;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getSubtotal() {
        return subtotal;
    }

    // Setters
    public void setIndex(int index) {
        this.index = index;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setSoldUnits(int soldUnits) {
        this.soldUnits = soldUnits;
        updateSubtotal();
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        updateSubtotal();
    }

    
    
    // Private helper to update subtotal
    private void updateSubtotal() {
        this.subtotal = this.soldUnits * this.unitPrice;
    }
    
     public void setDate(String date) {
        this.date = date;
       
    }
       public String getDate() {
        return date;
       
    }
    
    
}

