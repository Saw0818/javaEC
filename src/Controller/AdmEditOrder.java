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
import javax.persistence.*;
import javax.transaction.UserTransaction;


/**
 *
 * @author Admin
 */

@WebServlet("/AdmEditOrder")
public class AdmEditOrder extends HttpServlet {

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
            out.println("<title>Servlet StaffEditOrder</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet StaffEditOrder at " + request.getContextPath() + "</h1>");
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
        processRequest(request, response);
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
        
        String status = request.getParameter("status");
        String orderID = request.getParameter("orderId");
        PreparedStatement stmt;
        DBConnection db = new DBConnection();
        db.initializeJdbc();
        Connection conn = db.getConnection();
        String updatesql = "UPDATE ORDERS SET STATUS = ? WHERE ORDERID = ?";
        
        try{
        stmt = conn.prepareStatement(updatesql);
        stmt.setString(1, status);
        stmt.setString(2,orderID);
        int updatedRow = stmt.executeUpdate();
        
        if(updatedRow > 0){
        System.out.print("Syccess update");
        
        
        }else{
        System.out.print("Failed to update");
        
        }
        
        
        }catch(SQLException ex){
        
        
        
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("admEditOrder.jsp");
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
