package com.revature.banking.web.servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.banking.models.AppUser;
import com.revature.banking.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class UserServlet extends HttpServlet {

    private final UserService userService;
    private final ObjectMapper mapper;

    public UserServlet(UserService userService, ObjectMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("beginning of doPost..... cjs");
            PrintWriter respWriter = resp.getWriter();
            resp.setContentType("application/json");

            System.out.println("type = " + req.getParameter("type"));

            if (req.getParameter("type").equals("read_all")) {
                List<AppUser> appUserList = userService.readUsers();
                if (appUserList != null) {
                    System.out.println("User successfully fetched!");

                    if (appUserList.isEmpty()) {
                        resp.setStatus(404); // no cards found
                        return; // return here so you don't try to execute the logic after this block
                    }

                    System.out.println(appUserList);
                    resp.setStatus(201);
                    String payload = mapper.writeValueAsString(appUserList);
                    resp.getWriter().write(payload);

                } else {
                    System.out.println("Could not fetch user! Check logs.");
                    resp.setStatus(500); // server error
                }
            } else {
                AppUser newUser = mapper.readValue(req.getInputStream(), AppUser.class);
                System.out.println(newUser);
                boolean wasRegistered = userService.registerNewUser(newUser);
                if (wasRegistered) {
                    System.out.println("User successfully persisted!");
                    resp.setStatus(201);
                } else {
                    System.out.println("Could not persist user! Check logs.");
                    resp.setStatus(500); // server error
                }
            }
        } catch (JsonParseException e) {
            resp.setStatus(400); // client error; BAD REQUEST
            e.printStackTrace();
        }
    }
}
