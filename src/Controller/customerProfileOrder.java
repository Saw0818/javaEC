package Controller;

import Entity.*;
import javax.persistence.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import javax.annotation.*;
import javax.transaction.UserTransaction;
import java.util.*;

/**
 *
 * @author User
 */
@WebServlet("/customerProfileOrder")
public class customerProfileOrder extends HttpServlet {
    @PersistenceContext
    EntityManager em;
    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String orderId = request.getParameter("order");

    Order order = em.find(Order.class, orderId);
    if (order == null) {
        request.setAttribute("error", "Order not found.");
        request.getRequestDispatcher("customerProfileOrder.jsp").forward(request, response);
        return;
    }

    String cartId = order.getCart().getCartID();

    // Fetch all items for this cart
    List<CartItem> items = em.createQuery(
        "SELECT c FROM Cartitems c WHERE c.cartid.cartid = :cartId", CartItem.class)
        .setParameter("cartId", cartId)
        .getResultList();

    // Optional: create a structure to send only needed info
    List<Map<String, Object>> orderDetails = new ArrayList<>();

    for (CartItem item : items) {
        Map<String, Object> map = new HashMap<>();
        map.put("productName", item.getProduct().getProductname());
        map.put("quantity", item.getQuantity());
        map.put("price", item.getProduct().getProductprice()*item.getQuantity());

        orderDetails.add(map);
    }

    // Store order details in request
    request.setAttribute("orderDetails", orderDetails);
    request.setAttribute("orderId", orderId);
    request.setAttribute("orderCreateTime", order.getTimestamp());
    request.setAttribute("orderStatus", order.getStatus());
    // Forward to JSP
    request.getRequestDispatcher("customerProfileOrder.jsp").forward(request, response);
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
        return "Short description";
    }// </editor-fold>

}
