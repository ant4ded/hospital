package by.epam.hospital.controller.command;

import by.epam.hospital.controller.Command;
import by.epam.hospital.controller.HospitalUrl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FirstVisit implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(HospitalUrl.PAGE_MAIN).forward(request, response);
    }
}
