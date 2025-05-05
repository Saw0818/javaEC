/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Entity.*;
import java.io.File;
import java.io.IOException;
import javax.annotation.Resource;
import javax.persistence.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.transaction.UserTransaction;
import javax.servlet.http.*;
import java.nio.file.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
@WebServlet({"/customerAccount","/customerLogout"})
@MultipartConfig(maxFileSize = 5 * 1024 * 1024) // 5MB
public class EditProfile extends HttpServlet {

    @PersistenceContext
    EntityManager em;
    @Resource
    UserTransaction utx;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.print("Try Here");
        // Get form fields
        String logout = request.getParameter("logout");
        CustomerDA customerDA = new CustomerDA(em,utx);
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String ic = request.getParameter("ic");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        List<String> errors = new ArrayList<>();
        String confirm = request.getParameter("confirm");
        
        if(logout != null && logout.equals("logout")){
            if (request.getSession(false) != null) {
                Enumeration<String> attributeNames = request.getSession(false).getAttributeNames();
                while (attributeNames.hasMoreElements()) {
                    String attr = attributeNames.nextElement();
                    request.getSession(false).removeAttribute(attr);
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            }else{
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        }
        
        if(phone != null && !phone.trim().isEmpty()){
            if (!phone.matches("01\\d{1}-\\d{7,8}") && !phone.matches("01\\d{8,9}")) {
                errors.add("Phone number format is incorrect.");
            }else{
                try{
                    if (customerDA.isPhoneUsedByOtherCustomer(phone, email)) {
                        errors.add("Phone Number already been used in another account.");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }
        if(ic!=null&&!ic.trim().isEmpty()){
            if (!ic.matches("\\d{6}-\\d{2}-\\d{4}") && !ic.matches("\\d{12}")) {
                errors.add("IC number must be in the format XXXXXX-XX-XXXX or XXXXXXXXXXXX.");
            }else{
                try {
                    if (customerDA.compareCustomerIC(ic,email)) {
                        errors.add("IC already been used in another account.");
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
            
            
            if(!errors.isEmpty()){   
            request.setAttribute("errorMessage", String.join(" ", errors));
            request.getRequestDispatcher("customerAccount.jsp").forward(request, response);
            }else{
        try {
            if ("no".equals(confirm)) {
                HttpSession session = request.getSession();
                TypedQuery<Customer> query = em.createNamedQuery("Customer.findByEmail", Customer.class);
                query.setParameter("email", email);
                Customer customer = query.getSingleResult();
                customer.setCustomername(name);
                customer.setIc(ic.replace("-", "").trim());
                customer.setPhoneno(phone);
                customer.setHomeaddress(address);
                session.setAttribute("customer", customer);
                request.setAttribute("confirmMessage", "Please confirm your details by reselect the image and click the Edit button again.");
                request.setAttribute("confirming", "true");
                request.getRequestDispatcher("customerAccount.jsp").forward(request, response);
            }else{
                TypedQuery<Customer> query = em.createNamedQuery("Customer.findByEmail", Customer.class);
                query.setParameter("email", email);
                Customer customer = query.getSingleResult();
                
            utx.begin();

            // Get existing customer from DB
            customer.setCustomername(name);
            customer.setIc(ic.replace("-", "").trim());
            customer.setPhoneno(phone);
            customer.setHomeaddress(address);

            // Update customer fields
            
        
            // Handle file upload
            Part filePart = request.getPart("imageFile");
    if (filePart != null && filePart.getSize() > 0) {
        String fileName = filePart.getSubmittedFileName();
                
        String uploadPath = getServletContext().getRealPath("");
        Path currentPath = Paths.get(uploadPath).getParent().getParent();
        String stringPath = currentPath + File.separator + "web" + File.separator + "uploads";
        Path nowPath = Paths.get(stringPath, fileName);
        File uploadDir = new File(stringPath);

        if (!uploadDir.exists()){
            uploadDir.mkdirs();
        }

        try (InputStream fileContent = filePart.getInputStream()) {
            Files.copy(fileContent,nowPath,StandardCopyOption.REPLACE_EXISTING);
            customer.setProfileImage("uploads/"+fileName);
        }catch(Exception ex){
            ex.printStackTrace();
            response.getWriter().print("The image upload fail.");
        }
    }

            em.merge(customer);
            utx.commit();

            // Update session bean if needed
            request.getSession().setAttribute("customer", customer);

            request.getRequestDispatcher("customerAccount.jsp").forward(request, response);            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            response.getWriter().println("Update failed: " + ex.getMessage());
        }
        
    }
    }
}