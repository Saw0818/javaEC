package Controller;

import Entity.*;
import Entity.Staff;
import javax.persistence.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import javax.annotation.*;
import javax.transaction.UserTransaction;
import java.util.*;

public class LoginServlet extends HttpServlet {

    @PersistenceContext
    EntityManager em;
    @Resource
    UserTransaction utx;

    @Override
    public void init() throws ServletException {
        super.init();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

        CustomerDA customerDA = new CustomerDA(em, utx);
        StaffDA staffDA = new StaffDA(em, utx);  
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        List<String> errors = new ArrayList<>();

        if (email == null || email.trim().isEmpty()) {
            errors.add("Email is required.");
        } else if (!customerDA.isValidEmail(email)) {
            errors.add("Invalid email format.");
        }
        if (password == null || password.trim().isEmpty()) {
            errors.add("Password is required.");
        } else if (password.length() < 8) {
            errors.add("Password must have at least 8 characters.");
        }

        if (!errors.isEmpty()) {
            System.out.print("error 1");
            request.setAttribute("errorMessage", String.join(" ", errors));
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return; 
        }

        try {
    boolean isValidCustomerLogin = customerDA.isLoginValid(email, password);
    boolean isValidStaffLogin = staffDA.isLoginValid(email, password);  
    
    System.out.print("Customer is :"+isValidCustomerLogin);
    if (isValidCustomerLogin) {
        Customer customer = customerDA.getCustomerByEmail(email);
        HttpSession session = request.getSession();
        session.setAttribute("customer", customer);
        session.setAttribute("userRole", "customer");

        String homePage = getServletConfig().getInitParameter("customerHomePage");
        String headerPage = getServletConfig().getInitParameter("customerHeader");
        String footerPage = "customerFooter.jsp";

        session.setAttribute("header", headerPage);
        session.setAttribute("footer", footerPage);
        response.sendRedirect(homePage);
        return; 
    }else if(email.equals("admin@gmail.com")&&password.equals("admin123")){
        String homePage = getServletConfig().getInitParameter("adminHomePage");
        String headerPage = getServletConfig().getInitParameter("adminHeader");
        TypedQuery<Customer> query = em.createNamedQuery("Customer.findAll", Customer.class);
        List<Customer> customers = query.getResultList();
        Staff staff = staffDA.getStaffByEmail(email);
        HttpSession session = request.getSession();
        session.setAttribute("customers", customers);
        session.setAttribute("staff", staff);
        session.setAttribute("header", headerPage);
        session.setAttribute("userRole", "admin");
        response.sendRedirect(homePage);
    } else if (isValidStaffLogin) {
        Staff staff = staffDA.getStaffByEmail(email);
        HttpSession session = request.getSession();
        session.setAttribute("staff", staff);
        session.setAttribute("userRole", "staff");

        String homePage = getServletConfig().getInitParameter("staffHomePage");
        String headerPage = getServletConfig().getInitParameter("staffHeader");
        String footerPage = "staffFooter.jsp";
        session.setAttribute("header", headerPage);
        session.setAttribute("footer", footerPage);
        response.sendRedirect(homePage);
        return;  
    }else {
        request.setAttribute("errorMessage", "Invalid email or password.");
        request.getRequestDispatcher("login.jsp").forward(request, response);
        return; 
    }
} catch (Exception e) {
    request.setAttribute("errorMessage", "Login error: " + e.getMessage());
    request.getRequestDispatcher("login.jsp").forward(request, response);
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
        return "Handles login process.";
    }
}
