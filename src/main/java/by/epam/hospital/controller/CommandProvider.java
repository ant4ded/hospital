package by.epam.hospital.controller.main;

import by.epam.hospital.controller.Command;
import by.epam.hospital.controller.command.Authorization;
import by.epam.hospital.controller.command.FirstVisit;
import by.epam.hospital.controller.command.SignOut;

import java.util.HashMap;
import java.util.Map;

public class CommandProvider {
    private final Map<String, Command> map = new HashMap<>();

    public CommandProvider() {
        map.put(CommandName.AUTHORIZATION, new Authorization());
        map.put(CommandName.FIRST_VISIT, new FirstVisit());
        map.put(CommandName.SIGN_OUT, new SignOut());
    }

    public Command getCommand(String command) {
        return map.get(command.toUpperCase());
    }
}
