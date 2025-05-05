package Controller;

import Entity.*;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "AddToCart", urlPatterns = {"/AddToCart"})
public class AddToCart extends HttpServlet {

    private ProductDA prodDA;
    private CartDA cartDA;

    @Override
    public void init() throws ServletException {
        prodDA = new ProductDA();
        cartDA = new CartDA();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
    HttpSession session = request.getSession();
    Customer cust = (Customer) session.getAttribute("customer");
    
    // Validate if user is logged in
    if (cust == null) {
        request.setAttribute("errorMessage", "Please log in to add items to cart");
        request.getRequestDispatcher("login.jsp").forward(request, response);
        return;
    }
    
    String customerID = cust.getCustomerid();
    String prodID = request.getParameter("productID");
    String quantityParam = request.getParameter("quantity");
    try {
        // Parse quantity with default value of 1
        int quantity = 1;
        if (quantityParam != null && !quantityParam.trim().isEmpty()) {
            quantity = Integer.parseInt(quantityParam);
            if (quantity <= 0) {
                handleError(request, response, "Quantity must be greater than zero", "productDetail.jsp?productID=" + prodID);
                return;
            }
        }
        // Add to cart
        cartDA.addToCart(customerID, prodID, quantity);
        if (prodID == null || prodID.trim().isEmpty()) {
            handleError(request, response, "Invalid product ID", "products.jsp"); // or homepage
            return;
        }
        // Retrieve updated cart
        Cart cart = cartDA.getUserCart(customerID);
        // Calculate subtotal
        double subtotal = cart.calculateSubtotal();
        
        // Set attributes and forward to cart page
        request.setAttribute("subtotal", subtotal);
        request.setAttribute("cart", cart);
        request.getRequestDispatcher("cart.jsp").forward(request, response);
        
    } catch (NumberFormatException e) {
        handleError(request, response, "Invalid quantity format", "productDetail.jsp?productID=" + prodID);
    } catch (Exception e) {
        // Log the exception
        handleError(request, response, "Error adding item to cart: " + e.getMessage(), "cart.jsp");
    }
}

// Helper method to handle errors
private void handleError(HttpServletRequest request, HttpServletResponse response, 
                        String errorMessage, String destination) 
                        throws ServletException, IOException {
    request.setAttribute("errorMessage", errorMessage);
    request.getRequestDispatcher(destination).forward(request, response);
}

}
