package Controller;

import Entity.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "updateProduct", urlPatterns = {"/updateProduct"})
@MultipartConfig(
  fileSizeThreshold = 1024 * 1024 * 1,
  maxFileSize = 1024 * 1024 * 10,
  maxRequestSize = 1024 * 1024 * 100
)
public class UpdateProduct extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productID = request.getParameter("productID");
        String roomName = request.getParameter("roomName");
        String productName = request.getParameter("productName");
        String productDecs = request.getParameter("productDecs");
        int productPrice = Integer.parseInt(request.getParameter("productPrice"));
        String productLongDesc = request.getParameter("productLongDesc");
        String productImg = request.getParameter("productImg");
        
        String roomID = "";
        String tableName = "ROOMCATEGORY";
        DBConnection connection = new DBConnection();
        connection.initializeJdbc();
        ArrayList<Object[]> roomRows = connection.getRows(tableName);
        for(int i = 0; i < roomRows.size() ; i++){
            if(roomRows.get(i)[1].equals(roomName)){
                roomID = String.valueOf(roomRows.get(i)[0]);
                break;
            }
        }
        
        ProductDA update = new ProductDA();
        boolean checkUpdate = update.updateProduct(productName,productPrice,productDecs,productImg,productLongDesc,roomID,productID);
        if(checkUpdate){
            request.getRequestDispatcher("editProduct.jsp").forward(request, response);
        }else{
            response.getWriter().print("The product uploaded failed.");
        }
    }
}
