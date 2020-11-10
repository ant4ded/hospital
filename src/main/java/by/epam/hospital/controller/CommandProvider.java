package by.epam.hospital.controller;

import by.epam.hospital.controller.command.Authorization;
import by.epam.hospital.controller.command.FirstVisit;
import by.epam.hospital.controller.command.SignOut;
import by.epam.hospital.controller.command.admin.head.*;
import by.epam.hospital.controller.command.doctor.DiagnoseDisease;
import by.epam.hospital.controller.command.receptionist.FindUserCredentials;
import by.epam.hospital.controller.command.receptionist.RegisterClient;
import by.epam.hospital.dao.impl.*;
import by.epam.hospital.service.AdminHeadService;
import by.epam.hospital.service.ClientService;
import by.epam.hospital.service.DoctorService;
import by.epam.hospital.service.ReceptionistService;
import by.epam.hospital.service.impl.AdminHeadServiceImpl;
import by.epam.hospital.service.impl.ClientServiceImpl;
import by.epam.hospital.service.impl.DoctorServiceImpl;
import by.epam.hospital.service.impl.ReceptionistServiceImpl;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class CommandProvider {
    private final Map<CommandName, HttpCommand> map = new HashMap<>();

    public CommandProvider() {
        map.put(CommandName.FIRST_VISIT, new FirstVisit());
        map.put(CommandName.SIGN_OUT, new SignOut());
        map.put(CommandName.AUTHORIZATION,
                new Authorization(getClientService(),
                        Logger.getLogger(Authorization.class)));
        map.put(CommandName.REGISTER_CLIENT,
                new RegisterClient(getReceptionistService(),
                        Logger.getLogger(RegisterClient.class)));
        map.put(CommandName.FIND_ROLE_CONTROL_ATTRIBUTES,
                new FindRoleControlAttributes(getAdminHeadService(),
                        Logger.getLogger(FindRoleControlAttributes.class)));
        map.put(CommandName.ROLE_CONTROL,
                new RoleControl(getAdminHeadService(),
                        Logger.getLogger(RoleControl.class)));
        map.put(CommandName.FIND_DEPARTMENT_CONTROL_ATTRIBUTES,
                new FindDepartmentControlAttributes(getAdminHeadService(),
                        Logger.getLogger(FindDepartmentControlAttributes.class)));
        map.put(CommandName.CHANGE_DEPARTMENT_HEAD,
                new ChangeDepartmentHead(getAdminHeadService(),
                        Logger.getLogger(ChangeDepartmentHead.class)));
        map.put(CommandName.MOVE_DOCTOR_TO_DEPARTMENT,
                new MoveDoctorToDepartment(getAdminHeadService(),
                        Logger.getLogger(MoveDoctorToDepartment.class)));
        map.put(CommandName.DIAGNOSE_DISEASE,
                new DiagnoseDisease(getDoctorService(),
                        Logger.getLogger(DiagnoseDisease.class)));
        map.put(CommandName.FIND_USER_CREDENTIALS,
                new FindUserCredentials(getReceptionistService(),
                        Logger.getLogger(FindUserCredentials.class)));
    }

    public HttpCommand getCommand(CommandName command) {
        return map.get(command);
    }

    private ClientService getClientService() {
        return new ClientServiceImpl(new UserDaoImpl());
    }

    private ReceptionistService getReceptionistService() {
        return new ReceptionistServiceImpl(new UserDaoImpl(), new UserDetailsDaoImpl());
    }

    private AdminHeadService getAdminHeadService() {
        return new AdminHeadServiceImpl(new UserDaoImpl(), new DepartmentDaoImpl(), new DepartmentStaffDaoImpl());
    }

    private DoctorService getDoctorService() {
        return new DoctorServiceImpl(new IcdDaoImpl(), new UserDaoImpl(),
                new TherapyDaoImpl(), new DiagnosisDaoImpl(), new UserDetailsDaoImpl());
    }

}
