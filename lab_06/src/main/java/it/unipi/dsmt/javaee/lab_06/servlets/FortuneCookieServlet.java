package it.unipi.dsmt.javaee.lab_06.servlets;

import it.unipi.dsmt.javaee.lab_06.dao.QuotesDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "FortuneCookieServlet", value = "/FortuneCookieServlet")
public class FortuneCookieServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String randomQuote = QuotesDAO.pickOneQuote();
        StringBuilder html = new StringBuilder();
        html.append("<html>");
        html.append("<body>");
        html.append("<h1>");
        html.append("Hi, this quote is for you: \"").append(randomQuote).append("\"");
        html.append("</h1>");
        html.append("</body>");
        html.append("</html>");
        response.setContentType("text/html");
        response.getWriter().write(html.toString());
        response.getWriter().flush();
        response.getWriter().close();
    }
}
