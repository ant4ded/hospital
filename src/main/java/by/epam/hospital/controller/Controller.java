package by.epam.hospital.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// TODO: 16.10.2020 add urlPatterns for all roles and change action in all forms
//  (it's necessary for correct work authentication filter)
@WebServlet(urlPatterns = {"", "/main-servlet"})
public class Controller extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CommandProvider commandHelper = new CommandProvider();
        String commandFromRequest = req.getParameter(ParameterName.COMMAND);
        if (commandFromRequest != null && !commandFromRequest.isBlank()) {
            Command command = commandHelper.getCommand(CommandName.valueOf(commandFromRequest));
            command.execute(req, resp);
        } else {
            commandHelper.getCommand(CommandName.FIRST_VISIT).execute(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CommandProvider commandHelper = new CommandProvider();
        String commandFromRequest = req.getParameter(ParameterName.COMMAND);
        if (commandFromRequest != null && !commandFromRequest.isBlank()) {
            Command command = commandHelper.getCommand(CommandName.valueOf(commandFromRequest));
            command.execute(req, resp);
        }
    }
}
