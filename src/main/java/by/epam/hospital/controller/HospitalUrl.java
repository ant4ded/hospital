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

    public static final String COMMAND_FIND_USER_DETAILS = "/c-find-user-details";
    public static final String COMMAND_EDIT_USER_DETAILS = "/c-edit-user-details";

    public static final String COMMAND_CHANGE_DEPARTMENT_HEAD = "/c-change-department-head";
    public static final String COMMAND_FIND_DEPARTMENT_CONTROL_ATTRIBUTES = "/c-find-department-control-attributes";
    public static final String COMMAND_FIND_ROLE_CONTROL_ATTRIBUTES = "/c-find-role-control-attributes";
    public static final String COMMAND_MOVE_DOCTOR_TO_DEPARTMENT = "/c-move-doctor-to-department";
    public static final String COMMAND_ROLE_CONTROL = "/c-role-control";

    public static final String COMMAND_REGISTER_CLIENT = "/c-register-client";
    public static final String COMMAND_FIND_USER_CREDENTIALS = "/c-find-user-credentials";

    public static final String COMMAND_DIAGNOSE_DISEASE = "/c-diagnose-disease";

    private HospitalUrl() {
    }
}
