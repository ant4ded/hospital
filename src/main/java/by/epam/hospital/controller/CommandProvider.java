package by.epam.hospital.controller;

import by.epam.hospital.controller.command.Authorization;
import by.epam.hospital.controller.command.FirstVisit;
import by.epam.hospital.controller.command.admin.head.*;
import by.epam.hospital.controller.command.receptionist.RegisterClient;
import by.epam.hospital.controller.command.SignOut;

import java.util.HashMap;
import java.util.Map;

public class CommandProvider {
    private final Map<CommandName, Command> map = new HashMap<>();

    public CommandProvider() {
        map.put(CommandName.AUTHORIZATION, new Authorization());
        map.put(CommandName.FIRST_VISIT, new FirstVisit());
        map.put(CommandName.SIGN_OUT, new SignOut());
        map.put(CommandName.REGISTER_CLIENT, new RegisterClient());
        map.put(CommandName.FIND_ROLE_CONTROL_ATTRIBUTES, new FindRoleControlAttributes());
        map.put(CommandName.ROLE_CONTROL, new RoleControl());
        map.put(CommandName.FIND_DEPARTMENT_CONTROL_ATTRIBUTES, new FindDepartmentControlAttributes());
        map.put(CommandName.CHANGE_DEPARTMENT_HEAD, new ChangeDepartmentHead());
        map.put(CommandName.MOVE_DOCTOR_TO_DEPARTMENT, new MoveDoctorToDepartment());
    }

    public Command getCommand(CommandName command) {
        return map.get(command);
    }
}
