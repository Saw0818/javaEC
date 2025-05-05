package Controller;

import java.io.*;
import Entity.*;
import javax.servlet.annotation.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.nio.file.*;
import java.util.*;

@WebServlet(name = "AddProduct", urlPatterns = {"/AddProduct"})
@MultipartConfig(
  fileSizeThreshold = 1024 * 1024 * 1,
  maxFileSize = 1024 * 1024 * 10,
  maxRequestSize = 1024 * 1024 * 100
)
public class AddProduct extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    boolean error = false;
    System.out.print("Start testing form here");
        
    String productName = request.getParameter("productName");
    String productDecs = request.getParameter("productDecs");
    String productPrice = request.getParameter("productPrice");
    String productLongDesc = request.getParameter("productLongDesc");
    String roomName = request.getParameter("roomName");
    Path nowPath = null;
    String fileName = "";
    
    boolean checkPriceInt = true;
    try{
        int price = Integer.parseInt(productPrice);
    }catch(Exception ex){
        checkPriceInt = false;
    }
    
    Part filePart = request.getPart("imageFile");
    if (filePart != null && filePart.getSize() > 0) {
        fileName = filePart.getSubmittedFileName();

            String uploadPath = getServletContext().getRealPath("");
            Path currentPath = Paths.get(uploadPath).getParent().getParent();
            String stringPath = currentPath + File.separator + "web" + File.separator + "image";
            nowPath = Paths.get(stringPath, fileName);
            File uploadDir = new File(stringPath);
            if (!uploadDir.exists()){
                uploadDir.mkdirs();
            }

            try (InputStream fileContent = filePart.getInputStream()) {
                Files.copy(fileContent,nowPath,StandardCopyOption.REPLACE_EXISTING);
            }catch(Exception ex){
                ex.printStackTrace();
                response.getWriter().print("The image upload fail.");
            }
    }else{
        error = true;
        response.getWriter().print("No Image.");
    }
    
    if(productName == null || productDecs == null || productPrice == null || productLongDesc == null){
        error = true;
    }
    
    if(!error && checkPriceInt){
        DBConnection connection = new DBConnection();
        connection.initializeJdbc();
        String tableName = "ROOMCATEGORY";
        ArrayList<Object[]> getRoomID = connection.getRows(tableName);
        String roomID = "";
        for(int i = 0 ; i < getRoomID.size() ; i++){
            if(getRoomID.get(i)[1].equals(roomName)){
                roomID = String.valueOf(getRoomID.get(i)[0]);
            }
        }
        
        tableName = "PRODUCT";
        String productImage = "image/" + fileName;
        ProductDA create = new ProductDA();
        String productID = connection.generateID(tableName,"P",6,1);
        int price = Integer.parseInt(productPrice);
        boolean check = create.createProduct(productID,productName,price,productDecs,productImage,productLongDesc,roomID);
        if(check){
            request.getRequestDispatcher("editProduct.jsp").forward(request, response);
        }else{
            response.getWriter().print("Insert Product Error");
        }
    }else{
        String errorMessage = "";
        if(error){
            errorMessage = "Please fill in all the information!";
        }else{
            errorMessage = "Please enter the price in a number without decimal point";
        }
        
        request.setAttribute("errorMessage",errorMessage);
        request.getRequestDispatcher("AddProduct.jsp").forward(request, response);
    }
    
    }
}
