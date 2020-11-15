package by.epam.hospital.controller.command.doctor;

import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.HttpCommand;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.CardType;
import by.epam.hospital.entity.Therapy;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import by.epam.hospital.entity.table.UsersDetailsFieldName;
import by.epam.hospital.service.DoctorService;
import by.epam.hospital.service.ServiceException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CloseTherapy implements HttpCommand {
    private static final String MESSAGE_SUCCESS = "Success.";
    private static final String MESSAGE_WRONG_RESULT = "Patient not exist.";

    private final DoctorService doctorService;
    private final Logger logger;

    public CloseTherapy(DoctorService doctorService, Logger logger) {
        this.doctorService = doctorService;
        this.logger = logger;
    }

    @Override
    public Map<String, Object> execute(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        String doctorLogin = String.valueOf(request.getSession().getAttribute(ParameterName.LOGIN_USERNAME));
        UserDetails userDetails = new UserDetails();
        String firstName = request.getParameter(UsersDetailsFieldName.FIRST_NAME);
        userDetails.setFirstName(firstName);
        String surname = request.getParameter(UsersDetailsFieldName.SURNAME);
        userDetails.setSurname(surname);
        String lastName = request.getParameter(UsersDetailsFieldName.LAST_NAME);
        userDetails.setLastName(lastName);
        Date birthday = Date.valueOf(request.getParameter(UsersDetailsFieldName.BIRTHDAY));
        userDetails.setBirthday(birthday);
        CardType cardType = CardType.valueOf(request.getParameter(ParameterName.CARD_TYPE));
        try {
            Optional<User> userOptional = doctorService.findPatientByUserDetails(userDetails);
            if (userOptional.isPresent()) {
                boolean isDone = doctorService.closeTherapy(doctorLogin, userOptional.get().getLogin(), cardType);
                if (isDone) {
                    List<Therapy> therapies = doctorService.findOpenDoctorTherapies(doctorLogin, cardType);
                    if (!therapies.isEmpty()) {
                        result.put(ParameterName.THERAPIES_LIST, therapies);
                    }
                } else {
                    result.put(ParameterName.MESSAGE, MESSAGE_WRONG_RESULT);
                }
            }
            result.put(ParameterName.PAGE_FORWARD, HospitalUrl.PAGE_DOCTOR_THERAPIES);
        } catch (ServiceException e) {
            logger.error(e);
            result.put(ParameterName.COMMAND_EXCEPTION, e.getMessage());
        }
        return result;
    }
}
