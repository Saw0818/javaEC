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
@WebServlet("/EditStaff")
public class EditStaff extends HttpServlet {
    
    
  PreparedStatement stmt;
  
   /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private boolean updateAcc(String staffID, String staffName, String staffIc, String phoneNo, String homeAddress, String email, String password){
     boolean result = false;
     String updatesql = "UPDATE STAFF SET STAFFNAME = ?, IC = ?, PHONENO = ?, HOMEADDRESS = ?, EMAIL = ?, PASSWORD = ? WHERE STAFFID = ?";
     
     System.out.print("door to connect");
     
     
     
     
     
     try{
     DBConnection db = new DBConnection();
     db.initializeJdbc();
     Connection conn = db.getConnection();
     
      
          System.out.print(password);

       String passwordh = PasswordUtils.hashPassword(password).substring(0, Math.min(PasswordUtils.hashPassword(password).length(), 20));

            System.out.print(passwordh);

       
       
       
     stmt = conn.prepareStatement(updatesql);
     stmt.setString(1, staffName);
     stmt.setString(2, staffIc);
     stmt.setString(3, phoneNo);
     stmt.setString(4, homeAddress);
     stmt.setString(5, email);
     stmt.setString(6, passwordh);
     stmt.setString(7, staffID);
     
     int updatedRow = stmt.executeUpdate();
     
     if(updatedRow > 0){
     
     result = true;
     
     }
     
     }catch(SQLException ex){
     
     ex.printStackTrace();
     }
        
    
    return result;
    }
    
    protected void setFirstLogin(String staffID){
    String updatesql = "UPDATE STAFF SET ISFIRSTLOGIN = ? WHERE STAFFID =?";
    try{
    
        DBConnection db = new DBConnection();
        
        db.initializeJdbc();
        Connection conn = db.getConnection();
        stmt = conn.prepareStatement(updatesql);
        stmt.setBoolean(1, false);
        stmt.setString(2, staffID);
        stmt.executeUpdate();
        
    }catch(SQLException ex){
    ex.printStackTrace();
    
    }
    
    }
    
    
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet EditStaff</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditStaff at " + request.getContextPath() + "</h1>");
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
        
      
      
     String staffID = request.getParameter("staffID");
     String staffName = request.getParameter("staffName");
     String staffIc = request.getParameter("staffIc");
     String phoneNo = request.getParameter("phoneNo");
     String homeAddress = request.getParameter("homeAddress");
     String email = request.getParameter("email");
     String password = request.getParameter("password");
     Map<String, String> errors = new HashMap<>();
     
     System.out.print("editstaff get");
     RequestDispatcher dispatcher = request.getRequestDispatcher("staffFirstLoginEdit.jsp");
     request.setAttribute("staffID", staffID);
     if(staffIc == null || staffIc.matches("\\s*")){
         
         errors.put("icFormatError", "Cannot be Left Empty");
         
     }else if(!staffIc.matches("^\\d{12}$")){
     
         errors.put("icFormatError", "IC without '-': (eg)123456789012");
         
    
     
     }
     
     if(staffName == null || staffName.matches("\\s*")){
     errors.put("staffNameError", "Cannot Be Left Empty");
     
     }
     
     if(phoneNo == null || phoneNo.matches("\\s*")){
     errors.put("phoneError", "Cannot Be Left Empty");
     
     }
     
     if(homeAddress == null || homeAddress.matches("\\s*")){
     errors.put("homeAddressError", "Canot Be left Empty");
     }
     
     if(email == null || email.matches("\\s*")){
         
         
             errors.put("emailFormatError","Cannot Be Left Empty");
             
             }else if(!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")){
     
       errors.put("emailFormatError","Email must follow the format: StaffID-name@email.com");
        
     
     }
     
      if(password.length() <8 && !password.trim().isEmpty()){
     
       errors.put("pwdFormatError","Password must follow the format: atleast 8 characters");
       
     }else if(password.trim().isEmpty() || password == null){
     
         errors.put("pwdFormatError", "Password Cannot be left empty");
     }
     
     
     System.out.print("1");
     if(!errors.isEmpty()){
         System.out.print("2");
     request.setAttribute("errors", errors);
     
      dispatcher.include(request,response);
      
     return;
     }else{
         System.out.print("3");
     if(updateAcc(staffID, staffName, staffIc, phoneNo, homeAddress, email, password)){
     System.out.print("4");
         request.setAttribute("editSuccessful", true);
              request.setAttribute("FirstLogin", false);
setFirstLogin(staffID);
      dispatcher.include(request,response);
      return;
      
     }else{
         System.out.print("5");
     request.setAttribute("editSuccessful", false);
               dispatcher.include(request,response);
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
        
        
     String staffID = request.getParameter("staffID");
     String staffName = request.getParameter("staffName");
     String staffIc = request.getParameter("staffIc");
     String phoneNo = request.getParameter("phoneNo");
     String homeAddress = request.getParameter("homeAddress");
     String email = request.getParameter("email");
     String password = request.getParameter("password");
   
     Map<String, String> errors = new HashMap<>();
     
     
     RequestDispatcher dispatcher = request.getRequestDispatcher("editStaffRecord.jsp");
     request.setAttribute("staffID", staffID);
     
     if(!staffIc.matches("^\\d{12}$")){
     
         errors.put("icFormatError", "IC without '-': (eg)123456789012");
         
    
     
     }
     
     if(!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")){
     
       errors.put("emailFormatError","Email must follow the format: StaffID-name@email.com");
        
     
     }
     
     if(password.length() <8 && !password.trim().isEmpty()){
     
       errors.put("pwdFormatError","Password must follow the format: atleast 8 characters");
       
     }else if(password.trim().isEmpty() || password == null){
     
         errors.put("pwdFormatError", "Password Cannot be left empty");
     }
     
     


if (!errors.isEmpty()) {
    request.setAttribute("errors", errors);
    dispatcher.include(request, response);
    return;
} else {
    if (updateAcc(staffID, staffName, staffIc, phoneNo, homeAddress, email, password)) {
        request.setAttribute("editSuccessful", true);
    } else {
        request.setAttribute("editSuccessful", false);
    }
    dispatcher.include(request, response);
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
