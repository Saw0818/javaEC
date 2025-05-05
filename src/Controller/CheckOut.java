package Controller;

import Entity.*;
import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException; // Added missing import for SQLException
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession; // Added missing import for HttpSession

@WebServlet(name = "CheckOut", urlPatterns = {"/CheckOut"})
public class CheckOut extends HttpServlet {
    private CartDA cartDA;
    private ProductDA prodDA;
    private CouponDA couponDA;
    
    @Override
    public void init() throws ServletException {
        cartDA = new CartDA();
        prodDA = new ProductDA();
        couponDA = new CouponDA();
    }
  
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        HttpSession session = req.getSession();
        String action = req.getParameter("action");
        Customer cust = (Customer) session.getAttribute("customer");
        
        // Check if customer exists
        if (cust == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
        
        String customerID = cust.getCustomerid();
        if (customerID == null || customerID.isEmpty()) {
            resp.sendRedirect("login.jsp");
            return;
        }
        
        System.out.print("custID ok");
        try {
            // Handle increment/decrement/remove actions
            if ("increase".equals(action) || "decrease".equals(action) || "remove".equals(action)) {
                // Your existing cart update logic
                if (action != null) {
                
                    switch(action) {
                        case "increase":
                        case "decrease":
                            processQuantityUpdate(req, customerID, action);
                            Cart updatedCart = cartDA.getUserCart(customerID);
                            double updateSubtotal = calculateSubtotal(updatedCart);
                            req.setAttribute("subtotal", updateSubtotal);
                            resp.sendRedirect("cart.jsp");
                            break;
                        case "remove":
                            processItemRemoval(req, customerID);
                            Cart removedCart = cartDA.getUserCart(customerID);
                            double removedSubtotal = calculateSubtotal(removedCart);
                            req.setAttribute("subtotal", removedSubtotal);
                            resp.sendRedirect("cart.jsp");
                            break;
                        default:
                            req.setAttribute("error", "No action specified");
                            return;
                    }
                }
                return;
            }
            System.out.print("handle actions ok");
            
            // Handle checkout action
            if ("checkout".equals(action)) {
                // Your existing checkout logic
                // Get the full cart
                Cart fullCart = cartDA.getUserCart(customerID);
                System.out.print("run full cart done!");
                // Get selected product IDs
                
                // Debug code - run this first
                System.out.println("Debug - All parameters:");
                Enumeration<String> paramNames = req.getParameterNames();
                while(paramNames.hasMoreElements()) {
                    String name = paramNames.nextElement();
                    System.out.println(name + ": " + Arrays.toString(req.getParameterValues(name)));
                }

                // Then try to get the parameter values
                String[] selectedProductIds = req.getParameterValues("selectedProductIds");
                System.out.println("Selected product IDs: " + (selectedProductIds == null ? "null" : Arrays.toString(selectedProductIds)));
                
                // Validate that at least one product is selected
                if (selectedProductIds == null || selectedProductIds.length == 0) {
                    req.setAttribute("errorMessage", "Please select at least one product to checkout");
                    req.getRequestDispatcher("cart.jsp").forward(req, resp);
                    return;
                }
                
                // Build checkout cart with selected items
                System.out.print("select prod id ok");
                Cart checkoutCart = buildCheckoutCart(fullCart, selectedProductIds, customerID);
                System.out.print("run check cart done");
                // Validate that checkout cart has items (in case of errors)
                if (checkoutCart.getItems().length == 0) {
                    req.setAttribute("errorMessage", "No valid products selected for checkout");
                    req.getRequestDispatcher("cart.jsp").forward(req, resp);
                    return;
                }
                
                double subtotal = calculateSubtotal(checkoutCart);

                String shippingMethod = "standard";
                double shippingFee = 5.0;

                String couponCode = (String) session.getAttribute("couponCode");
                CartItem[] items = checkoutCart.getItems();
                if(couponCode != null){
                Coupon coupon = couponDA.getCoupon(couponCode);
                    double discountAmount = coupon.calculateDiscount(subtotal, shippingFee);
                    double taxAmount = (subtotal - discountAmount) * 0.06;
                    double total = subtotal + taxAmount;
                    System.out.print("handle logic ok");
                    

                    // Store in request for immediate display in checkout.jsp
                    req.setAttribute("checkoutCart", checkoutCart);
                    req.setAttribute("checkoutItems", items);
                    req.setAttribute("itemsIDs", selectedProductIds);
                    req.setAttribute("subtotal", subtotal);
                    req.setAttribute("discountAmt", discountAmount);
                    req.setAttribute("shippingFee", shippingFee);
                    req.setAttribute("tax", taxAmount);
                    req.setAttribute("total", total);
                    req.setAttribute("appliedCoupon", coupon);
                    System.out.print("request passing ok");
                    // Store in session for CreateOrder servlet to access
                    session.setAttribute("checkoutCart", checkoutCart);
                    session.setAttribute("checkoutItems", items);
                    session.setAttribute("checkoutItemsIDs", selectedProductIds);
                    session.setAttribute("checkoutSubtotal", subtotal);
                    session.setAttribute("checkoutDiscountAmt", discountAmount);
                    session.setAttribute("shipMethod", shippingMethod);
                    session.setAttribute("checkoutShippingFee", shippingFee);
                    session.setAttribute("checkoutTax", taxAmount);
                    session.setAttribute("checkoutTotal", total);
                    System.out.print("session passing ok");
                }else{
                    Coupon coupon = new Coupon();
                    double discountAmount = 0.0;
                    double taxAmount = (subtotal) * 0.06;
                    double total = subtotal + taxAmount;
                    System.out.print("handle logic ok");

                    // Store in request for immediate display in checkout.jsp
                    req.setAttribute("checkoutCart", checkoutCart);
                    req.setAttribute("checkoutItems", items);
                    req.setAttribute("itemsIDs", selectedProductIds);
                    req.setAttribute("subtotal", subtotal);
                    req.setAttribute("discountAmt", discountAmount);
                    req.setAttribute("shippingFee", shippingFee);
                    req.setAttribute("tax", taxAmount);
                    req.setAttribute("total", total);
                    req.setAttribute("appliedCoupon", coupon);
                    System.out.print("request passing ok");
                    // Store in session for CreateOrder servlet to access
                    session.setAttribute("checkoutCart", checkoutCart);
                    session.setAttribute("checkoutItems", items);
                    session.setAttribute("checkoutItemsIDs", selectedProductIds);
                    session.setAttribute("checkoutSubtotal", subtotal);
                    session.setAttribute("checkoutDiscountAmt", discountAmount);
                    session.setAttribute("shipMethod", shippingMethod);
                    session.setAttribute("checkoutShippingFee", shippingFee);
                    session.setAttribute("checkoutTax", taxAmount);
                    session.setAttribute("checkoutTotal", total);
                    System.out.print("session Attribute done!");
                    System.out.print(items);
                    System.out.print(subtotal);
                    System.out.print(shippingFee);
                    System.out.print(shippingMethod);
                    System.out.print(taxAmount);
                    System.out.print(total);
                    System.out.print(checkoutCart);
                    System.out.print("session passing ok");
                }

                // Note: appliedCoupon and shippingMethod are already in session
                // Forward to checkout page
                req.getRequestDispatcher("checkout.jsp").forward(req, resp);
            }
                
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
    // Build the checkout cart with selected products
    private Cart buildCheckoutCart(Cart fullCart, String[] selectedProductIds, String custID) throws SQLException {
        Cart checkoutCart = new Cart();
        String cartID = cartDA.createCart(custID);
        checkoutCart.setCartID(cartID); 
        if (selectedProductIds != null) {
            
            // Create a set of selected product IDs for easier lookup
            Set<String> selectedPidSet = new HashSet<>(Arrays.asList(selectedProductIds));

            // Get all items from the full cart
            CartItem[] allItems = fullCart.getItems();

            // Temporary list to hold items that will remain in the full cart
            List<CartItem> remainingItems = new ArrayList<>();

            // Process each item
            for (CartItem item : allItems) {
                String pid = item.getProduct().getProductid();
                int quantity = item.getQuantity();

                if (selectedPidSet.contains(pid)) {
                    // Add to checkout cart
                    checkoutCart.addItem(item.getProduct(), quantity);
                } else {
                    // Keep in the full cart
                    remainingItems.add(item);
                }
            }

            // Clear the full cart
            fullCart.clearCart();

            // Add back the remaining items
            for (CartItem item : remainingItems) {
                fullCart.addItem(item.getProduct(), item.getQuantity());
            }
        }

        return checkoutCart;
    }

    // Calculate the subtotal based on the checkout cart
    private double calculateSubtotal(Cart cart) {
        double subtotal = 0;
        for (CartItem item : cart.getItems()) {
            subtotal += item.getProduct().getProductprice() * item.getQuantity();
        }
        return subtotal;
    }


    // Calculate the shipping fee based on the method selected
    private double calculateShippingFee(String method) {
        return "express".equalsIgnoreCase(method) ? 10.0 : 5.0;
    }


    // Calculate the discount based on the coupon and subtotal
    private double calculateDiscount(double subtotal, Coupon coupon, double shippingFee) {
        if (coupon != null && coupon.isValid()) {
            if (coupon.getDiscountType().equals("FREESHIPPING")) {
                return shippingFee; // Shipping fee is already handled separately
            }
            return coupon.calculateDiscount(subtotal, shippingFee); // return discount amount(eg 10% of total)
        }
        return 0;
    }

    
    // Process adding/removing quantities in the cart
    private void processQuantityUpdate(HttpServletRequest req, String userId, String action) throws SQLException {
        String pid = req.getParameter("productID");
        
        if (pid == null || pid.isEmpty()) {
            throw new IllegalArgumentException("Product ID is missing");
        }
        
        System.out.println("Processing " + action + " for product: " + pid);
        
        Cart cart = cartDA.getUserCart(userId);
        int currentQuantity = cart.getQuantity(pid);
        
        System.out.println("Current quantity: " + currentQuantity);
        
        if ("increase".equals(action)) {
            cartDA.updateQuantity(userId, pid, currentQuantity + 1);
            System.out.println("Increased quantity to " + (currentQuantity + 1));
        } else if ("decrease".equals(action)) {
            if (currentQuantity > 1) {
                cartDA.updateQuantity(userId, pid, currentQuantity - 1);
                System.out.println("Decreased quantity to " + (currentQuantity - 1));
            } else {
                cartDA.removeFromCart(userId, pid);
                System.out.println("Removed item from cart (quantity was 1)");
            }
        }
    }
    
    // Process item removal from the cart
    private void processItemRemoval(HttpServletRequest req, String userId) throws SQLException {
        String pid = req.getParameter("productId");
        
        if (pid == null || pid.isEmpty()) {
            throw new IllegalArgumentException("Product ID is missing");
        }
        
        System.out.println("Removing product: " + pid);
        cartDA.removeFromCart(userId, pid);
    }
    
}