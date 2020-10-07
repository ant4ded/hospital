package by.epam.hospital.controller;

import by.epam.hospital.controller.main.MainCommandHelper;
import by.epam.hospital.controller.main.MainCommandName;
import by.epam.hospital.controller.main.MainParameter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "")
public class MainController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MainCommandHelper commandHelper = new MainCommandHelper();
        String commandFromRequest = req.getParameter(MainParameter.COMMAND);
        if (commandFromRequest != null && !commandFromRequest.isBlank()) {
            Command command = commandHelper.getCommand(commandFromRequest);
            command.execute(req, resp);
        } else {
            commandHelper.getCommand(MainCommandName.FIRST_VISIT).execute(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MainCommandHelper commandHelper = new MainCommandHelper();
        String commandFromRequest = req.getParameter(MainParameter.COMMAND);
        if (commandFromRequest != null && !commandFromRequest.isBlank()) {
            Command command = commandHelper.getCommand(commandFromRequest);
            command.execute(req, resp);
        }
    }
}
