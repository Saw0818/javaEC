package Controller;

import Entity.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "replyReview", urlPatterns = {"/replyReview"})
public class replyReview extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    String ratingID = request.getParameter("ratingID");
    String replyURL = request.getParameter("replyURL");
    String reply = request.getParameter("replyReview");
    String staffID = request.getParameter("staffID");
    response.getWriter().print(ratingID + " | ");
    response.getWriter().print(replyURL + " | ");
    response.getWriter().print(reply);
    
    if(reply.equals("")){
        request.setAttribute("replyError", "Reply Can't be empty");
        request.getRequestDispatcher(replyURL).forward(request, response);
    }else{
        DBConnection rating = new DBConnection();
        rating.initializeJdbc();
        boolean update = rating.addReply(reply,ratingID,staffID);
        if(update){
            request.getRequestDispatcher("replyReview.jsp").forward(request,response);
        }else{
            response.getWriter().print("Fail to add Reply");
        }
        
    }
    
    }

}
