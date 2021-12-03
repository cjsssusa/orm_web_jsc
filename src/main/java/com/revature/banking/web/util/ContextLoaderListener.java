package com.revature.banking.web.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.banking.orm.utils.CrudORM;
import com.revature.banking.services.BankService;
import com.revature.banking.services.UserService;
import com.revature.banking.web.servlets.AuthServlet;
import com.revature.banking.web.servlets.BankServlet;
import com.revature.banking.web.servlets.UserServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.URISyntaxException;
import java.sql.SQLException;

public class ContextLoaderListener implements ServletContextListener {

    private final static Logger logger = LogManager.getLogger();

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        System.out.println("Initializing application");

        ObjectMapper objectMapper = new ObjectMapper();

        CrudORM crudORM = new CrudORM(this);

        UserService userService = new UserService(crudORM);

        BankService bankService = new BankService(userService, crudORM);

        UserServlet userServlet = new UserServlet(userService, objectMapper);
        AuthServlet authServlet = new AuthServlet(userService, objectMapper);
        BankServlet bankServlet = new BankServlet(bankService, objectMapper);

        ServletContext context = sce.getServletContext();
        context.addServlet("UserServlet", userServlet).addMapping("/users/*");
        context.addServlet("AuthServlet", authServlet).addMapping("/auth");
        context.addServlet("BankServlet", bankServlet).addMapping("/bank");

        try {
            crudORM.createAllOfTablesWithDataSourceORM(this);
            System.out.println("Done createAllOfTablesWithDataSourceORM");
        } catch (SQLException | URISyntaxException e) {
            e.printStackTrace();
        }

        System.out.println("Application initialized!");
//        logger.info("Application initialized!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Shutting down Banking web application!");
    }
}
