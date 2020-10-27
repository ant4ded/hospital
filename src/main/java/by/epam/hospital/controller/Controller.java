package by.epam.hospital.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {HospitalUrl.EMPTY, HospitalUrl.SERVLET_MAIN, HospitalUrl.COMMAND_REGISTER_CLIENT,
        HospitalUrl.COMMAND_CHANGE_DEPARTMENT_HEAD, HospitalUrl.COMMAND_FIND_DEPARTMENT_CONTROL_ATTRIBUTES,
        HospitalUrl.COMMAND_FIND_ROLE_CONTROL_ATTRIBUTES, HospitalUrl.COMMAND_MOVE_DOCTOR_TO_DEPARTMENT,
        HospitalUrl.COMMAND_ROLE_CONTROL})
public class Controller extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CommandProvider commandHelper = new CommandProvider();
        String commandFromRequest = request.getParameter(ParameterName.COMMAND);
        if (commandFromRequest != null && !commandFromRequest.isBlank()) {
            HttpCommand httpCommand = commandHelper.getCommand(CommandName.valueOf(commandFromRequest));
            httpCommand.execute(request, response);
        } else {
            commandHelper.getCommand(CommandName.FIRST_VISIT).execute(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CommandProvider commandHelper = new CommandProvider();
        String commandFromRequest = request.getParameter(ParameterName.COMMAND);
        if (commandFromRequest != null && !commandFromRequest.isBlank()) {
            HttpCommand httpCommand = commandHelper.getCommand(CommandName.valueOf(commandFromRequest));
            httpCommand.execute(request, response);
        }
    }
}
