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

@WebServlet({"/staffProfile", "/staffLogout"})
public class EditStaffProfile extends HttpServlet {

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
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        List<String> errors = new ArrayList<>();
        String confirm = request.getParameter("confirm");

        if (logout!=null&&"logout".equals(logout)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                Enumeration<String> attributeNames = session.getAttributeNames();
                while (attributeNames.hasMoreElements()) {
                    String attr = attributeNames.nextElement();
                    session.removeAttribute(attr);
                }
                session.invalidate();
            }
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        if ((name == null || name.trim().isEmpty())) {
            errors.add("Name is required.");
        }
        if (phone == null || phone.trim().isEmpty()) {
            errors.add("Phone number is required.");
        } else if (!phone.matches("01\\d{1}-\\d{7,8}") && !phone.matches("01\\d{8,9}")) {
            errors.add("Phone number format is incorrect.");
        } else {
            try {
                if (staffDA.isPhoneUsedByOtherStaff(phone, email)) {
                    errors.add("Phone Number already been used in another account.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (address == null || address.trim().isEmpty()) {
            errors.add("Home address is required.");
        }
        System.out.println("Name: " + name);
        System.out.println("Phone: " + phone);
        System.out.println("Address: " + address);

        if (!errors.isEmpty()) {
            request.setAttribute("errorMessage", String.join(" ", errors));
            request.getRequestDispatcher("staffProfile.jsp").forward(request, response);
            return;
        }

        try {
            if ("no".equals(confirm)) {
                HttpSession session = request.getSession();
                TypedQuery<Staff> query = em.createNamedQuery("Staff.findByEmail", Staff.class);
                query.setParameter("email", email);
                Staff staff = query.getSingleResult();

                staff.setStaffname(name);
                staff.setPhoneno(phone);
                staff.setHomeaddress(address);

                session.setAttribute("staff", staff);
                request.setAttribute("confirmMessage", "Please confirm your details by reselecting the image and clicking the Edit button again.");
                request.setAttribute("confirming", "true");
                request.getRequestDispatcher("staffProfile.jsp").forward(request, response);
            } else {
                TypedQuery<Staff> query = em.createNamedQuery("Staff.findByEmail", Staff.class);
                query.setParameter("email", email);
                Staff staff = query.getSingleResult();

                utx.begin();

                staff.setStaffname(name);
                staff.setPhoneno(phone);
                staff.setHomeaddress(address);

                em.merge(staff);
                utx.commit();

                request.getSession().setAttribute("staff", staff);
                request.getRequestDispatcher("staffProfile.jsp").forward(request, response);
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
