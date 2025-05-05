/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;
import Entity.*;
import javax.persistence.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import javax.annotation.Resource;
import javax.transaction.UserTransaction;
import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;


/**
 *
 * @author User
 */
@WebServlet("/admCreateUser")
public class EditCustomerServlet extends HttpServlet {
    @PersistenceContext
    EntityManager em;
    @Resource
    UserTransaction utx;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        CustomerDA customerDA = new CustomerDA(em,utx);
        String customerID = request.getParameter("customerID");
        String name = request.getParameter("customerName");
        String ic = request.getParameter("customerIc");
        String phone = request.getParameter("phoneNo");
        String address = request.getParameter("homeAddress");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String selectedID = request.getParameter("selectedCustomer");
        List<String> errors = new ArrayList<>();
        Customer customer;
    try{
        switch (action) {
            case "create":
                if (email == null || email.trim().isEmpty()) {
                    errors.add("Email is required.");
                }else if (!customerDA.isValidEmail(email)) {
                    errors.add("Invalid email format.");
                }else if (customerDA.compareCustomerEmail(email)) {
                        request.setAttribute("errorMessage", "Email already exists.");
                        request.getRequestDispatcher("admCreateUser.jsp").forward(request, response);
                        return;
                    }
                if (password == null || password.trim().isEmpty()) {
                errors.add("Password is required.");
                } else if (password.length() < 8) {
                errors.add("Password must have at least 8 characters.");
                }
            if (!phone.matches("01\\d{1}-\\d{7,8}") && !phone.matches("01\\d{8,9}")) {
                errors.add("Phone number format is incorrect.");
            }else{
            try {
                if (customerDA.compareCustomerPhone(phone)) {
                    errors.add("Phone Number already been used in another account.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            }if (!ic.matches("\\d{6}-\\d{2}-\\d{4}") && !ic.matches("\\d{12}")) {
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
            if (!errors.isEmpty()) {
                request.setAttribute("errorMessage", String.join(" ", errors));
                request.getRequestDispatcher("admCreateUser.jsp").forward(request, response);
                return;
            }else{
            
            utx.begin(); 
            customer = new Customer();
            customer.setCustomerid("c" + String.format("%05d", customerDA.getAvailableCustomerId()));
            customer.setCustomername(name);
            customer.setIc(ic);
            customer.setPhoneno(phone);
            customer.setHomeaddress(address);
            customer.setEmail(email);
            customer.setPassword(PasswordUtils.hashPassword(password).substring(0, Math.min(PasswordUtils.hashPassword(password).length(), 20)));
            em.persist(customer);
            utx.commit(); 
            TypedQuery<Customer> query = em.createNamedQuery("Customer.findAll", Customer.class);
            List<Customer> customers = query.getResultList();

            HttpSession session = request.getSession();
            session.setAttribute("customers", customers);
            request.setAttribute("message", "Customer created successfully.");
            }
            break;

            case "update":
                
                utx.begin();
                
        if (ic != null && !ic.trim().isEmpty()) {
            if (!ic.matches("\\d{6}-\\d{2}-\\d{4}") && !ic.matches("\\d{12}")) {
            errors.add("IC number must be in the format XXXXXX-XX-XXXX or XXXXXXXXXXXX.");
        } else {
            ic = ic.replace("-", "").trim();
            TypedQuery<Customer> icQuery = em.createQuery(
                "SELECT c FROM Customer c WHERE c.ic = :ic AND c.customerid <> :id", Customer.class
            );
            icQuery.setParameter("ic", ic);
            icQuery.setParameter("id", customerID);
            try {
                if (icQuery.getSingleResult() != null) {
                    errors.add("IC number is already used in another account.");
                }
            } catch (NoResultException ignored) {}
        }
    }

    if (phone != null && !phone.trim().isEmpty()) {
        if (!phone.matches("01\\d{1}-\\d{7,8}") && !phone.matches("01\\d{8,9}")) {
            errors.add("Phone number format is incorrect.");
        } else {
            TypedQuery<Customer> phoneQuery = em.createQuery(
                "SELECT c FROM Customer c WHERE c.phoneno = :phone AND c.customerid <> :id", Customer.class
            );
            phoneQuery.setParameter("phone", phone);
            phoneQuery.setParameter("id", customerID);
            try {
                if (phoneQuery.getSingleResult() != null) {
                    errors.add("Phone number is already used in another account.");
                }
            } catch (NoResultException ignored) {}
        }
    }

    // Validate email only if not empty
    if (email != null && !email.trim().isEmpty()) {
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            errors.add("Email format is invalid.");
        } else {
            TypedQuery<Customer> emailQuery = em.createQuery(
                "SELECT c FROM CUSTOMER c WHERE c.EMAIL = :EMAIL AND c.CUSTOMERID <> :ID", Customer.class
            );
            emailQuery.setParameter("EMAIL", email);
            emailQuery.setParameter("ID", customerID);
            try {
                if (emailQuery.getSingleResult() != null) {
                    errors.add("Email is already used in another account.");
                }
            } catch (NoResultException ignored) {}
        }
    }


System.out.print("no error yet");
    if (!errors.isEmpty()) {
        System.out.print("error level 2");
        request.setAttribute("errorMessage", String.join(" ", errors));
        break;
    }
    System.out.print("not find yet");
    Customer update = em.find(Customer.class, selectedID);
    System.out.print(update);
    if (update != null) {
        if (name != null && !name.trim().isEmpty()) {
            update.setCustomername(name.trim());
        }
        if (ic != null && !ic.trim().isEmpty()) {
            update.setIc(ic.trim());
        }
        if (phone != null && !phone.trim().isEmpty()) {
            update.setPhoneno(phone.trim());
        }
        if (email != null && !email.trim().isEmpty()) {
            update.setEmail(email.trim());
        }
        if (password != null && !password.trim().isEmpty()) {
            System.out.print(PasswordUtils.hashPassword(password).substring(0, 20));
            
            update.setPassword(PasswordUtils.hashPassword(password).substring(0, 20));
        }
        if (address != null && !address.trim().isEmpty()) {
            update.setHomeaddress(address.trim());
        }

        em.merge(update);
        utx.commit();
        TypedQuery<Customer> query = em.createNamedQuery("Customer.findAll", Customer.class);
            List<Customer> customers = query.getResultList();

            HttpSession session = request.getSession();
            session.setAttribute("customers", customers);
        request.setAttribute("message", "Customer updated successfully.");
    } else {
        request.setAttribute("errorMessage", "Customer not found.");
    }
    break;
           
            case "delete":
                System.out.print("level 3");
                 utx.begin(); 

            customer = em.find(Customer.class, selectedID);

           
                 if (customer != null) {
                try{
                em.createNativeQuery("DELETE FROM ORDERS WHERE CUSTOMERID = ?").setParameter(1, selectedID).executeUpdate();
                }catch(Exception e){
                    System.out.print("1 "+e);
                }
                try{
                 List<String> cartIds = em.createNativeQuery("SELECT CARTID FROM CARTS WHERE CUSTOMERID = ?")
                            .setParameter(1, selectedID)
                            .getResultList();
                System.out.println("Looking for carts with customer ID: " + selectedID);
                System.out.println("Found " + cartIds.size() + " carts to process");
                
               
            for (String cartId : cartIds) {
                int deletedItems = em.createNativeQuery("DELETE FROM CARTITEMS WHERE CARTID = ?")
                                    .setParameter(1, cartId)
                                    .executeUpdate();

                System.out.println("Deleted " + deletedItems + " cart items for CartID: " + cartId);
            }
                }catch(Exception e){
                    System.out.print("2 "+e);
                }
            try{
            int deletedCarts = em.createNativeQuery("DELETE FROM CARTS WHERE CUSTOMERID = ?").setParameter(1, selectedID).executeUpdate();
            }catch(Exception e){
                    System.out.print("3 "+e);
            }try{
            em.createNativeQuery("DELETE FROM ADDRESSES WHERE CUSTOMERID = ?").setParameter(1, selectedID).executeUpdate();
            }catch(Exception e){
                    System.out.print("4 "+e);
                }
            try{
            Object result = em.createNativeQuery("SELECT RATINGID FROM RATING WHERE CUSTOMERID = ?").setParameter(1, selectedID).getSingleResult();
            String rateID="";
            if (result != null) {
                rateID = String.valueOf(result);
                System.out.println("Found rating ID: " + rateID);
            } 
            
            em.createNativeQuery("DELETE FROM REPLY WHERE RATINGID = ?").setParameter(1, rateID).executeUpdate();            
            }catch(Exception e){
                    System.out.print("6 "+e);
                }
            try{
            em.createNativeQuery("DELETE FROM RATING WHERE CUSTOMERID = ?").setParameter(1, selectedID).executeUpdate();
            }catch(Exception e){
                    System.out.print("7 "+e);
                }
            try{
            em.createNativeQuery("DELETE FROM CUSTOMER WHERE CUSTOMERID = ?").setParameter(1, selectedID).executeUpdate();
            }catch(Exception e){
                    System.out.print("8 "+e);
                }
                System.out.print("cus not null level 3");
                try{
                em.remove(customer);
                }catch(Exception e){
                    System.out.print("9 "+e);
                }
                request.setAttribute("message", "Customer deleted successfully.");
            } else {
                System.out.print("cus level 3");
                request.setAttribute("message", "Customer not found.");
            }
            System.out.print("level 3");
            utx.commit();
            TypedQuery<Customer> query = em.createNamedQuery("Customer.findAll", Customer.class);
            List<Customer> customers = query.getResultList();

            HttpSession session = request.getSession();
            session.setAttribute("customers", customers);
            break;
            default:
                request.setAttribute("message", "Unknown action.");
        }
        } catch (Exception e) {
            try {
                utx.rollback(); 
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            request.setAttribute("message", "Error: " + e.getMessage());
        }
        request.getRequestDispatcher("admCreateUser.jsp").forward(request, response);
    }
}