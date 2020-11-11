package by.epam.hospital.service;

public enum ServiceAction {
    ADD, REMOVE;

    public static boolean isAdd(String action) {
        boolean result = false;
        if (action != null) {
            result = action.equals(ADD.toString());
        }
        return result;
    }

    public static boolean isRemove(String action) {
        boolean result = false;
        if (action != null) {
            result = action.equals(REMOVE.toString());
        }
        return result;
    }
}
