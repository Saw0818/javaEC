package Entity;

import java.sql.*;

public class ProductDA {
    private PreparedStatement stmt;
    DBConnection connection = new DBConnection();
    
    public boolean createProduct(String productID, String productName, int productPrice, String productDesc , String productImg, String productLongDesc, String roomID){
        String createQuery = "INSERT INTO PRODUCT VALUES (?,?,?,?,?,?,?)";
        
        try{
            connection.initializeJdbc();
            Connection conn = connection.getConnection();
            stmt = conn.prepareStatement(createQuery);
            stmt.setString(1, productID);
            stmt.setString(2, productName);
            stmt.setInt(3, productPrice);
            stmt.setString(4, productDesc);
            stmt.setString(5, productImg);
            stmt.setString(6, productLongDesc);
            stmt.setString(7, roomID);;
            int rowCreate = stmt.executeUpdate();
            System.out.print(rowCreate);
            return rowCreate > 0;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean updateProduct(String productName, int productPrice, String productDesc, String productImg, String productLongDesc, String roomID , String productID){
        String updateQuery = "UPDATE PRODUCT SET PRODUCTNAME = ?, PRODUCTPRICE = ?, PRODUCTDESC = ?, PRODUCTIMG = ?, PRODUCTLONGDESC = ?, ROOMID = ? WHERE PRODUCTID = ?";
        
        try{
            System.out.print("Try here");
            connection.initializeJdbc();
            Connection conn = connection.getConnection();
            
            stmt = conn.prepareStatement(updateQuery);
            stmt.setString(1,productName);
            stmt.setInt(2,productPrice);
            stmt.setString(3,productDesc);
            stmt.setString(4,productImg);
            stmt.setString(5,productLongDesc);
            stmt.setString(6,roomID);
            stmt.setString(7,productID);

            System.out.print("Try here 2");
            int rowUpdated = stmt.executeUpdate();
            System.out.print(rowUpdated);
            return rowUpdated > 0;
        }catch(Exception ex){
            System.out.print("Exception Error");
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteProduct(String productID){
        String deleteQuery = "DELETE FROM PRODUCT WHERE PRODUCTID = ?";
        try{
            connection.initializeJdbc();
            Connection conn = connection.getConnection();
            
            stmt = conn.prepareStatement(deleteQuery);
            stmt.setString(1, productID);
            
            int rowDelete = stmt.executeUpdate();
            return rowDelete > 0;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
