package it.unipi.dsmt.jakartaee.lab_09.servlets;

import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.interfaces.QuotesEJB;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Properties;

@WebServlet(name = "FortuneCookieServlet", value = "/FortuneCookieServlet")
public class FortuneCookieServlet extends HttpServlet {
    public QuotesEJB getQuotesEJB() {
        Properties props = new Properties();
//        props.setProperty("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
//        props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
        InitialContext ic = null;
        try {
            ic = new InitialContext(props);
            String id = "java:global/lab_09_ejb/QuotesEJBImpl!" + QuotesEJB.class.getName();
            System.out.println("Id: " + id);
            return (QuotesEJB)ic.lookup(id);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String randomQuote = getQuotesEJB().pickOneQuote();
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
