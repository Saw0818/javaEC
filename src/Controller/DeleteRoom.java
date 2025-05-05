package Controller;

import Entity.*;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DeleteRoom", urlPatterns = {"/DeleteRoom"})
public class DeleteRoom extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String roomID = request.getParameter("roomID");
        
        response.getWriter().print(roomID);
        DBConnection connection = new DBConnection();
        connection.initializeJdbc();
        RoomCategoryDA delete = new RoomCategoryDA();
        
        boolean checkDeleteProduct = delete.deleteAllProduct(roomID);
        boolean check = delete.deleteCategory(roomID);
        if(check && checkDeleteProduct){
            request.getRequestDispatcher("editProduct.jsp").forward(request, response);
        }else{
            response.getWriter().print("Delete Fail");
        }
    }

}
