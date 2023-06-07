package it.unipi.dsmt.javaee.lab_10.servlets;

import it.unipi.dsmt.jakartaee.lab_10_ejb_interfaces.dto.BeerDTO;
import it.unipi.dsmt.jakartaee.lab_10_ejb_interfaces.interfaces.ShoppingCartEJB;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

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
            case "checkout":
                checkout(request, response);
                break;
        }
    }

    private void checkout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String productId = request.getParameter("productId");
        String productName = request.getParameter("productName");
        ShoppingCartEJB shoppingCartEJB = retrieveShoppingCartEJB(request);
        String orderId = shoppingCartEJB.checkout();
        response.getWriter().write(orderId + "");
        response.getWriter().flush();
        response.getWriter().close();
    }

    private void addProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String productId = request.getParameter("productId");
        String productName = request.getParameter("productName");
        ShoppingCartEJB shoppingCartEJB = retrieveShoppingCartEJB(request);
        shoppingCartEJB.addProduct(productId, productName);
        int numberOfProducts = shoppingCartEJB.getTotalNumberProducts();
        response.getWriter().write(numberOfProducts + "");
        response.getWriter().flush();
        response.getWriter().close();
    }

    private void processView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String targetResource = "/WEB-INF/jsp/shopping_cart.jsp";
        ShoppingCartEJB shoppingCartEJB = retrieveShoppingCartEJB(request);
        int numberOfProducts = shoppingCartEJB.getTotalNumberProducts();
        List<BeerDTO> products = shoppingCartEJB.getBeerDTOList();
        request.setAttribute("numberProducts", numberOfProducts);
        request.setAttribute("products", products);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetResource);
        requestDispatcher.forward(request, response);
    }

    private ShoppingCartEJB retrieveShoppingCartEJB(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        ShoppingCartEJB shoppingCartEJB = (ShoppingCartEJB) session.getAttribute("shoppingCartEJB");
        if (shoppingCartEJB == null) {
            Properties props = new Properties();
            InitialContext ic = null;
            try {
                ic = new InitialContext(props);
                String id = "java:global/lab_10_ejb_producer/ShoppingCartEJBImpl!" + ShoppingCartEJB.class.getName();
                System.out.println("Id: " + id);
                shoppingCartEJB = (ShoppingCartEJB)ic.lookup(id);
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
            session.setAttribute("shoppingCartEJB", shoppingCartEJB);
        }
        return shoppingCartEJB;
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
