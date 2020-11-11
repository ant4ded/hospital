package by.epam.hospital.service;

import by.epam.hospital.entity.CardType;

public enum ServiceAction {
    ADD, REMOVE;

    public static boolean hasValue(String value) {
        if (value != null) {
            for (ServiceAction v : ServiceAction.values()) {
                if (v.name().equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }
}
