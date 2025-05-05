package Controller;

import Entity.*;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/admin/CreateCoupons")
public class CreateCoupons extends HttpServlet {
    private CouponDA couponDA;
    
    @Override
    public void init() throws ServletException {
        couponDA = new CouponDA();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        // Check if user is admin
        HttpSession session = req.getSession();
        // Assuming there's an admin attribute in the session
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (isAdmin == null || !isAdmin) {
            resp.sendRedirect("../login.jsp");
            return;
        }
        
        req.getRequestDispatcher("createCoupons.jsp").forward(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        // Check if user is admin
        HttpSession session = req.getSession();
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (isAdmin == null || !isAdmin) {
            resp.sendRedirect("../login.jsp");
            return;
        }
        
        String action = req.getParameter("action");
        
        try {
            if ("createForAll".equals(action)) {
                // Create coupons for all customers
                couponDA.createCouponsForAllCustomers();
                req.setAttribute("successMessage", "Coupons created successfully for all customers");
            } else if ("createSingle".equals(action)) {
                // Create a single coupon for a specific customer
                String customerId = req.getParameter("customerId");
                String couponType = req.getParameter("couponType");
                
                if (customerId == null || customerId.trim().isEmpty()) {
                    throw new Exception("Customer ID is required");
                }
                
                if (couponType == null || couponType.trim().isEmpty()) {
                    throw new Exception("Coupon type is required");
                }
                
                Coupon coupon;
                switch (couponType) {
                    case "BIRTHDAY":
                        coupon = couponDA.createBirthdayCoupon(customerId);
                        break;
                    case "FIXED":
                        coupon = couponDA.createFixedCoupon(customerId);
                        break;
                    case "FREESHIPPING":
                        coupon = couponDA.createFreeShippingCoupon(customerId);
                        break;
                    default:
                        throw new Exception("Invalid coupon type");
                }
                
                req.setAttribute("successMessage", "Coupon " + coupon.getCouponCode() + 
                                " created successfully for customer " + customerId);
            } else if ("createGeneral".equals(action)) {
                // Create general coupons not tied to specific customers
                String couponType = req.getParameter("couponType");
                int count = Integer.parseInt(req.getParameter("count"));
                
                if (couponType == null || couponType.trim().isEmpty()) {
                    throw new Exception("Coupon type is required");
                }
                
                if (count <= 0 || count > 100) {
                    count = 10; // Default to 10 if invalid count
                }
                
                for (int i = 0; i < count; i++) {
                    switch (couponType) {
                        case "FIXED":
                            couponDA.createFixedCoupon(null);
                            break;
                        case "FREESHIPPING":
                            couponDA.createFreeShippingCoupon(null);
                            break;
                        default:
                            throw new Exception("Invalid coupon type for general coupons");
                    }
                }
                
                req.setAttribute("successMessage", count + " general " + couponType + " coupons created successfully");
            } else {
                throw new Exception("Invalid action");
            }
        } catch (SQLException e) {
            req.setAttribute("errorMessage", "Database error: " + e.getMessage());
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error: " + e.getMessage());
        }
        
        req.getRequestDispatcher("createCoupons.jsp").forward(req, resp);
    }
}