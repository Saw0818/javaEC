package Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import Entity.*;
import java.io.*;
import java.nio.file.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;

@WebServlet(name = "AddCategory", urlPatterns = {"/AddCategory"})
@MultipartConfig(
  fileSizeThreshold = 1024 * 1024 * 1,
  maxFileSize = 1024 * 1024 * 10,
  maxRequestSize = 1024 * 1024 * 100
)
public class AddCategory extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DBConnection connection = new DBConnection();
        connection.initializeJdbc();
        String roomID = connection.generateID("ROOMCATEGORY","R",3,1);
        String roomName = request.getParameter("roomName");
        String roomCategory = request.getParameter("roomDecs");
        
        Part filePart = request.getPart("imageFile");
        Path nowPath = null;
        String fileName = "";
        boolean error = false;
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
        }
        
        if(roomID == null || roomName == null || roomCategory == null){
            error = true;
        }
        
        if(!error){
            String roomImage = "image/" + fileName;
            RoomCategoryDA add = new RoomCategoryDA();
            boolean check = add.addCategory(roomID,roomName,roomCategory,roomImage);
            if(check){
                request.getRequestDispatcher("editProduct.jsp").forward(request, response);
            }else{
                response.getWriter().print("Error");
            }
        }else{
            String errorMessage = "Please fill in all the information!";
            request.setAttribute("errorMessage",errorMessage);
            request.getRequestDispatcher("AddCategory.jsp").forward(request, response);
        }
        
    }
}
