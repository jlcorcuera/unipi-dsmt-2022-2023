package it.unipi.dsmt.jakartaee.lab_09.servlets;

import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.dto.BeerDTO;
import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.interfaces.BeerEJB;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "BeerSearch1Servlet", value = "/BeerSearch1Servlet")
public class BeerSearch1Servlet extends HttpServlet {

    @EJB(mappedName = "BeerEJBFromJSON")
    private BeerEJB beerEJB;

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchFilter = request.getParameter("search") != null ? request.getParameter("search") : "";
        List<BeerDTO> beerDTOList = beerEJB.search(searchFilter);
        request.setAttribute("beers", beerDTOList);
        request.setAttribute("search", searchFilter);
        String targetJSP = "/WEB-INF/jsp/search.jsp";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetJSP);
        requestDispatcher.forward(request, response);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

}
