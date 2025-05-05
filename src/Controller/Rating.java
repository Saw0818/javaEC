package Controller;

import Entity.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Rating", urlPatterns = {"/Rating"})
public class Rating extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String customerID = request.getParameter("customerID");
        String productURL = request.getParameter("productURL");
        String productID = request.getParameter("productID");
        String review = request.getParameter("getReview");
        String ratingStar = request.getParameter("rate");
        String errorMessage = "The comment or rating can't be empty!";
        System.out.print(productURL);
        if(review.equals("")|| ratingStar == null){
            request.setAttribute("ratingError", errorMessage);
            request.getRequestDispatcher(productURL).forward(request, response);
        }else{
            DBConnection rating = new DBConnection();
            rating.initializeJdbc();
            boolean update = rating.addRating(ratingStar,review, productID,customerID);
            if(update){
                request.getRequestDispatcher(productURL).forward(request, response);
            }else{
                response.getWriter().print("Fail to add Rating");
            }
        }
    }

}
