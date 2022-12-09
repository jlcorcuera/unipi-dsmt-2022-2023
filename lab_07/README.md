
# Hands-on session 7: Jakarta EE - Part 2: Servlets & JSP

In this session, the next topics are covered:

- Jakarta EE evolution
- Creating a Maven Java Web Application
- The web.xml file for Jakarta EE 9.1
- JSP - Why?
- JSP - scriptlet tag
- JSP - expression tag
- JSP - dynamic HTML generation
- Servlet & JSP

## Required software

For this session it is required to have installed:

- Java SDK 11. (*)
- Apache Maven 3.x version. (*)
- [Apache Tomcat 10.0.27](https://dlcdn.apache.org/tomcat/tomcat-10/v10.0.27/bin/apache-tomcat-10.0.27.zip) version.
- An IDE (IntelliJ, Eclipse, Netbeans)

Also, do not forget to define the following environment variables:

- `M2_HOME` -> root directory of your Maven installation.
- `JDK_HOME` -> root directory of your JDK installation.
- Update the `PATH` environment variable by adding the `bin` directories of your JDK and Maven installations.

(*) You can avoid doing all these step manually by installing
[SDKMAN](https://sdkman.io/).


## Exercise 01: Search beer filter

The solution is straightforward, when a user clicks on the Search button, a POST to the BeerSearchServlet servlet have to be performed:

**File: src/main/webapp/WEB-INF/jsp/search.jsp**
```html
<form class="d-flex" role="search" method="post" action="${pageContext.request.contextPath}/BeerSearchServlet">
  <input class="form-control me-2" type="search" name="search" placeholder="Search" aria-label="Search" value="<%= search%>">
  <button class="btn btn-outline-success" type="submit">Search</button>
</form>
```

As you can see, the value that you write into the search input text is identified in the request as **search**.
Next, in the servlet we have to retrieve that value and pass it to the **BeersDAO.search** method:

**File: src/main/java/it/unipi/dsmt/javaee/lab_07/servlets/BeerSearchServlet.java**
```java
@WebServlet(name = "BeerSearchServlet", value = "/BeerSearchServlet")
public class BeerSearchServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchFilter = request.getParameter("search") != null ? request.getParameter("search") : "";
        List<BeerDTO> beerDTOList = BeersDAO.search(searchFilter);
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
```

Pay attention to the **processRequest** method. This method is used to handle GET or POST calls.

## Exercise 02: Registering a new beer

For this exercise, it is required to design a new JSP to register the information of new beers:

**File: src/main/webapp/WEB-INF/jsp/new_beer.jsp**
```html
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>New Beer</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
</head>
<body>
  <div class="container-sm px-4">
    <!-- Content here -->
    <h1 class="display-1 mt-5">New beer form</h1>
    <form class="row g-3 needs-validation" novalidate method="post" action="${pageContext.request.contextPath}/BeerManagerServlet">
      <input type="hidden" name="action" value="create">
      <div class="mb-2">
        <label for="nameInput" class="form-label">Name</label>
        <input type="text" required class="form-control" id="nameInput" name="name">
        <div class="invalid-feedback">
          Please enter a name.
        </div>
      </div>
      <div class="mb-2 form-url">
        <label for="imageInput" class="form-label">Image URL</label>
        <input type="url" required class="form-control" id="imageInput" name="imageUrl">
        <div class="invalid-feedback">
          Please enter an image URL.
        </div>
      </div>
      <div class="mb-2 form-url">
        <label for="ratingInput" class="form-label">Rating</label>
        <input type="number" required class="form-control" id="ratingInput" name="rating">
        <div class="invalid-feedback">
          Please enter a rating.
        </div>
      </div>
      <div class="mb-2">
        <button type="submit" class="btn btn-primary">Create</button>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/BeerSearchServlet">Cancel</a>
      </div>
    </form>
  </div>
  <script type="text/javascript">
    (() => {
      'use strict'
      // Fetch all the forms we want to apply custom Bootstrap validation styles to
      const forms = document.querySelectorAll('.needs-validation')
      // Loop over them and prevent submission
      Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
          if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
          }
          form.classList.add('was-validated');
        }, false)
      })
    })()
  </script>
</body>
</html>
```

In the previous jsp file, the HTML form sends the information by using a POST method to the servlet **BeerManagerServlet**.
This servlet, receives the data of the form and stores it by invoking the method **create** of the **BeersDAO** class.

**File: src/main/java/it/unipi/dsmt/javaee/lab_07/servlets/BeerManagerServlet.java**
```java
package it.unipi.dsmt.javaee.lab_07.servlets;

import it.unipi.dsmt.javaee.lab_07.dao.BeersDAO;
import it.unipi.dsmt.javaee.lab_07.dto.BeerDTO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "BeerManagerServlet", value = "/BeerManagerServlet")
public class BeerManagerServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") != null ? request.getParameter("action") : "new";
        switch (action) {
            case "new":
                processNew(request, response);
                break;
            case "create":
                processCreate(request, response);
                break;
        }
    }

    private void processNew(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String targetResource = "/WEB-INF/jsp/new_beer.jsp";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetResource);
        requestDispatcher.forward(request, response);
    }

    private void processCreate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String imageUrl = request.getParameter("imageUrl");
        Double rating = Double.parseDouble(request.getParameter("rating"));
        BeerDTO beerDTO = new BeerDTO(name, imageUrl, rating);
        BeersDAO.create(beerDTO);
        String targetResource = "/BeerSearchServlet";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetResource);
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
```

## Exercise 03: Adding a beer to a shopping cart

In order to display add beers to a shopping cart, we have to add logic in the **Add to cart** button to store the beers into the user web session.
There are many changes to perform:

1.  Every time a user clicks on a **Add to cart** button, the information of the productId and productName are sent to the server. We are going to send this information
    by using AJAX.

**File: src/main/webapp/WEB-INF/jsp/search.jsp**

In the header section we proceed to define:
```javascript
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
  <script type="text/javascript">
    function addShoppingCart(productId, productName){
        params = {action: 'add-product', productId: productId, productName: productName};
        $.post("${pageContext.request.contextPath}/ShoppingCartServlet", params, function(result){
          $("#counter").text(result);
        }).fail(function(xhr, status, error) {
          alert('error');
        });
    }
  </script>
```

For every **Add to cart** button, we define an onclick function:
```html
    <button type="button" onclick='addShoppingCart("<%= beer.getId() %>", "<%= beer.getName() %>");' class="btn btn-sm btn-outline-secondary">Add to cart</button>
```

2. In the **ShoppingCartServlet** servlet, we define logic to add beers and to see the beers added to our shopping cart.
   Note that we have used the HttpSession to store the information of the shopping cart by creating an object of the class **ShoppingCartDTO**.

**File: src/main/java/it/unipi/dsmt/javaee/lab_07/servlets/ShoppingCartServlet.java**
```java
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
```

3. Finally, to display the beers added so far in our shopping cart, we have to check in the HttpSession object whether a shopping cart exists or not.
   The variable numberProducts is 0 when there is not shopping cart in the session or when there are no products in it.

**File: src/main/webapp/WEB-INF/jsp/shopping_cart.jsp**
```html
<%@ page import="it.unipi.dsmt.javaee.lab_07.dto.ShoppingCartDTO" %>
<%@ page import="it.unipi.dsmt.javaee.lab_07.dto.BeerDTO" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Shopping Cart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
</head>
<%
    ShoppingCartDTO shoppingCartDTO = (ShoppingCartDTO) request.getAttribute("shoppingCart");
    int numberProducts = (int) request.getAttribute("numberProducts");
%>
<body>
    <div class="container-sm px-4">
        <h4 class="d-flex justify-content-between align-items-center mt-5">
            <span class="text-primary">Your cart</span>
            <span class="badge bg-primary rounded-pill"><%= numberProducts %></span>
        </h4>
        <ul class="list-group mb-3">
            <% if (numberProducts == 0) { %>
                <li class="list-group-item d-flex justify-content-between">
                    <span>You have 0 products.</span>
                    <strong>--</strong>
                </li>
            <% } else { %>
            <%
                List<BeerDTO> products = shoppingCartDTO.getBeerDTOList();
                for(BeerDTO beerDTO: products) {
            %>
                <li class="list-group-item d-flex justify-content-between lh-sm">
                    <div>
                        <h6 class="my-0"><%= beerDTO.getName()%></h6>
                        <small class="text-muted"></small>
                    </div>
                    <span class="text-muted"><%= beerDTO.getQuantity()%></span>
                </li>
            <%  } %>
            <li class="list-group-item d-flex justify-content-between">
                <span>Total (USD)</span>
                <strong>--</strong>
            </li>
            <% } %>
        </ul>
        <div class="mb-2">
            <button type="submit" class="btn btn-primary">Checkout</button>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/BeerSearchServlet">Back</a>
        </div>
    </div>
</body>
</html>
```