package it.unipi.dsmt.javaee.lab_07.servlets;

import it.unipi.dsmt.javaee.lab_07.dto.ShoppingCartDTO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "ShoppingCartServlet", value = "/ShoppingCartServlet")
public class ShoppingCartServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") != null ? request.getParameter("action") : "view";
        switch (action) {
            case "view":
                processView(request, response);
                break;
            case "add-product":
                addProduct(request, response);
                break;
        }
    }

    private void addProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String productId = request.getParameter("productId");
        String productName = request.getParameter("productName");
        HttpSession session = request.getSession(true);
        ShoppingCartDTO shoppingCartDTO = (ShoppingCartDTO) session.getAttribute("shoppingCart");
        if (shoppingCartDTO == null) {
            shoppingCartDTO = new ShoppingCartDTO();
        }
        shoppingCartDTO.addProduct(productId, productName);
        int numberOfProducts = shoppingCartDTO.getTotalNumberProducts();
        session.setAttribute("shoppingCart", shoppingCartDTO);
        response.getWriter().write(numberOfProducts + "");
        response.getWriter().flush();
        response.getWriter().close();
    }

    private void processView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String targetResource = "/WEB-INF/jsp/shopping_cart.jsp";
        HttpSession session = request.getSession(true);
        ShoppingCartDTO shoppingCartDTO = (ShoppingCartDTO) session.getAttribute("shoppingCart");
        int numberProducts = 0;
        if (shoppingCartDTO != null) {
            numberProducts = shoppingCartDTO.getTotalNumberProducts();
        }
        request.setAttribute("numberProducts", numberProducts);
        request.setAttribute("shoppingCart", shoppingCartDTO);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetResource);
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
