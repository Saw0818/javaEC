package Controller;

import Entity.*;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/CartController")
public class CartController extends HttpServlet {
    private CartDA cartDA = new CartDA();
    private CouponDA couponDA = new CouponDA();
    
    @Override
    public void init() throws ServletException {
        // Already instantiated in class definition
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        HttpSession session = req.getSession();
        Customer cust = (Customer) session.getAttribute("customer");
        
        // Check if customer exists
        if (cust == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
        
        String customerID = cust.getCustomerid();
        
        try {
            // Get cart from database
            Cart cart = cartDA.getUserCart(customerID);
            
            // Apply coupon from session if exists
            String couponCode = (String) session.getAttribute("couponCode");
            Coupon coupon = couponDA.getCoupon(couponCode);
            if(coupon == null){
                session.removeAttribute("couponCode");
            }
            
            
            // Also store in session for other pages
            session.setAttribute("cart", cart);
            // Forward to cart.jsp
            req.getRequestDispatcher("cart.jsp").forward(req, resp);
            
        } catch (SQLException ex) {
            handleError(req, resp, "Error adding item to cart: " + ex.getMessage(), "cart.jsp");
        }
    }
    
    private void handleError(HttpServletRequest request, HttpServletResponse response, 
                            String errorMessage, String destination) 
                            throws ServletException, IOException {
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher(destination).forward(request, response);
    }
    
    // Handle POST requests (for consistency, though we use UpdateCart for most operations)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
    private double calculateSubtotal(CartItem[] items) {
        double subtotal = 0.0;
        for (CartItem item : items) {
            subtotal += item.getProduct().getProductprice() * item.getQuantity();
        }
        return subtotal;
    }
}