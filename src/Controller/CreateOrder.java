package Controller;

import Entity.*;
import java.util.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/CreateOrder")
public class CreateOrder extends HttpServlet {
    private CartDA cartDA;
    private OrderDA orderDA;
    private CouponDA couponDA;
    private AddressDA addressDA;
    private PaymentMethodDA paymentMethodDA;
    
    @Override
    public void init() throws ServletException {
        cartDA = new CartDA();
        orderDA = new OrderDA();
        couponDA = new CouponDA();
        addressDA = new AddressDA();
        paymentMethodDA = new PaymentMethodDA();
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
        System.out.print("no cust login");
        String customerId = cust.getCustomerid();
        String street = req.getParameter("street");
        String city = req.getParameter("city");
        String state = req.getParameter("state");
        String postcode = req.getParameter("postcode");
        String paymentMethodId = req.getParameter("paymentMethodId");
        String shipId = "SHP001";
        Cart checkoutCart = (Cart) session.getAttribute("checkoutCart");
        System.out.print("request Attribute done!");
        try {
            
        List<String> errors = new ArrayList<>();
        if (street == null || street.trim().isEmpty()) {
            errors.add("Street is required.");
        }
        if (city == null || city.trim().isEmpty()) {
                errors.add("city is required.");
        }
        if (state == null || state.trim().isEmpty()) {
                errors.add("state is required.");
        }
        if (postcode == null || postcode.trim().isEmpty()) {
                errors.add("postcode is required.");
        }
        if (paymentMethodId == null || paymentMethodId.trim().isEmpty()) {
                errors.add("Payment Method is required.");
        }
            // Retrieve checkout data from session (stored by CheckOut servlet)
            CartItem[] items = (CartItem[]) session.getAttribute("checkoutItems");
            Double subtotal = (Double) session.getAttribute("checkoutSubtotal");
            Double discountAmt = (Double) session.getAttribute("checkoutDiscountAmt");
            Double shippingFee = (Double) session.getAttribute("checkoutShippingFee");
            Double tax = (Double) session.getAttribute("checkoutTax");
            Double total = (Double) session.getAttribute("checkoutTotal");
            Coupon coupon = (Coupon) session.getAttribute("appliedCoupon");
            String shipMethod = (String) session.getAttribute("shipMethod");
            
            System.out.print("session Attribute done!");
            System.out.print(items);
            System.out.print(subtotal);
            System.out.print(shippingFee);
            System.out.print(shipMethod);
            System.out.print(tax);
            System.out.print(total);
            System.out.print(checkoutCart);
            
            // Validate required session attributes
            if (items == null || subtotal == null || shippingFee == null || tax == null || total == null || shipMethod == null) {
                req.setAttribute("errorMessage", "Missing required checkout information. Please try again.");
                req.getRequestDispatcher("cart.jsp").forward(req, resp);
                return;
            }
            
            if(!errors.isEmpty()){   
                    req.setAttribute("errorMessage", String.join(" ", errors));
                    req.getRequestDispatcher("checkout.jsp").forward(req, resp);
            }
            System.out.print("Validation done");
            // Get the user's current cart
            if (checkoutCart == null || checkoutCart.getItems().length == 0) {
                throw new ValidationException("Your cart is empty. Please add items before checking out.");
            }
            // Create shipping object
            Shipping shipping = new Shipping();
            //shipping.setShippingId(shipId);
            if(shipMethod.equals("express")){
                shippingFee = 10.0;
                shipId = "SHP002";
                //shipping.setShippingId(shipId);
            }
            shipping.setMethod(shipMethod);
            shipping.setCost(shippingFee);
            shipping.setEstimatedDays("express".equalsIgnoreCase(shipMethod) ? 1 : 3);
            System.out.print("Shipping object done");
            // Create address object and save to get addressId
            Address address = new Address(street, city, state, postcode);
            String addressId = addressDA.saveAddress(address, customerId);
            address.setAddressId(addressId);
            System.out.print("data handle done!");
            
            // Build order object
            Order order = new Order();
            order.setCustomerId(customerId);
            order.setCart(checkoutCart);
            order.setDiscount(coupon);
            order.setDiscountAmt(discountAmt);
            order.setShipping(shipping);
            
            order.setShippingAddress(address);
            order.setTimestamp(LocalDateTime.now());
            order.setTax(tax);
            order.setTotal(total);
            order.setStatus("PENDING");
            order.setPaymentMethodId(paymentMethodId);
            
            // Update calculated fields to ensure consistency
            order.updateCalculatedFields();
            System.out.print("Build and set object done!");
            // Save the order to database
            String orderId = orderDA.createOrder(order, checkoutCart, shipId);
            
            order.setOrderId(orderId);
            if (orderId == null || orderId.isEmpty()) {
                throw new SQLException("Failed to create order in database");
            }
            
            // Apply coupon if applicable
            if (coupon != null) {
                couponDA.markCouponAsUsed(coupon.getCouponCode(), customerId);
            }
            
            System.out.print("Save to database done!");
            System.out.print("Run redirect now!");
            // Redirect to order confirmation page
            if(paymentMethodId.equals("PMT001") || paymentMethodId.equals("PMT002") || paymentMethodId.equals("PMT003")) {
                // Clean up the session attributes before redirecting
                session.setAttribute("order", order);
                cleanupSessionAttributes(session);
                resp.sendRedirect("orderConfirmation.jsp");
            }
            
        } catch (ValidationException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("checkout.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("errorMessage", "Database error occurred: " + e.getMessage());
            req.getRequestDispatcher("checkout.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            req.getRequestDispatcher("checkout.jsp").forward(req, resp);
            e.printStackTrace();
        }
    }
    
    
    /**
     * Helper method to clean up session attributes after order creation
     */
    private void cleanupSessionAttributes(HttpSession session) {
        session.removeAttribute("checkoutItems");
        session.removeAttribute("checkoutSubtotal");
        session.removeAttribute("checkoutDiscountAmt");
        session.removeAttribute("checkoutShippingFee");
        session.removeAttribute("checkoutTax");
        session.removeAttribute("checkoutTotal");
        session.removeAttribute("appliedCoupon");
        session.removeAttribute("shippingMethod");
    }
    
    /**
     * Custom exception for validation errors
     */
    private class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }
}