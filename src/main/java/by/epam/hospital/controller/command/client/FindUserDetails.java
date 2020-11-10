package by.epam.hospital.controller.command.client;

import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.UserDetails;
import by.epam.hospital.entity.table.UsersDetailsFieldName;
import by.epam.hospital.service.ClientService;
import by.epam.hospital.service.ServiceException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FindUserDetails implements HttpCommand {
    private static final String MESSAGE_UPDATE_UNSUCCESSFUL = "Update failed.";

    private final ClientService clientService;
    private final Logger logger;

    public FindUserDetails(ClientService clientService, Logger logger) {
        this.clientService = clientService;
        this.logger = logger;
    }

    @Override
    public Map<String, Object> execute(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        String login = String.valueOf(request.getSession().getAttribute(ParameterName.LOGIN_USERNAME));
        try {
            Optional<UserDetails> optionalUserDetails = clientService.findUserDetails(login);
            if (optionalUserDetails.isPresent()) {
                result.put(UsersDetailsFieldName.PASSPORT_ID, optionalUserDetails.get().getPassportId());
                result.put(UsersDetailsFieldName.GENDER, optionalUserDetails.get().getGender());
                result.put(UsersDetailsFieldName.FIRST_NAME, optionalUserDetails.get().getFirstName());
                result.put(UsersDetailsFieldName.SURNAME, optionalUserDetails.get().getSurname());
                result.put(UsersDetailsFieldName.LAST_NAME, optionalUserDetails.get().getLastName());
                result.put(UsersDetailsFieldName.BIRTHDAY, optionalUserDetails.get().getBirthday());
                result.put(UsersDetailsFieldName.ADDRESS, optionalUserDetails.get().getAddress());
                result.put(UsersDetailsFieldName.PHONE, optionalUserDetails.get().getPhone());
            } else {
                result.put(ParameterName.MESSAGE, MESSAGE_UPDATE_UNSUCCESSFUL);
            }
            result.put(ParameterName.PAGE_FORWARD, HospitalUrl.PAGE_PROFILE);
        } catch (ServiceException e) {
            logger.error(e);
            result.put(ParameterName.COMMAND_EXCEPTION, e.getMessage());
        }
        return result;
    }
}
