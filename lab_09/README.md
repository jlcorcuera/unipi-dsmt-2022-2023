
# Hands-on session 9: EJBs

In this session, the next topics are covered:

- Java Enterprise Application Project
- Step 1: defining the interfaces - pojos project
- Step 2: defining the EJB Application project
- Step 3: defining the Web Application project
- Step 4: deploying our EJB & Web Application in Glassfish
- Referring to a JDBC Resource


## Required software

For this session it is required to have installed:

- Java SDK 11. (*)
- Apache Maven 3.x version. (*)
- [Glassfish 6.2.5](https://www.eclipse.org/downloads/download.php?file=/ee4j/glassfish/glassfish-6.2.5.zip) version.
- An IDE (IntelliJ, Eclipse, Netbeans)

Also, do not forget to define the following environment variables:

- `M2_HOME` -> root directory of your Maven installation.
- `JDK_HOME` -> root directory of your JDK installation.
- Update the `PATH` environment variable by adding the `bin` directories of your JDK and Maven installations.

(*) You can avoid doing all these step manually by installing
[SDKMAN](https://sdkman.io/).


## Restoring a MySQL dump file: world.sql

Run the following command in a terminal by using (do not forget to update/provide your MySQL credentials):

```sh
mysql -u <user> -p < world.sql
```

## Configuring a JDBC Resource

Please, perform the steps defined to create a [JDBC Resource](https://github.com/jlcorcuera/unipi-dsmt-2022-2023/tree/main/lab_08#exercise-03-creating-a-jdbc-resource): **jdbc/WorldPool**.

**Now, you are ready to deploy the EJB and Web application projects in Glassfish.**

## Exercise 01: BeersEJB

The goal for this exercise is to implement an EJB which implements a method called **search**. This method should behave as the BeersDAO.search method of lab_07. In that lab session, the data was obtained by loading the information from a JSON file.

**Remember, since we have a Web Application and EJBs, we have to define shared data types like: DTOs and Interfaces in our lab-09-ejb-interfaces project.** 

### Project lab-09-ejb-interfaces

In this project we have two define our BeerDTO DTO and BeerEJB interface (you can check the implementation in these files):

**File: src/main/java/it/unipi/dsmt/jakartaee/lab_09_ejb_interfaces/dto/BeerDTO.java**
**File: src/main/java/it/unipi/dsmt/jakartaee/lab_09_ejb_interfaces/interfaces/BeerEJB.java**

### Project lab-09-ejb

Now, we have to define our BeerEJBFromJSONImpl EJB which loads the information from a JSON file and filter out those beers which match a keywork (see **search** method):

**File: src/main/java/it/unipi/dsmt/jakartaee/lab_09_ejb/ejb/BeerEJBFromJSONImpl.java**
```java
package it.unipi.dsmt.jakartaee.lab_09_ejb.ejb;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipi.dsmt.jakartaee.lab_09_ejb.model.Beer;
import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.dto.BeerDTO;
import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.interfaces.BeerEJB;
import jakarta.ejb.Stateless;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Stateless(mappedName = "BeerEJBFromJSON")
public class BeerEJBFromJSONImpl implements BeerEJB {

    private static List<Beer> beerList = new ArrayList<Beer>();

    public BeerEJBFromJSONImpl(){
        init();
    }

    public static void init() {
        try {
            String beersJSONFile = "data/beers.json";
            ClassLoader classLoader = BeerEJBFromJSONImpl.class.getClassLoader();
            URL resource = classLoader.getResource(beersJSONFile);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(resource);

            JsonNode searchResultNode = jsonNode.get("search_results");
            Iterator<JsonNode> iterator = searchResultNode.iterator();
            while (iterator.hasNext()) {
                JsonNode beerJSON = iterator.next();
                Beer beer = new Beer();
                beer.setId(UUID.randomUUID().toString());
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

    @Override
    public List<BeerDTO> search(String keyword){
        return beerList.stream()
                .filter(beer -> beer.getName().toLowerCase().contains(keyword.toLowerCase()))
                .map(beer -> new BeerDTO(beer.getId(), beer.getName(), beer.getLink(), beer.getImage(), beer.getRating()))
                .collect(Collectors.toList());
    }
}
```

### Project lab-09-web

In the Web Application side, the BeerSearch1Servlet servlet makes use of the previously defined EJB:

**File: src/main/java/it/unipi/dsmt/jakartaee/lab_09/servlets/BeerSearch1Servlet.java**
```java
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
```

## Exercise 02: BeersEJB v.2.0

Different from the previous question, for this exercise we are going to fetch beer data from a MySQL Database Table. 

### Restoring the database: unipi.sql

Run the following command in a terminal by using (do not forget to update/provide your MySQL credentials):

```sh
mysql -u <user> -p < unipi.sql
```

### Configuring a JDBC Resource

Please, perform the steps defined to create a [JDBC Resource](https://github.com/jlcorcuera/unipi-dsmt-2022-2023/tree/main/lab_08#exercise-03-creating-a-jdbc-resource): **jdbc/BeerPool**.

### Project lab-09-ejb-interfaces

No changes needed. We are going to use the same DTO and Interface.

### Project lab-09-ejb

Now, we have to define our BeerEJBFromMySQL EJB which filters out records (see **search** method):

**File: src/main/java/it/unipi/dsmt/jakartaee/lab_09_ejb/ejb/BeerEJBFromMySQLImpl.java**
```java
package it.unipi.dsmt.jakartaee.lab_09_ejb.ejb;

import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.dto.BeerDTO;
import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.interfaces.BeerEJB;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Stateless(mappedName = "BeerEJBFromMySQL")
public class BeerEJBFromMySQLImpl implements BeerEJB {

    private static final Integer BEER_PRODUCT_TYPE = 2;

    @Resource(lookup = "jdbc/BeerPool")
    private DataSource dataSource;

    @Override
    public List<BeerDTO> search(String keyword){
        List<BeerDTO> beers = new ArrayList<>();
        try(Connection connection = dataSource.getConnection()) {
            StringBuilder sql = new StringBuilder();
            sql.append("select ");
            sql.append("    ep.id, ");//1
            sql.append("    ep.name, ");//2
            sql.append("    image_url as link, ");//3
            sql.append("    image_url as image, ");//4
            sql.append("    1 as rating ");//5
            sql.append("from ");
            sql.append("    ec_product ep ");
            sql.append("    inner join ec_beer eb on ep.id = eb.product_id ");
            sql.append("where ");
            sql.append(" ep.type = ").append(BEER_PRODUCT_TYPE);
            if (keyword != null && !keyword.isEmpty()){
                sql.append("  and lower(ep.name) like concat('%', lower(?), '%') ");
            }
            sql.append(" order by lower(ep.name)");
            try(PreparedStatement pstmt = connection.prepareStatement(sql.toString())){
                if (keyword != null && !keyword.isEmpty()){
                    pstmt.setString(1, keyword);
                }
                try(ResultSet rs = pstmt.executeQuery()){
                    while (rs.next()){
                        BeerDTO dto = new BeerDTO();
                        dto.setId(rs.getString(1));
                        dto.setName(rs.getString(2));
                        dto.setLink(rs.getString(3));
                        dto.setImageUrl(rs.getString(4));
                        dto.setRating(rs.getDouble(5));
                        beers.add(dto);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beers;
    }
}
```

### Project lab-09-web

We have defined another servlet called **BeerSearch2Servlet**. The code is similar to the previous BeerSearch1Servlet servlet but the injection of an EJB reference is different:

**File: src/main/java/it/unipi/dsmt/jakartaee/lab_09/servlets/BeerSearch2Servlet.java**
```java
    ...
    @EJB(mappedName = "BeerEJBFromMySQL")
    private BeerEJB beerEJB;
    ...
```

## Exercise 03: Shopping Cart EJB

This functionality originally was implemented by defining a **ShoppingCartDTO** object and storing it into the user's session (**HttpSession**). For this question we are going to implement a Stateless EJB which and its reference will be stored into the user's session.

### Project lab-09-ejb-interfaces

An Interface was defined with all operations to be supported by the ShoppingCart EJB:

**File: src/main/java/it/unipi/dsmt/jakartaee/lab_09_ejb_interfaces/interfaces/ShoppingCartEJB.java**
```java
package it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.interfaces;

import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.dto.BeerDTO;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface ShoppingCartEJB {

    public void addProduct(String productId, String name);
    public List<BeerDTO> getBeerDTOList();
    public int getTotalNumberProducts();
}
```

### Project lab-09-ejb

Here, ShoppingCartEJBImpl is defined. This EJB defines logic to handle products in a shopping cart.

**File: src/main/java/it/unipi/dsmt/jakartaee/lab_09_ejb/ejb/ShoppingCartEJBImpl.java**
```java
package it.unipi.dsmt.jakartaee.lab_09_ejb.ejb;

import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.dto.BeerDTO;
import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.interfaces.ShoppingCartEJB;
import jakarta.ejb.Stateful;

import java.util.ArrayList;
import java.util.List;

@Stateful
public class ShoppingCartEJBImpl implements ShoppingCartEJB {

    private List<BeerDTO> beerDTOList;

    public ShoppingCartEJBImpl(){
        beerDTOList = new ArrayList<>();
    }

    public void addProduct(String productId, String name){
        BeerDTO found = null;
        for(BeerDTO beerDTO: beerDTOList) {
            if (beerDTO.getId().equals(productId)) {
                found = beerDTO;
                break;
            }
        }
        if (found != null) {
            found.setQuantity(found.getQuantity() + 1);
        } else {
            found = new BeerDTO(productId, name, 1);
            beerDTOList.add(found);
        }
    }

    public List<BeerDTO> getBeerDTOList() {
        return beerDTOList;
    }

    public int getTotalNumberProducts() {
        return beerDTOList.stream()
                .map(beerDTO -> beerDTO.getQuantity())
                .reduce(0, Integer::sum);
    }

}
```

### Project lab-09-web

The trick in these changes is to store the reference of the ShoppingCartEJB into the user session. Please, pay attention in the implementation and usage of the **retrieveShoppingCartEJB** method.


**File: src/main/java/it/unipi/dsmt/jakartaee/lab_09/servlets/ShoppingCartServlet.java**
```java
package it.unipi.dsmt.jakartaee.lab_09.servlets;

import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.dto.BeerDTO;
import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.interfaces.QuotesEJB;
import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.interfaces.ShoppingCartEJB;
import jakarta.ejb.EJB;
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
        }
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
        HttpSession session = request.getSession(true);
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
                String id = "java:global/lab_09_ejb/ShoppingCartEJBImpl!" + ShoppingCartEJB.class.getName();
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
```