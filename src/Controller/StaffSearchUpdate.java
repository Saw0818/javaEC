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
import Entity.Staff;
import Entity.DBConnection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Admin
 */
@WebServlet("/StaffSearchUpdate")
public class StaffSearchUpdate extends HttpServlet {
   private PreparedStatement stmt;
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
        String staffID = request.getParameter("param1");
        String action = request.getParameter("action");
        
        if("edit".equalsIgnoreCase(action)){
            request.setAttribute("staffID", staffID);
        RequestDispatcher dispatcher = request.getRequestDispatcher("editStaffRecord.jsp");
        dispatcher.forward(request, response);
        
        }else if("delete".equalsIgnoreCase(action)){
        
            
             try{
        DBConnection db = new DBConnection();
        db.initializeJdbc();
        Connection conn = db.getConnection();
        
     
        String sql = "SELECT * FROM STAFF WHERE STAFFID = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, staffID);
        Staff staff = null;
        staff = new Staff();
        ResultSet rs = stmt.executeQuery();
        
        
         if(rs.next()){
        staff.setStaffid(rs.getString("STAFFID"));
        staff.setStaffname(rs.getString("STAFFNAME"));
        staff.setEmail(rs.getString("EMAIL"));
        staff.setIc(rs.getString("IC"));
        staff.setPhoneno(rs.getString("PHONENO"));
        staff.setHomeaddress(rs.getString("HOMEADDRESS"));
        staff.setPassword(rs.getString("PASSWORD"));
        
        
          PrintWriter out = response.getWriter();
         
           out.println("<html><body>");
            out.println("<h1>Are You Sure You Want To Delete The Following Account?</h1>");
            out.println("<p>Staff ID: " + (staff.getStaffid() == null ? "" : staff.getStaffid()) + "</p>");
            out.println("<p>Staff Name: " + (staff.getStaffname() == null ? "" : staff.getStaffname()) + "</p>");
            out.println("<p>Staff Email: " + (staff.getEmail() == null ? "" : staff.getEmail()) + "</p>");
            out.println("<p>Staff IC: " + (staff.getIc() == null ? "" : staff.getIc()) + "</p>");
            out.println("<p>Staff Phone No: " + (staff.getPhoneno() == null ? "" : staff.getPhoneno()) + "</p>");
            out.println("<p>Staff Home Address: " + (staff.getHomeaddress() == null ? "" : staff.getHomeaddress()) + "</p>");
            out.println("<form action=\"DeleteStaffServlet\" method=\"post\">");
            out.println("<input type=\"hidden\" name=\"staffID\" value=\"" + staff.getStaffid() + "\">");
            out.println("<button><a href='staffList.jsp'>Cancel</a></button>");
            out.println("<input type=\"submit\" value=\"Confirm Delete\">");
            out.println("</form>");
            out.println("</body></html>");
    
        
         }
        
        
        
        
        }catch(Exception ex){
        ex.printStackTrace();
        
        }
            
       

  
        
        }
        
        
        
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
        
        String searchByID = request.getParameter("searchByID");
        request.setAttribute("searched", true);
        Staff staff = null;
        
        
        try{
        DBConnection db = new DBConnection();
        db.initializeJdbc();
        Connection conn = db.getConnection();
        
        if(searchByID != null){
        String sql = "SELECT * FROM STAFF WHERE STAFFID = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, searchByID);
        
        ResultSet rs = stmt.executeQuery();
        
       if(rs.next()){
           
           staff= new Staff();
          staff.setStaffid(rs.getString("STAFFID"));
           staff.setStaffname(rs.getString("STAFFNAME"));
           staff.setIc(rs.getString("IC"));
           staff.setPhoneno(rs.getString("PHONENO"));
           staff.setHomeaddress(rs.getString("HOMEADDRESS"));
           staff.setEmail(rs.getString("EMAIL"));
           staff.setPassword(rs.getString("PASSWORD"));
           staff.setIsfirstlogin(rs.getBoolean("ISFIRSTLOGIN"));
           
       }
        
        
        }
        
        
        
        
        }catch(Exception ex){
        ex.printStackTrace();
        
        }
        
           request.setAttribute("staffList", staff);
           
        RequestDispatcher dispatcher = request.getRequestDispatcher("staffList.jsp");
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
