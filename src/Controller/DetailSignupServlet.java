package Controller;

import Entity.Customer;
import Entity.CustomerDA;
import javax.persistence.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import javax.annotation.Resource;
import javax.transaction.UserTransaction;
import java.util.*;
@WebServlet("/detailSignup")
public class DetailSignupServlet extends HttpServlet {

    @PersistenceContext
    EntityManager em;
    @Resource
    UserTransaction utx;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        CustomerDA customerDA = new CustomerDA(em,utx);
        String email = request.getParameter("email");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String ic = request.getParameter("ic");
        List<String> errors = new ArrayList<>();
        String confirm = request.getParameter("confirm");
        if ((firstname == null || firstname.trim().isEmpty()) &&
                (lastname == null || lastname.trim().isEmpty())) {
                errors.add("Name is required.");
            }
            if (phone == null || phone.trim().isEmpty()) {
                errors.add("Phone number is required.");
            } else if (!phone.matches("01\\d{1}-\\d{7,8}") && !phone.matches("01\\d{8,9}")) {
                errors.add("Phone number format is incorrect.");
            }else{
            try {
                if (customerDA.compareCustomerPhone(phone)) {
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
                if (customerDA.compareCustomerIC(ic)) {
                    errors.add("IC already been used in another account.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            }
            
//                else if(){
//                    errors.add("IC already been used in another account.");
//                }
            
            if (address == null || address.trim().isEmpty()) {
                errors.add("Home address is required.");
            }
            if(!errors.isEmpty()){   
            request.setAttribute("errorMessage", String.join(" ", errors));
            request.getRequestDispatcher("detailSignup.jsp").forward(request, response);
            }else{
        try {
            if ("no".equals(confirm)) {
                HttpSession session = request.getSession();
                Customer customer = new Customer();
                customer.setCustomername(firstname + " " + lastname);
                customer.setPhoneno(phone);
                customer.setIc(ic);
                customer.setHomeaddress(address);
                customer.setEmail(email);
                customer.setPassword(""); 
                session.setAttribute("customer", customer);
                System.out.println("customername: " + customer.getCustomername());
                System.out.println("phone: " + customer.getPhoneno());
                System.out.println("address: " + customer.getHomeaddress());
                System.out.println("ic: " + customer.getIc());
                session.setAttribute("firstname",firstname);
                session.setAttribute("lastname",lastname);
                request.setAttribute("confirmMessage", "Please confirm your details by clicking the Sign Up button again.");
                request.setAttribute("confirming", "true");
                request.getRequestDispatcher("detailSignup.jsp").forward(request, response);
            } else { 
                HttpSession session = request.getSession();
                Customer customer = (Customer) session.getAttribute("customer");
                System.out.println("customername: " + customer.getCustomername());
System.out.println("phone: " + customer.getPhoneno());
System.out.println("address: " + customer.getHomeaddress());
System.out.println("ic: " + customer.getIc());
                customerDA.ContDetailSignUp(customer.getEmail(), customer.getCustomername(), customer.getPhoneno(), customer.getIc(), customer.getHomeaddress());
                session.removeAttribute("firstname");
                session.removeAttribute("lastname");
                response.sendRedirect("login.jsp");
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error during detail signup: " + e.getMessage());
            request.getRequestDispatcher("detailSignup.jsp").forward(request, response);
        }
            }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles the customer detail signup process.";
    }
}
