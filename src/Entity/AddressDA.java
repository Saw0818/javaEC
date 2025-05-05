package Entity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddressDA {
    private static final String DB_URL = "jdbc:mysql://databaseforassignment.c3eozrschsjm.us-east-1.rds.amazonaws.com:3306/assignmentdb";
    private static final String DB_USER = "admin";
    private static final String DB_PASS = "superidoldexiaorong";
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
    
    /**
     * Save address to database and return the address ID
     */
    public String saveAddress(Address address, String customerId) throws SQLException {
        // Generate a new address ID if not provided
        if (address.getAddressId() == null || address.getAddressId().isEmpty()) {
            String addressID = null;
            DBConnection db = new DBConnection();
            db.initializeJdbc();
            addressID = db.generateID("ADDRESSES", "AD", 6, 2);
            address.setAddressId(addressID);
        }
        
        String sql = "INSERT INTO ADDRESSES (ADDRESSID, CUSTOMERID, STREET, CITY, STATES, POSTALCODE, PLATENUM) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
                     
        try (Connection conn = getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, address.getAddressId());
            ps.setString(2, customerId);
            ps.setString(3, address.getStreet() != null ? address.getStreet() : "");
            ps.setString(4, address.getCity());
            ps.setString(5, address.getState() != null ? address.getState() : "");
            ps.setString(6, address.getPostalCode());
            ps.setString(7, address.getPlateNum() != null ? address.getPlateNum() : "");
            ps.executeUpdate();
        }
        return address.getAddressId();
    }
    
    /**
     * Get customer's addresses
     */
    public List<Address> getCustomerAddresses(String customerId) throws SQLException {
        List<Address> addresses = new ArrayList<>();
        String sql = "SELECT * FROM ADDRESSES WHERE CUSTOMERID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Address address = new Address();
                    address.setAddressId(rs.getString("ADDRESSID"));
                    address.setStreet(rs.getString("STREET"));
                    address.setCity(rs.getString("CITY"));
                    address.setState(rs.getString("STATE"));
                    address.setPostalCode(rs.getString("POSTAL"));
                    address.setPlateNum(rs.getString("PLATENO"));
                    addresses.add(address);
                }
            }
        }
        return addresses;
    }
    
    /**
     * Get address by ID
     */
    public Address getAddressById(String addressId) throws SQLException {
        String sql = "SELECT * FROM ADDRESSES WHERE ADDRESSID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, addressId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Address address = new Address();
                    address.setAddressId(rs.getString("ADDRESSID"));
                    address.setStreet(rs.getString("STREET"));
                    address.setCity(rs.getString("CITY"));
                    address.setState(rs.getString("STATES"));
                    address.setPostalCode(rs.getString("POSTALCODE"));
                    address.setPlateNum(rs.getString("PLATENUM"));
                    return address;
                }
            }
        }
        return null;
    }
    
    /**
     * Delete address
     */
    public boolean deleteAddress(String addressId) throws SQLException {
        String sql = "DELETE FROM ADDRESSES WHERE ADDRESSID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, addressId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }
}