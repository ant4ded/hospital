package by.epam.hospital.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface HttpCommand {
    Map<String, Object> execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException;
}
