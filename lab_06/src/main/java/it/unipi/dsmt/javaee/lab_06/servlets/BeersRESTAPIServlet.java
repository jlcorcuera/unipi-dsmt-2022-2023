package it.unipi.dsmt.javaee.lab_06.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipi.dsmt.javaee.lab_06.dao.BeersDAO;
import it.unipi.dsmt.javaee.lab_06.dto.BeerDTO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "BeersRESTAPIServlet", value = "/BeersRESTAPIServlet")
public class BeersRESTAPIServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");
        ObjectMapper objectMapper = new ObjectMapper();
        List<BeerDTO> resultList = BeersDAO.search(search);
        String jsonContent = objectMapper.writeValueAsString(resultList);
        response.setContentType("application/json");
        response.getWriter().write(jsonContent);
        response.getWriter().flush();
        response.getWriter().close();
    }

}
