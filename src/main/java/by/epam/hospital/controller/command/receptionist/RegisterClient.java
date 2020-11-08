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

public class RegisterClient implements HttpCommand {
    private static final String MESSAGE_SUCCESS = "Success.";
    private static final String MESSAGE_WRONG_RESULT = "Move doctor to another department was not perform.";

    private final ReceptionistService receptionistService;
    private final Logger logger;

    public RegisterClient(ReceptionistService receptionistService, Logger logger) {
        this.receptionistService = receptionistService;
        this.logger = logger;
    }

    @Override
    public Map<String, Object> execute(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        User user = new User();
        UserDetails userDetails = new UserDetails();
        boolean isSuccess;
        user.setLogin(request.getParameter(UsersFieldName.LOGIN));
        user.setPassword(request.getParameter(UsersFieldName.PASSWORD));
        userDetails.setPassportId(request.getParameter(UsersDetailsFieldName.PASSPORT_ID));
        userDetails.setGender(UserDetails.Gender.valueOf(request.getParameter(UsersDetailsFieldName.GENDER)));
        userDetails.setFirstName(request.getParameter(UsersDetailsFieldName.FIRST_NAME));
        userDetails.setSurname(request.getParameter(UsersDetailsFieldName.SURNAME));
        userDetails.setLastName(request.getParameter(UsersDetailsFieldName.LAST_NAME));
        userDetails.setBirthday(Date.valueOf(request.getParameter(UsersDetailsFieldName.BIRTHDAY)));
        userDetails.setAddress(request.getParameter(UsersDetailsFieldName.ADDRESS));
        userDetails.setPhone(request.getParameter(UsersDetailsFieldName.PHONE));

        user.setUserDetails(userDetails);
        request.removeAttribute(UsersFieldName.LOGIN);
        request.removeAttribute(UsersFieldName.PASSWORD);
        request.removeAttribute(UsersDetailsFieldName.PASSPORT_ID);
        request.removeAttribute(UsersDetailsFieldName.GENDER);
        request.removeAttribute(UsersDetailsFieldName.FIRST_NAME);
        request.removeAttribute(UsersDetailsFieldName.SURNAME);
        request.removeAttribute(UsersDetailsFieldName.LAST_NAME);
        request.removeAttribute(UsersDetailsFieldName.BIRTHDAY);
        request.removeAttribute(UsersDetailsFieldName.ADDRESS);
        request.removeAttribute(UsersDetailsFieldName.PHONE);
        try {
            isSuccess = receptionistService.registerClient(user);
            result.put(ParameterName.MESSAGE, isSuccess ?
                    MESSAGE_SUCCESS :
                    MESSAGE_WRONG_RESULT);
            result.put(ParameterName.PAGE_FORWARD, HospitalUrl.PAGE_REGISTRY);
        } catch (ServiceException e) {
            logger.error(e);
            result.put(ParameterName.COMMAND_EXCEPTION, e.getMessage());
        }
        return result;
    }
}
