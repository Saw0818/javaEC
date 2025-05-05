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
@WebServlet("/CreateStaffAccount")
public class CreateStaffAccount extends HttpServlet {
    public PreparedStatement stmt ;
    private Connection conn;

    protected boolean insertAcc(String staffID, String staffUserName, String staffPassword, String email){
        
        String addAcc = "INSERT INTO STAFF (STAFFID, STAFFNAME, EMAIL, PASSWORD, ISFIRSTLOGIN) VALUES (?, ?, ?, ?, ?)";
        boolean result = false;
        
        
        try{
            DBConnection db = new DBConnection();
            db.initializeJdbc();
            Connection conn = db.getConnection();
            
            stmt = conn.prepareStatement(addAcc);
            stmt.setString(1, staffID);
            stmt.setString(2, staffUserName);
            stmt.setString(3, email);
            stmt.setString(4, staffPassword);
            stmt.setBoolean(5, true);
        int addedAcc = stmt.executeUpdate();
        
        if(addedAcc > 0){
        
         result = true;
        }        
        }catch(SQLException ex){
        ex.printStackTrace();
        }finally {
        try {
            if (stmt != null) stmt.close();  
            if (conn != null) conn.close();  
        } catch (SQLException ex) {
            ex.printStackTrace();  
        }
    }
        return result;
    }
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String staffID = request.getParameter("staffID");
        String staffUserName = request.getParameter("staffUserName");
        String staffPassword = request.getParameter("pwd");
        String confirmPassword = request.getParameter("pwdConfirm");
    }

    
    private void createConnection(){
    
    DBConnection db = new DBConnection();
            Connection conn = db.getConnection();
        
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
     
        
        
         String staffID = request.getParameter("staffID");
        String staffUserName = request.getParameter("staffUserName");
        String staffPassword = request.getParameter("pwd");
        String confirmPassword = request.getParameter("pwdConfirm");
        String email = request.getParameter("email");
     
        
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("admCreateStaff.jsp");
        Map<String,String> errors = new HashMap<>();
        
      try{
              DBConnection db = new DBConnection();
              db.initializeJdbc();
            Connection conn = db.getConnection();
          String selectsAll = "SELECT * FROM STAFF" ;
        stmt = conn.prepareStatement(selectsAll);
      
        ResultSet rs = stmt.executeQuery();
      
       String staffPasswordh = PasswordUtils.hashPassword(staffPassword).substring(0, Math.min(PasswordUtils.hashPassword(staffPassword).length(), 20));
       String confirmPasswordh = PasswordUtils.hashPassword(confirmPassword).substring(0, Math.min(PasswordUtils.hashPassword(confirmPassword).length(), 20));
          if(!staffPasswordh.equals(confirmPasswordh)){
              
          errors.put("pwdFormatError", "Password not same with Confirm Password");
          
                
                
          }else if(!staffPassword.matches(".{8,}") ){
          errors.put("pwdFormatError","Password must follow the format: atleast 8 characters");
         
         
          }
          
          while(rs.next()){
          
           if(email.equals(rs.getString("EMAIL"))){
          
          errors.put("emailFormatError", "Email already Exists");
          }
          
          }
          
          if((!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$"))|| (email.matches("\\s*"))){
          
          errors.put("emailFormatError","Email must follow the format: StaffID-name@email.com");
          }
          
          if(!errors.isEmpty()){
          request.setAttribute("errors", errors);
           dispatcher.include(request,response);
          
          }else{
          
          if(insertAcc(staffID,staffUserName, staffPasswordh,email)){
          
         request.setAttribute("addSuccessful",true);
         
          dispatcher.include(request,response);
        
        
          }else{
    request.setAttribute("addSuccessful",false);
         
          dispatcher.include(request,response);
          
          }
         }
       }catch(SQLException ex){
            ex.printStackTrace();
           PrintWriter out = response.getWriter();
           out.println("<h1>SQL Error</h1>");
       
       }
     
      
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
