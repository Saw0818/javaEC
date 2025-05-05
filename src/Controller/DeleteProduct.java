package Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import Entity.*;
import java.io.*;
import javax.servlet.http.*;

@WebServlet(name = "DeleteProduct", urlPatterns = {"/DeleteProduct"})
public class DeleteProduct extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productID = request.getParameter("productID");
        
        DBConnection connection = new DBConnection();
        connection.initializeJdbc();
        ProductDA delete = new ProductDA();
        boolean check = delete.deleteProduct(productID);
        if(check){
            request.getRequestDispatcher("editProduct.jsp").forward(request, response);
        }else{
            response.getWriter().print("Delete Fail");
        }
    }
}
