package by.epam.hospital.controller.command.receptionist;

import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import by.epam.hospital.entity.table.UsersDetailsFieldName;
import by.epam.hospital.entity.table.UsersFieldName;
import by.epam.hospital.service.ReceptionistService;
import by.epam.hospital.service.ServiceException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FindUserCredentials implements HttpCommand {
    private static final String MESSAGE_NON_EXISTENT_USER = "Non existent user.";

    private final ReceptionistService receptionistService;
    private final Logger logger;

    public FindUserCredentials(ReceptionistService receptionistService, Logger logger) {
        this.receptionistService = receptionistService;
        this.logger = logger;
    }

    @Override
    public Map<String, Object> execute(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(request.getParameter(UsersDetailsFieldName.FIRST_NAME));
        userDetails.setSurname(request.getParameter(UsersDetailsFieldName.SURNAME));
        userDetails.setLastName(request.getParameter(UsersDetailsFieldName.LAST_NAME));
        userDetails.setBirthday(Date.valueOf(request.getParameter(UsersDetailsFieldName.BIRTHDAY)));
        try {
            Optional<User> optionalUser = receptionistService.findUserCredentials(userDetails);
            if (optionalUser.isPresent()) {
                result.put(UsersFieldName.LOGIN, optionalUser.get().getLogin());
                result.put(UsersFieldName.PASSWORD, optionalUser.get().getPassword());
            } else {
                result.put(ParameterName.MESSAGE, MESSAGE_NON_EXISTENT_USER);
            }
            result.put(ParameterName.PAGE_FORWARD, HospitalUrl.PAGE_USER_CREDENTIALS);
        } catch (ServiceException e) {
            logger.error(e);
            result.put(ParameterName.COMMAND_EXCEPTION, e.getMessage());
        }
        return result;
    }
}
