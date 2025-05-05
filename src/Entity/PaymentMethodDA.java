package Entity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaymentMethodDA {
    private static final String DB_URL = "jdbc:mysql://databaseforassignment.c3eozrschsjm.us-east-1.rds.amazonaws.com:3306/assignmentdb";
    private static final String DB_USER = "admin";
    private static final String DB_PASS = "superidoldexiaorong";
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
    
    /**
     * Get or create a payment method for the customer
     * If the payment method type already exists, return its ID
     * Otherwise, create a new payment method and return its ID
     */
    public String getOrCreatePaymentMethod(String paymentType, String customerId) throws SQLException {
        // First check if the customer already has this payment method type
        String methodId = getExistingPaymentMethodId(customerId, paymentType);
        
        if (methodId != null) {
            return methodId;
        }
        
        // If not, create a new payment method
        return createPaymentMethod(customerId, paymentType);
    }
    
    /**
     * Get the ID of an existing payment method by type
     */
    private String getExistingPaymentMethodId(String customerId, String paymentType) throws SQLException {
        String sql = "SELECT METHODID FROM PAYMENT_METHODS WHERE CUSTOMERID = ? AND METHOD_TYPE = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerId);
            ps.setString(2, paymentType);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("METHODID");
                }
            }
        }
        return null;
    }
    
    /**
     * Create a new payment method
     */
    private String createPaymentMethod(String customerId, String paymentType) throws SQLException {
        String methodId = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String sql = "INSERT INTO PAYMENT_METHODS (METHODID, CUSTOMERID, METHOD_TYPE) VALUES (?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, methodId);
            ps.setString(2, customerId);
            ps.setString(3, paymentType);
            ps.executeUpdate();
        }
        return methodId;
    }
    
    /**
     * Get all payment methods for a customer
     */
    public List<PaymentMethod> getCustomerPaymentMethods(String customerId) throws SQLException {
        List<PaymentMethod> methods = new ArrayList<>();
        String sql = "SELECT * FROM PAYMENT_METHODS WHERE CUSTOMERID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PaymentMethod method = new PaymentMethod();
                    method.setMethodId(rs.getString("METHODID"));
                    method.setCustomerId(rs.getString("CUSTOMERID"));
                    method.setMethodType(rs.getString("METHOD_TYPE"));
                    // Set other fields based on your PaymentMethod class structure
                    methods.add(method);
                }
            }
        }
        return methods;
    }
}