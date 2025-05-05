package Controller;

import Entity.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/UpdateCart")
public class UpdateCart extends HttpServlet {
    private CartDA cartDA = new CartDA();
    private ProductDA prodDA = new ProductDA();
    private CouponDA couponDA = new CouponDA();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        // Forward to doPost to handle all cart operations
        doPost(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        Customer cust = (Customer) session.getAttribute("customer");
        
        // Check if customer exists
        if (cust == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
        
        String customerId = cust.getCustomerid();
        String couponCode = req.getParameter("couponCode");
        
        try {
            if (customerId == null) {
                resp.sendRedirect("login.jsp");
                return;
            }
            
            
            // Reload and display updated cart
            session.setAttribute("couponCode", couponCode);
            
            // Forward to cart controller
            resp.sendRedirect("CartController");
            
        } catch (Exception ex) {
            handleError(req, resp, "Error adding item to cart: " + ex.getMessage(), "cart.jsp");
        }
    }
    
        private void handleError(HttpServletRequest request, HttpServletResponse response, 
                            String errorMessage, String destination) 
                            throws ServletException, IOException {
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher(destination).forward(request, response);
    }
}