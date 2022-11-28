package it.unipi.dsmt.javaee.lab_06.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "CalculatorServlet", value = "/CalculatorServlet")
public class CalculatorServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int a = Integer.parseInt(request.getParameter("a"));
        int b = Integer.parseInt(request.getParameter("b"));
        String action = request.getParameter("action");
        double result = 0;
        switch(action){
            case "add":
                result = a + b;
                break;
            case "sub":
                result = a - b;
                break;
            case "mul":
                result = a * b;
                break;
            case "div":
                result = a * 1.0 / b;
                break;
        }
        StringBuilder html = new StringBuilder();
        html.append("<html>");
        html.append("<body>");
        html.append("<h1>");
        html.append("You sent: a = ").append(a).append(", b = ").append(b).append("<br>");
        html.append("Action: ").append(action).append("<br>");
        html.append("Result: ").append(result);
        html.append("</h1>");
        html.append("</body>");
        html.append("</html>");
        response.setContentType("text/html");
        response.getWriter().write(html.toString());
        response.getWriter().flush();
        response.getWriter().close();
    }
}
