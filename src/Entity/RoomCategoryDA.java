package Entity;

import java.sql.*;

public class RoomCategoryDA {
    private PreparedStatement stmt;
    DBConnection connection = new DBConnection();
    
    public boolean addCategory(String roomID, String roomName, String category, String roomImage){
        String createQuery = "INSERT INTO ROOMCATEGORY VALUES (?,?,?,?)";
        try{
            connection.initializeJdbc();
            Connection conn = connection.getConnection();
            stmt = conn.prepareStatement(createQuery);
            stmt.setString(1, roomID);
            stmt.setString(2, roomName);
            stmt.setString(3, category);
            stmt.setString(4, roomImage);
            
            int rowCreate = stmt.executeUpdate();
            return rowCreate > 0;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean updateCategory(String roomName, String roomCategory, String roomImage, String roomID){
        String updateQuery = "UPDATE ROOMCATEGORY SET ROOMNAME = ?,CATEGORY = ? , ROOMIMAGE = ? WHERE ROOMID = ?";
        try{
            connection.initializeJdbc();
            Connection conn = connection.getConnection();
            stmt = conn.prepareStatement(updateQuery);
            stmt.setString(1,roomName);
            stmt.setString(2,roomCategory);
            stmt.setString(3,roomImage);
            stmt.setString(4,roomID);
            
            int rowUpdate = stmt.executeUpdate();
            return rowUpdate > 0;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteCategory(String roomID){
        String deleteQuery = "DELETE FROM ROOMCATEGORY WHERE ROOMID = ?";
        try{
            connection.initializeJdbc();
            Connection conn = connection.getConnection();
            
            stmt = conn.prepareStatement(deleteQuery);
            stmt.setString(1, roomID);
            
            int rowDelete = stmt.executeUpdate();
            return rowDelete > 0;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteAllProduct(String roomID){
        String deleteQuery = "DELETE FROM PRODUCT WHERE ROOMID = ?";
        try{
            connection.initializeJdbc();
            Connection conn = connection.getConnection();
            
            stmt = conn.prepareStatement(deleteQuery);
            stmt.setString(1, roomID);
            
            int rowDelete = stmt.executeUpdate();
            return rowDelete > 0;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
