package by.epam.hospital.controller.main;

import by.epam.hospital.controller.Command;
import by.epam.hospital.controller.main.command.Authorization;
import by.epam.hospital.controller.main.command.FirstVisit;
import by.epam.hospital.controller.main.command.SignOut;

import java.util.HashMap;
import java.util.Map;

public class MainCommandHelper {
    private final Map<String, Command> map = new HashMap<>();

    public MainCommandHelper() {
        map.put(MainCommandName.AUTHORIZATION, new Authorization());
        map.put(MainCommandName.FIRST_VISIT, new FirstVisit());
        map.put(MainCommandName.SIGN_OUT, new SignOut());
    }

    public Command getCommand(String command) {
        return map.get(command.toUpperCase());
    }
}
