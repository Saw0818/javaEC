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

@WebServlet({"/adminProfile", "/adminLogout"})
public class EditAdminProfile extends HttpServlet {

    @PersistenceContext
    EntityManager em;
    @Resource
    UserTransaction utx;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String logout = request.getParameter("logout");
        StaffDA staffDA = new StaffDA(em, utx);
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String ic = request.getParameter("ic");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        List<String> errors = new ArrayList<>();
        String confirm = request.getParameter("confirm");

        if(logout!=null&&logout.equals("logout")){
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
            if ((name == null || name.trim().isEmpty())) {
                    errors.add("Name is required.");
                }
                if (phone == null || phone.trim().isEmpty()) {
                    errors.add("Phone number is required.");
                } else if (!phone.matches("01\\d{1}-\\d{7,8}") && !phone.matches("01\\d{8,9}")) {
                    errors.add("Phone number format is incorrect.");
                }else{
                try {
                    if (staffDA.isPhoneUsedByOtherStaff(phone, email)) {
                        errors.add("Phone Number already been used in another account.");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                }
                if (ic == null || ic.trim().isEmpty()) {
                    errors.add("IC number is required.");
                } else if (!ic.matches("\\d{6}-\\d{2}-\\d{4}") && !ic.matches("\\d{12}")) {
                    errors.add("IC number must be in the format XXXXXX-XX-XXXX or XXXXXXXXXXXX.");
                } else{
                    try {
                    if (staffDA.compareStaffIC(ic,email)) {
                        errors.add("IC already been used in another account.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                }
 
                if(!errors.isEmpty()){   
                request.setAttribute("errorMessage", String.join(" ", errors));
                request.getRequestDispatcher("adminProfile.jsp").forward(request, response);
                }else{
            try {
                if ("no".equals(confirm)) {
                    HttpSession session = request.getSession();
                    TypedQuery<Staff> query = em.createNamedQuery("Staff.findByEmail", Staff.class);
                    query.setParameter("email", email);
                    Staff staff = query.getSingleResult();
                    staff.setStaffname(name);
                    staff.setIc(ic.replace("-", "").trim());
                    staff.setPhoneno(phone);
                    staff.setHomeaddress(address);
                    session.setAttribute("staff", staff);
                    request.setAttribute("confirmMessage", "Please confirm your details by reselect the image and click the Edit button again.");
                    request.setAttribute("confirming", "true");
                    request.getRequestDispatcher("adminProfile.jsp").forward(request, response);
                }else{
                   TypedQuery<Staff> query = em.createNamedQuery("Staff.findByEmail", Staff.class);
                    query.setParameter("EMAIL", email);
                    Staff staff = query.getSingleResult();

                    utx.begin();

                    staff.setStaffname(name);
                    staff.setIc(ic.replace("-", "").trim());
                    staff.setPhoneno(phone);
                    staff.setHomeaddress(address);

                em.merge(staff);
                utx.commit();

                request.getSession().setAttribute("staff", staff);
                request.getRequestDispatcher("adminProfile.jsp").forward(request, response);
            }
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