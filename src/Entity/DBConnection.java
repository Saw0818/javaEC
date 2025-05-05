package Entity;
import java.sql.*;
import java.util.ArrayList;
import java.time.*;

public class DBConnection {
    private Connection connection = null;
    private String host = "jdbc:mysql://databaseforassignment.c3eozrschsjm.us-east-1.rds.amazonaws.com:3306/assignmentdb";
    private String username = "admin";
    private String password = "superidoldexiaorong";
    private String driver;
    private String databaseName;

    public void initializeJdbc() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            connection = DriverManager.getConnection(host, username, password);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String[] getTables() {
        String[] tables = null;

        try {
            DatabaseMetaData dbMetaData = connection.getMetaData();
            ResultSet rsTables = dbMetaData.getTables(null, null, null, new String[]{"TABLE"});

            int size = 0;
            while (rsTables.next()) {
                size++;
            }

            rsTables = dbMetaData.getTables(null, null, null, new String[]{"TABLE"});
            tables = new String[size];
            int i = 0;
            while (rsTables.next()) {
                tables[i++] = rsTables.getString("TABLE_NAME");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return tables;
    }

    public ArrayList<String> getColumnNames(String tableName) {
        ArrayList<String> columnNames = new ArrayList<String>();

        try {
            DatabaseMetaData dbMetaData = connection.getMetaData();
            ResultSet rsColumns = dbMetaData.getColumns(null, null, tableName, null);

            while (rsColumns.next()) {
                columnNames.add(rsColumns.getString("COLUMN_NAME"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return columnNames;
    }

    public ArrayList getRows(String tableName) {
        Statement stmt;
        ArrayList<Object[]> rowData = new ArrayList<Object[]>();
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from " + tableName);

            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] tableRow = new Object[columnCount];
                int index = 0;
                for (int i = 0; i < columnCount; i++) {
                    tableRow[index++] = rs.getObject(i + 1);
                }
                rowData.add(tableRow);
            }
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
        return rowData;
    }
    
    public ArrayList joinTable(String tableName1, String tableName2, String joinVariable){
        Statement stmt;
        ArrayList<Object[]> joinData = new ArrayList<Object[]>();
        try{
            stmt = connection.createStatement();
            String query = "SELECT * FROM " + tableName1 + " a JOIN " + tableName2 + " b ON a." + joinVariable + " =  b." + joinVariable;
            ResultSet rs = stmt.executeQuery(query);
            
            int columnCount = rs.getMetaData().getColumnCount();
            while(rs.next()){
                Object[] tableRow = new Object[columnCount];
                int index = 0;
                for(int i = 0; i < columnCount ; i++){
                    tableRow[index++] = rs.getObject(i + 1);
                }
                joinData.add(tableRow);
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return joinData;
    }
    
    public ArrayList joinThreeTable(String tableName, String tableName2, String tableName3, String firstJoinVariable, String secondJoinVariable){
        Statement stmt;
        ArrayList<Object[]> joinData = new ArrayList<Object[]>();
        try{
            stmt = connection.createStatement();
            String query = "SELECT * FROM " + tableName + " A JOIN " + tableName2 +" B ON A." + firstJoinVariable + " = B." + firstJoinVariable +" JOIN "+ tableName3 + " C ON B." + secondJoinVariable + " = C." + secondJoinVariable;
            ResultSet rs = stmt.executeQuery(query);
            
            int columnCount = rs.getMetaData().getColumnCount();
            while(rs.next()){
                Object[] tableRow = new Object[columnCount];
                int index = 0;
                for(int i = 0; i < columnCount ; i++){
                    tableRow[index++] = rs.getObject(i + 1);
                }
                joinData.add(tableRow);
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return joinData;
    }
    
    public ArrayList getTopTenSales(){
        Statement stmt;
        ArrayList<Object[]> data = new ArrayList<Object[]>();
        try{
            stmt = connection.createStatement();
            String query = "SELECT P.PRODUCTID, P.PRODUCTNAME, SUM(CI.QUANTITY) AS TOTALUNIT , P.PRODUCTPRICE, P.PRODUCTIMG, P.ROOMID  FROM ORDERS O JOIN CARTS C ON O.CARTID = C.CARTID JOIN CARTITEMS CI ON CI.CARTID = C.CARTID JOIN PRODUCT P ON CI.PRODUCTID = P.PRODUCTID GROUP BY P.PRODUCTID, P.PRODUCTNAME, P.PRODUCTPRICE, P.PRODUCTIMG,P.ROOMID ORDER BY TOTALUNIT DESC";
            ResultSet rs = stmt.executeQuery(query);
            
            int columnCount = rs.getMetaData().getColumnCount();
            while(rs.next()){
                Object[] tableRow = new Object[columnCount];
                int index = 0;
                for(int i = 0; i < columnCount ; i++){
                    tableRow[index++] = rs.getObject(i + 1);
                }
                data.add(tableRow);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return data;
    }
    
    public ArrayList searchTable(String tableName, String keywords){
        Statement stmt;
        ArrayList<Product> searchData = new ArrayList<Product>();
        try{
            keywords = keywords.toUpperCase();
            initializeJdbc();
            stmt = connection.createStatement();
            String query = "SELECT * FROM " + tableName + " Where PRODUCTID = '"+ keywords + "' OR UPPER(PRODUCTNAME) LIKE '%"+ keywords +"%'";
            ResultSet rs = stmt.executeQuery(query);
            int columnCount = rs.getMetaData().getColumnCount();
            while(rs.next()){
                String id = rs.getString("PRODUCTID");
                String name = rs.getString("PRODUCTNAME");
                int price = rs.getInt("PRODUCTPRICE");
                String desc = rs.getString("PRODUCTDESC");
                String img = rs.getString("PRODUCTIMG");
                
                searchData.add(new Product(id,name,price,desc,img));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return searchData;
    }
    
    public String generateID(String tableName, String idChar,int idLength, int idCharLength){
        ArrayList<Object[]> data = getRows(tableName);
        String lastRecordID = "";
        for(int i = 0; i < data.size() ; i++){
            if(i+1 == data.size()){
                lastRecordID = data.get(i)[0].toString();
            }
        }
        
        int newID = 0;
        if(!data.isEmpty()){          
            newID = Integer.parseInt(lastRecordID.substring(idCharLength)) + 1;
        }
        int length = String.valueOf(newID).length();
        String addZeroToID = "";
        for(int i = length ; i < (idLength - idCharLength); i++){
            addZeroToID += "0";
        }
        String newRatingId = idChar + addZeroToID + newID;
        return newRatingId;
    }
    
    public boolean addRating(String ratingStar, String ratingDesc, String productID, String customerID){
        String newRatingId = generateID("RATING", "R", 10 ,1);
        try{
            PreparedStatement stmt;
            String today = LocalDate.now().toString();
            String query = "INSERT INTO RATING VALUES (?,?,?,?,?,?)";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, newRatingId);
            stmt.setString(2, ratingStar);
            stmt.setString(3, ratingDesc);
            stmt.setString(4, today);
            stmt.setString(5, productID);
            stmt.setString(6, customerID);
            int rowInserted = stmt.executeUpdate();
            return rowInserted > 0;
            
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean addReply(String replyDesc, String ratingID, String staffID){
        String newRatingId = generateID("REPLY", "RP", 11 , 2);
        
        System.out.print("This is new ID -> " + newRatingId);
        System.out.print("This is rating ID -> " + ratingID);
        System.out.print("This is staff ID -> " + staffID);
        
        try{
            PreparedStatement stmt;
            String today = LocalDate.now().toString();
            
            String query = "INSERT INTO REPLY VALUES (?,?,?,?,?)";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, newRatingId);
            stmt.setString(2, replyDesc);
            stmt.setString(3, today);
            stmt.setString(4, ratingID);
            stmt.setString(5, staffID);
            
            int rowInserted = stmt.executeUpdate();
            return rowInserted > 0;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
    
    public Connection getConnection() {
        return connection;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}