
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

```html
        <form class="d-flex" role="search" method="post" action="${pageContext.request.contextPath}/BeerSearchServlet">
          <input class="form-control me-2" type="search" name="search" placeholder="Search" aria-label="Search" value="<%= search%>">
          <button class="btn btn-outline-success" type="submit">Search</button>
        </form>
```

As you can see, the value that you write into the search input text is identified in the request as **search**. 
Next, in the servlet we have to retrieve that value and pass it to the **BeersDAO.search** method:

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

The solution will be uploaded Nov 8th.

## Exercise 03: Adding a beer to a shopping cart

The solution will be uploaded Nov 8th.