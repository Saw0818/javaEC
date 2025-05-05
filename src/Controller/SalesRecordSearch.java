/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.RequestDispatcher;
import Entity.*;
import Entity.DBConnection;
import java.util.HashSet;
import java.util.Set;
import java.util.*;

/**
 *
 * @author Admin
 */
@WebServlet(name = "SalesRecordSearch", urlPatterns = {"/SalesRecordSearch"})
public class SalesRecordSearch extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SalesRecordSearch</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SalesRecordSearch at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private SalesRecord[] topTenProduct(String year, String month, String day, String date) {
    SalesRecord[] salesRecordArray = null;

    StringBuilder sql = new StringBuilder("SELECT \n" +
    "    P.PRODUCTID,\n" +
    "    P.PRODUCTNAME,\n" +
    "    SUM(CI.QUANTITY) AS SOLDUNITS,\n" +
    "    P.PRODUCTPRICE AS UNITPRICE\n" +
    "FROM ORDERS O\n" +
    "JOIN CARTS C ON O.CARTID = C.CARTID\n" +
    "JOIN CARTITEMS CI ON CI.CARTID = C.CARTID\n" +
    "JOIN PRODUCT P ON P.PRODUCTID = CI.PRODUCTID\n" +
    "WHERE 1=1\n");

List<Object> parameter = new ArrayList<>();

if (year != null && !year.isEmpty()) {
    sql.append(" AND YEAR(O.CREATED_AT) = ? ");
    parameter.add(Integer.parseInt(year));
}
if (month != null && !month.isEmpty()) {
    sql.append(" AND MONTH(O.CREATED_AT) = ? ");
    parameter.add(Integer.parseInt(month));
}
if (day != null && !day.isEmpty()) {
    sql.append(" AND DAY(O.CREATED_AT) = ? ");
    parameter.add(Integer.parseInt(day));
}

sql.append(" GROUP BY P.PRODUCTID, P.PRODUCTNAME, P.PRODUCTPRICE ");
sql.append(" ORDER BY SOLDUNITS DESC FETCH FIRST 10 ROWS ONLY");

    
System.out.print("Before Connection ok");
    try {
        DBConnection db = new DBConnection();
        db.initializeJdbc();
        Connection conn = db.getConnection();

        PreparedStatement stmt = conn.prepareStatement(sql.toString());
System.out.print("preparestatement ok");
        for (int i = 0; i < parameter.size(); i++) {
            stmt.setObject(i + 1, parameter.get(i));
        }

        ResultSet rs = stmt.executeQuery();
System.out.print("past execution ok");
        List<SalesRecord> salesList = new ArrayList<>();

        int topCount = 0;
        while (rs.next() && topCount<10) {
            String productId = rs.getString("PRODUCTID");
            String productName = rs.getString("PRODUCTNAME");
            int soldUnits = rs.getInt("SOLDUNITS");
            int unitPrice = rs.getInt("UNITPRICE");
System.out.print("Sold Units :"+soldUnits);
            SalesRecord record = new SalesRecord(productId, productName, soldUnits, unitPrice, "");
            salesList.add(record);
            topCount++;
        }
System.out.print("top ten size" + salesList.size());
        salesRecordArray = salesList.toArray(new SalesRecord[0]);

    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    return salesRecordArray;
}

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String day = request.getParameter("day");
       try{ 
       DBConnection db = new DBConnection();
        db.initializeJdbc();
        Connection conn = db.getConnection();
           
       StringBuilder sql = new StringBuilder("SELECT \n" +
"    P.PRODUCTID,\n" +
"    P.PRODUCTNAME,\n" +
"    O.CREATED_AT,\n" +
"    CI.QUANTITY AS SOLDUNITS,\n" +
"    P.PRODUCTPRICE AS UNITPRICE\n" +
"FROM ORDERS O\n" +
"JOIN CARTS C ON O.CARTID = C.CARTID\n" +
"JOIN CARTITEMS CI ON C.CARTID = CI.CARTID\n" +
"JOIN PRODUCT P ON CI.PRODUCTID = P.PRODUCTID\n" +
"WHERE 1=1");

List<Object> parameters = new ArrayList<>();

if (year != null && !year.isEmpty()) {
    sql.append(" AND YEAR(CREATED_AT) = ?");
    parameters.add(Integer.parseInt(year));
}
if (month != null && !month.isEmpty()) {
    sql.append(" AND MONTH(CREATED_AT) = ?");
    parameters.add(Integer.parseInt(month));
}
if (day != null && !day.isEmpty()) {
    sql.append(" AND DAY(CREATED_AT) = ?");
    parameters.add(Integer.parseInt(day));
}

PreparedStatement stmt = conn.prepareStatement(sql.toString());

// Bind parameters
for (int i = 0; i < parameters.size(); i++) {
    stmt.setObject(i + 1, parameters.get(i));
    
}
ResultSet rs = stmt.executeQuery();

List<SalesRecord> salesList = new ArrayList<>();

while (rs.next()) {
    String productId = rs.getString("PRODUCTID");
    String productName = rs.getString("PRODUCTNAME");
    int soldUnits = rs.getInt("SOLDUNITS"); 
    double unitPrice = rs.getDouble("UNITPRICE"); 
    String date = rs.getString("CREATED_AT"); 

    SalesRecord record = new SalesRecord(productId, productName, soldUnits, unitPrice, date);
    salesList.add(record);
    System.out.print(rs.getString("PRODUCTID"));
}


SalesRecord[] salesRecord = salesList.toArray(new SalesRecord[0]);

SalesRecord[] topTenSales = topTenProduct(year, month, day, "");
request.setAttribute("topTen", topTenSales);
request.setAttribute("salesRecord", salesRecord);
        
        }catch(SQLException e){
        e.printStackTrace();
        
        }
    
        
RequestDispatcher dispatcher = request.getRequestDispatcher("salesRecord.jsp");
dispatcher.forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
