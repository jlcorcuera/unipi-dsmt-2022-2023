
# Hands-on session 6: Java EE - Part 1: Servlets


In this session, the next topics are covered:

- Jakarta EE evolution
- Creating our first Maven Java Web Application
- Running our first Maven Java Web Application
- Structure of a Maven Java Web Application
- The web.xml file
- ServletContextListener interface
- Servlets


## Required software

For this session it is required to have installed:

- Java SDK 11. (*)
- Apache Maven 3.x version. (*)
- Apache Tomcat 9.x version.
- An IDE (IntelliJ, Eclipse, Netbeans)

Also, do not forget to define the following environment variables:

- `M2_HOME` -> root directory of your Maven installation.
- `JDK_HOME` -> root directory of your JDK installation.
- Update the `PATH` environment variable by adding the `bin` directories of your JDK and Maven installations.

(*) You can avoid doing all these step manually by installing
[SDKMAN](https://sdkman.io/).


## Exercise 01: The Calculator

The idea here is to make use of Javascript to set the value of the hidden input text. Once this value is set, a submit of this form is perform.

```html
<html>
<script>
    function call_post(action) {
        alert('Action: ' + action);
        document.getElementById("action").value = action;
        document.getElementById('calculator_form').submit();
    }
</script>
<body>
<h2>The server calculator!!!</h2>
<br>
<form id="calculator_form" action="http://localhost:8080/lab_06/CalculatorServlet" method="POST">
    A: <input type="number" name="a"> <br/>
    B: <input type="number" name="b"/>
    <input id="action" type="hidden" name="action" value=""/>
    <br>
    <br>
    <button id="add" onclick="javascript:call_post('add');">Add</button>
    <button id="sub" onclick="javascript:call_post('sub');">Sub</button>
    <button id="mul" onclick="javascript:call_post('mul');">Mul</button>
    <button id="div" onclick="javascript:call_post('div');">Div</button>
</form>
</br>
</body>
</html>
```

In the Servlet side, we just need to read the a, b and action parameters and return a response.

```java
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

```

## Exercise 02: The Fortune Cookie Servlet

As it was specified in the problem description, the information of these fortune cookies must be loaded once so it is required to make use of a ServletContextListener. When the context has been initialized, we proceed to load the list of these quotes.

```java
package it.unipi.dsmt.javaee.lab_06.conf;

import it.unipi.dsmt.javaee.lab_06.dao.QuotesDAO;

import javax.servlet.*;
import javax.servlet.annotation.*;

@WebListener
public class ApplicationListener implements ServletContextListener {

    public ApplicationListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /* This method is called when the servlet context is initialized(when the Web application is deployed). */
        System.out.println("This method is executed once, when the Application is started.");
        QuotesDAO.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        /* This method is called when the servlet Context is undeployed or Application Server shuts down. */
        System.out.println("When the context is destroyed, we should see this message.");
    }
}
```

The class QuotesDAO defines an init method which loads the quotes and also defines a method to pick one of these quotes at random.

```java
package it.unipi.dsmt.javaee.lab_06.dao;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

public class QuotesDAO {

    private static List<String> quotes;
    private static Random random = new Random();

    public static void init() {
        try {
            String quotesTxtFile = "data/quotes.txt";
            ClassLoader classLoader = QuotesDAO.class.getClassLoader();
            URL resource = classLoader.getResource(quotesTxtFile);
            File txtFile = new File(resource.toURI());
            quotes = Files.readAllLines(txtFile.toPath(), StandardCharsets.UTF_8);
            System.out.println("Quotes were loaded.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static String pickOneQuote(){
        int randomIndex = random.nextInt(quotes.size());
        return quotes.get(randomIndex);
    }
}
```

Finally, in the Servlet side, we just need to call the method pickOneQuote of the QuotesDAO class.

```java
package it.unipi.dsmt.javaee.lab_06.servlets;

import it.unipi.dsmt.javaee.lab_06.dao.QuotesDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "FortuneCookieServlet", value = "/FortuneCookieServlet")
public class FortuneCookieServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String randomQuote = QuotesDAO.pickOneQuote();
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

```

## Exercise 03: Beers REST API

Similar as in the previous exercise, we have to load the information from this beers.json file once.

```java
package it.unipi.dsmt.javaee.lab_06.conf;

import it.unipi.dsmt.javaee.lab_06.dao.BeersDAO;
import it.unipi.dsmt.javaee.lab_06.dao.QuotesDAO;

import javax.servlet.*;
import javax.servlet.annotation.*;

@WebListener
public class ApplicationListener implements ServletContextListener {

    public ApplicationListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /* This method is called when the servlet context is initialized(when the Web application is deployed). */
        System.out.println("This method is executed once, when the Application is started.");
        QuotesDAO.init();
        BeersDAO.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        /* This method is called when the servlet Context is undeployed or Application Server shuts down. */
        System.out.println("When the context is destroyed, we should see this message.");
    }
}
```

This BeerDAO class implements a method to initialize an internal List and another method to filter out those beers which includes a keyword passed as parameter to that method.

```java
package it.unipi.dsmt.javaee.lab_06.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipi.dsmt.javaee.lab_06.entity.Beer;
import it.unipi.dsmt.javaee.lab_06.dto.BeerDTO;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class BeersDAO {

    private static List<Beer> beerList = new ArrayList<Beer>();

    public static void init() {
        try {
            String beersJSONFile = "data/beers.json";
            ClassLoader classLoader = BeersDAO.class.getClassLoader();
            URL resource = classLoader.getResource(beersJSONFile);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(resource);

            JsonNode searchResultNode = jsonNode.get("search_results");
            Iterator<JsonNode> iterator = searchResultNode.iterator();
            while (iterator.hasNext()) {
                JsonNode beerJSON = iterator.next();
                Beer beer = new Beer();
                beer.setName(beerJSON.get("title").asText());
                beer.setAsin(beerJSON.get("asin").asText());
                beer.setLink(beerJSON.get("link").asText());
                beer.setImage(beerJSON.get("image").asText());
                beer.setRating(beerJSON.get("rating").asDouble());
                beer.setRatingsTotal(beerJSON.get("ratings_total").asDouble());
                beerList.add(beer);
            }
            System.out.println("Loaded " + beerList.size() + " beers.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<BeerDTO> search(String keyword){
        return beerList.stream()
                .filter(beer -> beer.getName().toLowerCase().contains(keyword.toLowerCase()))
                .map(beer -> new BeerDTO(beer.getName(), beer.getLink()))
                .collect(Collectors.toList());
    }

}
```

Finally, in the Servlet side, we just need to call the method search of the BeersDAO class and convert the List of BeerDTO into a JSON format.

```java
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

```