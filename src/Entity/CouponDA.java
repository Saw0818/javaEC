package Entity;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CouponDA {
    private static final String DB_URL  = "jdbc:derby://localhost:1527/assignmentDB";
    private static final String DB_USER = "nbuser";
    private static final String DB_PASS = "nbuser";
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
    
    /**
     * Generate a new coupon ID
     */
    public String generateCouponId() {
        return "CPN" + UUID.randomUUID().toString().substring(0, 3).toUpperCase();
    }
    
    /**
     * Generate a random coupon code
     */
    private String generateCouponCode() {
        // Generate a random 8-character alphanumeric code
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = (int) (chars.length() * Math.random());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
    
    /**
     * Get a coupon by its code
     */
    public Coupon getCoupon(String couponCode) throws SQLException {
        String sql = "SELECT * FROM COUPONS WHERE COUPONCODE = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, couponCode);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCoupon(rs);
                }
                return null;
            }
        }
    }
    
    /**
     * Create a birthday coupon for a specific customer
     */
    public Coupon createBirthdayCoupon(String customerId) throws SQLException {
        // Create coupon object
        Coupon coupon = new Coupon();
        coupon.setCouponId(generateCouponId());
        coupon.setCouponCode("BDAY" + generateCouponCode().substring(0, 4));
        coupon.setDiscountType("BIRTHDAY");
        coupon.setExpiry(LocalDateTime.now().plusMonths(3)); // Valid for 3 months
        coupon.setStatus("ACTIVE");
        coupon.setCreatedAt(LocalDateTime.now());
        coupon.setCustomerId(customerId);
        
        // Save to database
        saveCoupon(coupon);
        
        return coupon;
    }
    
    /**
     * Create a fixed discount coupon
     * 
     * @param customerId Customer ID or null for a general coupon
     */
    public Coupon createFixedCoupon(String customerId) throws SQLException {
        // Create coupon object
        Coupon coupon = new Coupon();
        coupon.setCouponId(generateCouponId());
        coupon.setCouponCode("FIX" + generateCouponCode().substring(0, 5));
        coupon.setDiscountType("FIXED");
        coupon.setExpiry(LocalDateTime.now().plusMonths(3)); // Valid for 3 months
        coupon.setStatus("ACTIVE");
        coupon.setCreatedAt(LocalDateTime.now());
        coupon.setCustomerId(customerId); // Can be null for general coupon
        
        // Save to database
        saveCoupon(coupon);
        
        return coupon;
    }
    
    /**
     * Create a free shipping coupon
     * 
     * @param customerId Customer ID or null for a general coupon
     */
    public Coupon createFreeShippingCoupon(String customerId) throws SQLException {
        // Create coupon object
        Coupon coupon = new Coupon();
        coupon.setCouponId(generateCouponId());
        coupon.setCouponCode("SHIP" + generateCouponCode().substring(0, 4));
        coupon.setDiscountType("FREESHIPPING");
        coupon.setExpiry(LocalDateTime.now().plusMonths(3)); // Valid for 3 months
        coupon.setStatus("ACTIVE");
        coupon.setCreatedAt(LocalDateTime.now());
        coupon.setCustomerId(customerId); // Can be null for general coupon
        
        // Save to database
        saveCoupon(coupon);
        
        return coupon;
    }
    
    /**
     * Create coupons for all customers
     */
    public void createCouponsForAllCustomers() throws SQLException {
        // Get all customer IDs
        List<String> customerIds = getAllCustomerIds();
        
        for (String customerId : customerIds) {
            // Create one of each type of coupon for each customer
            createBirthdayCoupon(customerId);
            createFixedCoupon(customerId);
            createFreeShippingCoupon(customerId);
        }
        
        // Also create some general coupons not tied to specific customers
        for (int i = 0; i < 5; i++) {
            createFixedCoupon(null);
            createFreeShippingCoupon(null);
        }
    }
    
    /**
     * Get all customer IDs from the database
     */
    private List<String> getAllCustomerIds() throws SQLException {
        List<String> customerIds = new ArrayList<>();
        
        String sql = "SELECT CUSTOMERID FROM CUSTOMER";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                customerIds.add(rs.getString("CUSTOMERID"));
            }
        }
        
        return customerIds;
    }
    
    /**
     * Save a coupon to the database
     */
    private void saveCoupon(Coupon coupon) throws SQLException {
        String sql = "INSERT INTO COUPONS (COUPONID, COUPONCODE, DISCOUNTTYPE, " +
                     "EXPIRY, STATUS, CREATED_AT, CUSTOMERID) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, coupon.getCouponId());
            ps.setString(2, coupon.getCouponCode());
            ps.setString(3, coupon.getDiscountType());
            ps.setTimestamp(4, Timestamp.valueOf(coupon.getExpiry()));
            ps.setString(5, coupon.getStatus());
            ps.setTimestamp(6, Timestamp.valueOf(coupon.getCreatedAt()));
            
            // Customer ID can be null for general coupons
            if (coupon.getCustomerId() != null) {
                ps.setString(7, coupon.getCustomerId());
            } else {
                ps.setNull(7, java.sql.Types.VARCHAR);
            }
            
            ps.executeUpdate();
        }
    }
    
    /**
     * Validate a coupon code for a specific customer
     */
    public Coupon validateCoupon(String couponCode, String customerId) throws SQLException {
        String sql = "SELECT * FROM COUPONS WHERE COUPONCODE = ? AND STATUS = 'ACTIVE' AND EXPIRY > ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, couponCode);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Coupon coupon = mapResultSetToCoupon(rs);
                    
                    // Check if coupon is valid for this customer
                    String couponCustomerId = coupon.getCustomerId();
                    if (couponCustomerId != null && !couponCustomerId.isEmpty() && !couponCustomerId.equals(customerId)) {
                        return null; // Coupon belongs to another customer
                    }
                    
                    return coupon;
                }
                return null;
            }
        }
    }
    
    /**
     * Mark a coupon as used
     */
    public void markCouponAsUsed(String couponCode, String customerId) throws SQLException {
        String sql = "UPDATE COUPONS SET STATUS = 'USED', REDEEMED_AT = ? WHERE COUPONCODE = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(2, couponCode);
            
            ps.executeUpdate();
        }
    }
    
    /**
     * Update expired coupons status
     */
    public void updateExpiredCoupons() throws SQLException {
        String sql = "UPDATE COUPONS SET STATUS = 'EXPIRED' WHERE EXPIRY < ? AND STATUS = 'ACTIVE'";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        }
    }
    
    /**
     * Get available coupons for a customer
     */
    public List<Coupon> getAvailableCoupons(String customerId) throws SQLException {
        // First update any expired coupons
        updateExpiredCoupons();
        
        List<Coupon> coupons = new ArrayList<>();
        
        // Get coupons that are either specific to this customer or not tied to any customer
        String sql = "SELECT * FROM COUPONS WHERE STATUS = 'ACTIVE' AND EXPIRY > ? " +
                     "AND (CUSTOMERID = ? OR CUSTOMERID IS NULL)";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(2, customerId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    coupons.add(mapResultSetToCoupon(rs));
                }
            }
        }
        
        return coupons;
    }
    
    /**
     * Helper method to map ResultSet to Coupon object
     */
    private Coupon mapResultSetToCoupon(ResultSet rs) throws SQLException {
        Coupon coupon = new Coupon();
        coupon.setCouponId(rs.getString("COUPONID"));
        coupon.setCouponCode(rs.getString("COUPONCODE"));
        coupon.setDiscountType(rs.getString("DISCOUNTTYPE"));
        coupon.setExpiry(rs.getTimestamp("EXPIRY").toLocalDateTime());
        coupon.setStatus(rs.getString("STATUS"));
        coupon.setCreatedAt(rs.getTimestamp("CREATED_AT").toLocalDateTime());
        
        Timestamp redeemedAt = rs.getTimestamp("REDEEMED_AT");
        if (redeemedAt != null) {
            coupon.setRedeemedAt(redeemedAt.toLocalDateTime());
        }
        
        coupon.setCustomerId(rs.getString("CUSTOMERID"));
        return coupon;
    }
    
    /**
     * Calculate discount amount based on coupon type
     */
    public double calculateDiscountAmount(Coupon coupon, double subtotal, double shippingFee) {
        if (coupon == null || !coupon.isValid()) {
            return 0.0;
        }
        
        return coupon.calculateDiscount(subtotal, shippingFee);
    }
}