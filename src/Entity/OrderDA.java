package Entity;

import Entity.*;
import java.sql.*;
import java.util.*;

public class OrderDA {
    private static final String DB_URL  = "jdbc:derby://localhost:1527/assignmentDB";
    private static final String DB_USER = "nbuser";
    private static final String DB_PASS = "nbuser";
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
    
    
    /**
     * Create an order and its items in a single transaction.
     * @return orderId of the created order
     */
    public String createOrder(Order order, Cart cart, String shippingId) throws Exception {
        Connection c = null;
        PreparedStatement p = null;
        String orderId = null;
        
        
        // Generate order ID if not already set
        try {
            DBConnection db = new DBConnection();
            db.initializeJdbc();
            orderId = db.generateID("ORDERS", "OID", 12, 3);
            if (cart.getCartID() == null){String cartId = db.generateID("CARTS", "CT", 6, 2);cart.setCartID(cartId);}
            
            String sql = "INSERT INTO ORDERS " +
                     "(ORDERID, CUSTOMERID, CARTID, DISCOUNTAMT, SHIPPINGID, TAX, TOTAL, STATUS, METHODID, ADDRESSID, CREATED_AT) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            c = getConnection();
            p = c.prepareStatement(sql);
            p.setString(1, orderId);
            p.setString(2, order.getCustomerId());
            p.setString(3, cart.getCartID());
            p.setDouble(4, order.getDiscountAmt() != null ? order.getDiscountAmt() : 0.0);
            p.setString(5, shippingId);
            p.setDouble(6, order.getTax() != null ? order.getTax() : 0.0);
            p.setDouble(7, order.getTotal() != null ? order.getTotal() : 0.0);
            p.setString(8, "PENDING");
            System.out.print("database status done!");
            p.setString(9, order.getPaymentMethodId());
            p.setString(10, order.getShippingAddress() != null ? order.getShippingAddress().getAddressId() : null);
            p.setTimestamp(11, Timestamp.valueOf(order.getTimestamp()));
            
            int rowsAffected = p.executeUpdate();
            System.out.println("Rows inserted: " + rowsAffected);
            return orderId;
        } catch (SQLException e) {
            System.err.println("Error creating cart: " + e.getMessage());
            throw e;
        }
        
    }

}