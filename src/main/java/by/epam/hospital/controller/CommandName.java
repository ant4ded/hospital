package by.epam.hospital.controller;

public enum CommandName {
    AUTHORIZATION,
    FIRST_VISIT,
    SIGN_OUT,
    REGISTER_CLIENT,
    FIND_USER_CREDENTIALS,
    FIND_ROLE_CONTROL_ATTRIBUTES,
    FIND_DEPARTMENT_CONTROL_ATTRIBUTES,
    ROLE_CONTROL,
    CHANGE_DEPARTMENT_HEAD,
    MOVE_DOCTOR_TO_DEPARTMENT,
    DIAGNOSE_DISEASE,
    FIND_USER_DETAILS,
    EDIT_USER_DETAILS;

    public static boolean hasValue(String value) {
        if (value != null) {
            for (CommandName v : CommandName.values()) {
                if (v.name().equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }
}
