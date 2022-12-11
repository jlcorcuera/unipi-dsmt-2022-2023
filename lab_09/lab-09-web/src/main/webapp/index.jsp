<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: jose
  Date: 29/11/22
  Time: 23:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>DSMT: Lab 09</title>
    <style>
        .red {
            color: red;
            font-weight: bold;
        }
        .blue {
            color: blue;
            font-weight: bold;
        }
    </style>
</head>
<%
    int n = 10;
    SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    String currentDateTime = SDF.format(new Date());
%>

<body>
    <h1>Welcome, the current time is <%= currentDateTime %></h1>
    <br>
    <b><p>Fib(n) = Fib(n - 1) + Fib(n - 2) </p></b>
    <p>Listing first <%= n %> Fibonacci numbers:</p>
    <%
        int fib1 = 0;
        int fib2 = 1;
    %>
    <p>Fib(0) = <%= fib1 %>.</p>
    <p>Fib(1) = <%= fib2 %>.</p>
    <%
        for (int i = 2; i < n; i++) {
            int curFib = fib1 + fib2;
            fib1 = fib2;
            fib2 = curFib;
    %>
    <p class="<%= (i % 2 == 0) ? "red" : "blue" %>">Fib(<%= i %>) = <%= curFib %>.</p>
    <% } %>

    <h2>Examples</h2>
    <ul>
        <li><a target="_blank" href="${pageContext.request.contextPath}/CalculatorServlet?action=add&a=151&b=100">CalculatorServlet example</a></li>
        <li><a target="_blank" href="${pageContext.request.contextPath}/FortuneCookieServlet">FortuneCookieServlet example</a></li>
    </ul>
    <h2>Exercises</h2>
    <ul>
        <li><a target="_blank" href="#">Exercise 01: BeersEJB</a></li>
        <li><a target="_blank" href="#">Exercise 02: BeersEJB v.2.0</a></li>
        <li><a target="_blank" href="#">Shopping Cart EJB</a></li>
    </ul>

</body>
</html>