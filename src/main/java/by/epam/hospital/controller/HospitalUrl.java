package by.epam.hospital.controller;

public class HospitalUrl {
    public static final String EMPTY = "";
    public static final String MAIN_URL = "http://localhost:8080/hospital";
    public static final String APP_NAME_URL = "/hospital";
    public static final String SERVLET_MAIN = "/main-servlet";

    public static final String PAGE_MAIN = "/main";
    public static final String PAGE_REGISTRY = "/registry";
    public static final String PAGE_USER_CREDENTIALS = "/user-credentials";
    public static final String PAGE_ROLE_CONTROL = "/role-control";
    public static final String PAGE_DEPARTMENT_CONTROL = "/department-control";
    public static final String PAGE_ERROR = "/error-handler";
    public static final String PAGE_DIAGNOSE_DISEASE = "/diagnose-disease";
    public static final String PAGE_PROFILE = "/profile";
    public static final String PAGE_PATIENT_THERAPIES = "/patient-therapies";
    public static final String PAGE_DOCTOR_THERAPIES = "/doctor-therapies";
    public static final String PAGE_MEDICAMENT_CONTROL = "/medicament-control";
    public static final String PAGE_PROCEDURE_CONTROL = "/procedure-control";
    public static final String PAGE_ASSIGN_MEDICAMENT = "/assign-medicament";
    public static final String PAGE_ASSIGNMENT_MEDICATIONS = "/assignment-medications";
    public static final String PAGE_ASSIGN_PROCEDURE = "/assign-procedure";
    public static final String PAGE_ASSIGNMENT_PROCEDURES = "/assignment-procedures";

    public static final String COMMAND_FIND_USER_DETAILS = "/c-find-user-details";
    public static final String COMMAND_EDIT_USER_DETAILS = "/c-edit-user-details";

    public static final String COMMAND_CHANGE_DEPARTMENT_HEAD = "/c-change-department-head";
    public static final String COMMAND_FIND_DEPARTMENT_CONTROL_ATTRIBUTES = "/c-find-department-control-attributes";
    public static final String COMMAND_FIND_ROLE_CONTROL_ATTRIBUTES = "/c-find-role-control-attributes";
    public static final String COMMAND_MOVE_DOCTOR_TO_DEPARTMENT = "/c-move-doctor-to-department";
    public static final String COMMAND_ROLE_CONTROL = "/c-role-control";
    public static final String COMMAND_PROCEDURE_CONTROL = "/c-procedure-control";
    public static final String COMMAND_MEDICAMENT_CONTROL = "/c-medicament-control";
    public static final String COMMAND_ADMIN_FIND_MEDICATIONS_PAGING = "/c-a-find-medications-paging";
    public static final String COMMAND_ADMIN_FIND_PROCEDURES_PAGING = "/c-a-find-procedures-paging";

    public static final String COMMAND_REGISTER_CLIENT = "/c-register-client";
    public static final String COMMAND_FIND_USER_CREDENTIALS = "/c-find-user-credentials";

    public static final String COMMAND_CLOSE_THERAPY = "/c-close-therapy";
    public static final String COMMAND_DIAGNOSE_DISEASE = "/c-diagnose-disease";
    public static final String COMMAND_FIND_OPEN_DOCTOR_THERAPIES = "/c-find-open-doctor-therapies";
    public static final String COMMAND_FIND_PATIENT_THERAPIES = "/c-find-patient-therapies";
    public static final String COMMAND_MAKE_LAST_DIAGNOSIS_FINAL = "/c-make-last-diagnosis-final";
    public static final String COMMAND_DOCTOR_FIND_MEDICATIONS_PAGING = "/c-d-find-medications-paging";
    public static final String COMMAND_DOCTOR_FIND_PROCEDURES_PAGING = "/c-d-find-procedures-paging";
    public static final String COMMAND_FIND_ASSIGNMENT_MEDICATIONS = "/c-find-assignment-medications";
    public static final String COMMAND_FIND_ASSIGNMENT_PROCEDURES = "/c-find-assignment-procedures";
    public static final String COMMAND_ASSIGN_MEDICAMENT = "/c-assign-medicament";
    public static final String COMMAND_ASSIGN_PROCEDURE = "/c-assign-procedure";

    private HospitalUrl() {
    }
}
