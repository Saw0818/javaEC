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
import Entity.Staff;
import Entity.DBConnection;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.sql.*;
import java.util.*;
import Entity.*;
import java.time.LocalDateTime;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
/**
 *
 * @author Admin
 */


@WebServlet("/EditOrderSearch")
public class EditOrderSearch extends HttpServlet {

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

    String searchByID = request.getParameter("searchByID");
    request.setAttribute("searched", true); 
    Order order = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        if (searchByID != null && !searchByID.trim().isEmpty()) {
            DBConnection db = new DBConnection();
            db.initializeJdbc();
            conn = db.getConnection();

            String sql = "SELECT * FROM ORDERS WHERE ORDERID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, searchByID.trim());
            rs = stmt.executeQuery();

            if (rs.next()) {
                order = new Order();
                order.setOrderId(rs.getString("ORDERID"));
                order.getCart().setCartID(rs.getString("CARTID"));
                order.setCustomerId(rs.getString("CUSTOMERID"));
                order.setTax(rs.getDouble("TAX"));
                order.setDiscountAmt(rs.getDouble("DISCOUNTAMT"));
                order.getShipping().setShippingId(rs.getString("SHIPPINGID"));
                order.setTotal(rs.getDouble("TOTAL"));
                order.getShippingAddress().setAddressId(rs.getString("ADDRESSID"));
                order.setPaymentMethodId(rs.getString("METHODID"));
                order.setStatus(rs.getString("STATUS"));

                Timestamp timestamp = rs.getTimestamp("CREATED_AT");
                if (timestamp != null) {
                    order.setTimestamp(timestamp.toLocalDateTime());
                }
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    } finally {
        try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
        try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
        try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
    }

  
    request.setAttribute("order", order);
    request.setAttribute("searched", true);
    RequestDispatcher dispatcher = request.getRequestDispatcher("staffEditOrder.jsp");
    dispatcher.forward(request, response);
}

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
