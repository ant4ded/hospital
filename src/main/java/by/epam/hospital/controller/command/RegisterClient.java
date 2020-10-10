package by.epam.hospital.controller.command;

import by.epam.hospital.controller.Command;
import by.epam.hospital.controller.HospitalUrl;
import by.epam.hospital.controller.ParameterName;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import by.epam.hospital.entity.table.UsersDetailsFieldName;
import by.epam.hospital.entity.table.UsersFieldName;
import by.epam.hospital.service.ReceptionistService;
import by.epam.hospital.service.ServiceException;
import by.epam.hospital.service.impl.ReceptionistServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

public class RegisterClient implements Command {
    private static final String SUCCESSFUL_MESSAGE = "Client registration completed successful";
    private static final String UNSUCCESSFUL_MESSAGE = "Client registration didn't complete";

    private final ReceptionistService service = new ReceptionistServiceImpl();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User();
        UserDetails userDetails = new UserDetails();
        boolean result;

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
            result = service.registerClient(user);
            request.setAttribute(ParameterName.MESSAGE, result ? SUCCESSFUL_MESSAGE : UNSUCCESSFUL_MESSAGE);
        } catch (ServiceException e) {
            request.setAttribute(ParameterName.MESSAGE, e.getMessage());
        } finally {
            request.getRequestDispatcher(HospitalUrl.MAIN_URL + HospitalUrl.PAGE_REGISTRY).forward(request, response);
        }
    }
}
