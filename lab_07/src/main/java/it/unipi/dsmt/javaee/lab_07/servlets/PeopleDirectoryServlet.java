package it.unipi.dsmt.javaee.lab_07.servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "PeopleDirectoryServlet", value = "/PeopleDirectoryServlet")
public class PeopleDirectoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("username");
        String targetJSP = null;
        if (username == null){
            targetJSP = "/WEB-INF/jsp/register-person.jsp";
        } else {
            targetJSP = "/WEB-INF/jsp/people-directory.jsp";
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetJSP);
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String userNameParam = request.getParameter("username");
        session.setAttribute("username", userNameParam);
        ServletContext servletContext = request.getServletContext();
        List<String> peopleDirectory = (List<String>) servletContext.getAttribute("peopleDirectory");
        peopleDirectory.add(userNameParam);
//        servletContext.setAttribute("peopleDirectory", peopleDirectory);
//        String targetJSP = "/WEB-INF/jsp/people-directory.jsp";
//        RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetJSP);
//        requestDispatcher.forward(request, response);
        String target = request.getContextPath() + "/PeopleDirectoryServlet";
        response.sendRedirect(target);
    }
}
