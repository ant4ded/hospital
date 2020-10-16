package by.epam.hospital.controller;

import by.epam.hospital.controller.command.Authorization;
import by.epam.hospital.controller.command.FirstVisit;
import by.epam.hospital.controller.command.receptionist.RegisterClient;
import by.epam.hospital.controller.command.SignOut;
import by.epam.hospital.controller.command.admin.head.FindUserRoles;
import by.epam.hospital.controller.command.admin.head.RoleControl;

import java.util.HashMap;
import java.util.Map;

public class CommandProvider {
    private final Map<CommandName, Command> map = new HashMap<>();

    public CommandProvider() {
        map.put(CommandName.AUTHORIZATION, new Authorization());
        map.put(CommandName.FIRST_VISIT, new FirstVisit());
        map.put(CommandName.SIGN_OUT, new SignOut());
        map.put(CommandName.REGISTER_CLIENT, new RegisterClient());
        map.put(CommandName.FIND_USER_ROLES, new FindUserRoles());
        map.put(CommandName.ROLE_CONTROL, new RoleControl());
    }

    public Command getCommand(CommandName command) {
        return map.get(command);
    }
}
