package it.unipi.dsmt.javaee.lab_07.servlets;

import it.unipi.dsmt.javaee.lab_07.dao.BeersDAO;
import it.unipi.dsmt.javaee.lab_07.dto.BeerDTO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "BeerSearchServlet", value = "/BeerSearchServlet")
public class BeerSearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<BeerDTO> beerDTOList = BeersDAO.search("");
        request.setAttribute("beers", beerDTOList);
        String targetJSP = "/WEB-INF/jsp/search.jsp";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetJSP);
        requestDispatcher.forward(request, response);
    }

}
