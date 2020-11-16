package by.epam.hospital.service;

public enum ServiceAction {
    ADD, DELETE;

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
