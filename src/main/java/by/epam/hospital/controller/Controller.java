package by.epam.hospital.controller;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(urlPatterns = {HospitalUrl.EMPTY, HospitalUrl.SERVLET_MAIN,
        HospitalUrl.COMMAND_EDIT_USER_DETAILS, HospitalUrl.COMMAND_FIND_USER_DETAILS,

        HospitalUrl.COMMAND_CHANGE_DEPARTMENT_HEAD, HospitalUrl.COMMAND_FIND_DEPARTMENT_CONTROL_ATTRIBUTES,
        HospitalUrl.COMMAND_FIND_ROLE_CONTROL_ATTRIBUTES, HospitalUrl.COMMAND_MOVE_DOCTOR_TO_DEPARTMENT,
        HospitalUrl.COMMAND_ROLE_CONTROL,

        HospitalUrl.COMMAND_REGISTER_CLIENT, HospitalUrl.COMMAND_FIND_USER_CREDENTIALS,

        HospitalUrl.COMMAND_CLOSE_THERAPY, HospitalUrl.COMMAND_DIAGNOSE_DISEASE,
        HospitalUrl.COMMAND_FIND_OPEN_DOCTOR_THERAPIES, HospitalUrl.COMMAND_FIND_PATIENT_THERAPIES,
        HospitalUrl.COMMAND_MAKE_LAST_DIAGNOSIS_FINAL})
public class Controller extends HttpServlet {
    private static final Logger logger = Logger.getLogger(Controller.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        CommandProvider commandProvider = new CommandProvider();
        String commandFromRequest = request.getParameter(ParameterName.COMMAND);
        try {
            if (commandFromRequest != null && !commandFromRequest.isBlank()) {
                doCommand(request, response, commandFromRequest);
            } else {
                commandProvider.getCommand(CommandName.FIRST_VISIT).execute(request, response);
            }
        } catch (ServletException | IOException e) {
            logger.error("Exception in controller. Get method.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String commandFromRequest = request.getParameter(ParameterName.COMMAND);
        try {
            if (commandFromRequest != null && !commandFromRequest.isBlank()) {
                doCommand(request, response, commandFromRequest);
            }
        } catch (ServletException | IOException e) {
            logger.error("Exception in controller. Post method", e);
        }
    }

    private void doCommand(HttpServletRequest request, HttpServletResponse response, String commandName)
            throws ServletException, IOException {
        CommandProvider commandHelper = new CommandProvider();
        HttpCommand httpCommand = commandHelper.getCommand(CommandName.valueOf(commandName));
        Map<String, Object> parameters = httpCommand.execute(request, response);
        if (!parameters.containsKey(ParameterName.COMMAND_EXCEPTION)) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                if (!entry.getKey().equals(ParameterName.PAGE_FORWARD)) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                }
            }
            request.getRequestDispatcher(String.valueOf(parameters.get(ParameterName.PAGE_FORWARD)))
                    .forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    String.valueOf(parameters.get(ParameterName.COMMAND_EXCEPTION)));
        }
    }
}
