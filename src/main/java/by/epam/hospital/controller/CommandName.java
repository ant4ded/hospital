package by.epam.hospital.controller;

public enum CommandName {
    AUTHORIZATION,
    FIRST_VISIT,
    SIGN_OUT,

    FIND_USER_CREDENTIALS,
    REGISTER_CLIENT,

    CHANGE_DEPARTMENT_HEAD,
    FIND_DEPARTMENT_CONTROL_ATTRIBUTES,
    FIND_ROLE_CONTROL_ATTRIBUTES,
    MOVE_DOCTOR_TO_DEPARTMENT,
    ROLE_CONTROL,
    PROCEDURE_CONTROL,
    MEDICAMENT_CONTROL,
    ADMIN_FIND_MEDICATIONS_PAGING,
    ADMIN_FIND_PROCEDURES_PAGING,

    FIND_USER_DETAILS,
    EDIT_USER_DETAILS,

    CLOSE_THERAPY,
    DIAGNOSE_DISEASE,
    FIND_OPEN_DOCTOR_THERAPIES,
    FIND_PATIENT_THERAPIES,
    MAKE_LAST_DIAGNOSIS_FINAL;

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
