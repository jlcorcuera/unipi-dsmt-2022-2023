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
