package Controller;

import Entity.*;
import java.io.*;
import java.nio.file.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "UpdateRoom", urlPatterns = {"/UpdateRoom"})
@MultipartConfig(
  fileSizeThreshold = 1024 * 1024 * 1,
  maxFileSize = 1024 * 1024 * 10,
  maxRequestSize = 1024 * 1024 * 100
)
public class UpdateRoom extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean error = false;
        
        String roomID = request.getParameter("roomID");
        String roomName = request.getParameter("roomName");
        String roomCategory = request.getParameter("roomCategory");
        String roomImage = request.getParameter("imgSrc");
        
        Part filePart = request.getPart("imageFile");
        Path nowPath = null;
        String fileName = "";
        
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
            roomImage = "image/" + fileName;
        }
        
        if(!error){
            RoomCategoryDA update = new RoomCategoryDA();
            boolean check = update.updateCategory(roomName,roomCategory,roomImage,roomID);
            if(check){
                request.getRequestDispatcher("editProduct.jsp").forward(request, response);
            }else{
                response.getWriter().print("Error");
            }
        }else{
            String errorMessage = "Please fill in all the information!";
            request.setAttribute("errorMessage",errorMessage);
            request.getRequestDispatcher("editRoomDetail.jsp").forward(request, response);
        }
    }
}
