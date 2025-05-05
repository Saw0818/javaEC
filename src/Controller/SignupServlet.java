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
@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    @PersistenceContext
    EntityManager em;
    @Resource
    UserTransaction utx;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        CustomerDA customerDA = new CustomerDA(em,utx);
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("repeat-password");
        List<String> errors = new ArrayList<>();
        boolean isSignup = "signup".equalsIgnoreCase(request.getParameter("formType"));
        String confirm = request.getParameter("confirm");
        if (email == null || email.trim().isEmpty()) {
            errors.add("Email is required.");
        }else if (!customerDA.isValidEmail(email)) {
            errors.add("Invalid email format.");
        } 
        if (isSignup) {
             if (password == null || password.trim().isEmpty()) {
                errors.add("Password is required.");
            } else if (password.length() < 8) {
                errors.add("Password must have at least 8 characters.");
            }
             if(!errors.isEmpty()){   
                    request.setAttribute("errorMessage", String.join(" ", errors));
                    request.getRequestDispatcher("signup.jsp").forward(request, response);
                    }
            try {
                if (password != null && repeatPassword != null && password.equals(repeatPassword)) {
                    if (customerDA.compareCustomerEmail(email)) {
                        request.setAttribute("errorMessage", "Email already exists.");
                        request.getRequestDispatcher("signup.jsp").forward(request, response);
                        return;
                    }
                    
                    if ("no".equals(confirm)) {
                        HttpSession session = request.getSession();
                        Customer customer = new Customer(null, null, null, null, null, email, password,null);
                        session.setAttribute("customer", customer);
                        session.setAttribute("email", email);
                        session.setAttribute("password", password);
                        request.setAttribute("confirmMessage", "Please confirm your signup.");
                        request.setAttribute("confirming", "true");
                        request.getRequestDispatcher("signup.jsp").forward(request, response);
                    } else {
                        customerDA.SignUpCustomer(email, password);
                        HttpSession session = request.getSession();
                        session.removeAttribute("password");                   
                        request.setAttribute("confirming", "false");
                        response.sendRedirect("detailSignup.jsp");
                    }
                } else {
                    request.setAttribute("errorMessage", "Passwords do not match.");
                    request.getRequestDispatcher("signup.jsp").forward(request, response);
                }
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Error saving signup info: " + e.getMessage());
                request.getRequestDispatcher("signup.jsp").forward(request, response);
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
        return "Handles user signup process.";
    }
}
