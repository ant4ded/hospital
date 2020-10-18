package by.epam.hospital.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"", "/main-servlet"})
public class Controller extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CommandProvider commandHelper = new CommandProvider();
        String commandFromRequest = request.getParameter(ParameterName.COMMAND);
        if (commandFromRequest != null && !commandFromRequest.isBlank()) {
            Command command = commandHelper.getCommand(CommandName.valueOf(commandFromRequest));
            command.execute(request, response);
        } else {
            commandHelper.getCommand(CommandName.FIRST_VISIT).execute(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CommandProvider commandHelper = new CommandProvider();
        String commandFromRequest = request.getParameter(ParameterName.COMMAND);
        if (commandFromRequest != null && !commandFromRequest.isBlank()) {
            Command command = commandHelper.getCommand(CommandName.valueOf(commandFromRequest));
            command.execute(request, response);
        }
    }
}
