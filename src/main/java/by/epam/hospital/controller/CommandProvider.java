package by.epam.hospital.controller;

import by.epam.hospital.controller.command.Authorization;
import by.epam.hospital.controller.command.FirstVisit;
import by.epam.hospital.controller.command.SignOut;
import by.epam.hospital.controller.command.admin.head.*;
import by.epam.hospital.controller.command.client.EditUserDetails;
import by.epam.hospital.controller.command.client.FindUserDetails;
import by.epam.hospital.controller.command.doctor.*;
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

        map.put(CommandName.FIND_USER_CREDENTIALS,
                new FindUserCredentials(getReceptionistService(),
                        Logger.getLogger(FindUserCredentials.class)));
        map.put(CommandName.REGISTER_CLIENT,
                new RegisterClient(getReceptionistService(),
                        Logger.getLogger(RegisterClient.class)));

        map.put(CommandName.CHANGE_DEPARTMENT_HEAD,
                new ChangeDepartmentHead(getAdminHeadService(),
                        Logger.getLogger(ChangeDepartmentHead.class)));
        map.put(CommandName.FIND_DEPARTMENT_CONTROL_ATTRIBUTES,
                new FindDepartmentControlAttributes(getAdminHeadService(),
                        Logger.getLogger(FindDepartmentControlAttributes.class)));
        map.put(CommandName.FIND_ROLE_CONTROL_ATTRIBUTES,
                new FindRoleControlAttributes(getAdminHeadService(),
                        Logger.getLogger(FindRoleControlAttributes.class)));
        map.put(CommandName.MOVE_DOCTOR_TO_DEPARTMENT,
                new MoveDoctorToDepartment(getAdminHeadService(),
                        Logger.getLogger(MoveDoctorToDepartment.class)));
        map.put(CommandName.ROLE_CONTROL,
                new RoleControl(getAdminHeadService(),
                        Logger.getLogger(RoleControl.class)));
        map.put(CommandName.PROCEDURE_CONTROL,
                new ProcedureControl(getAdminHeadService(),
                        Logger.getLogger(ProcedureControl.class)));
        map.put(CommandName.MEDICAMENT_CONTROL,
                new MedicamentControl(getAdminHeadService(),
                        Logger.getLogger(MedicamentControl.class)));
        map.put(CommandName.FIND_ALL_PROCEDURES,
                new MedicamentControl(getAdminHeadService(),
                        Logger.getLogger(MedicamentControl.class)));
        map.put(CommandName.FIND_ALL_MEDICATIONS,
                new MedicamentControl(getAdminHeadService(),
                        Logger.getLogger(MedicamentControl.class)));

        map.put(CommandName.FIND_USER_DETAILS,
                new FindUserDetails(getClientService(),
                        Logger.getLogger(FindUserDetails.class)));
        map.put(CommandName.EDIT_USER_DETAILS,
                new EditUserDetails(getClientService(),
                        Logger.getLogger(EditUserDetails.class)));

        map.put(CommandName.CLOSE_THERAPY,
                new CloseTherapy(getDoctorService(),
                        Logger.getLogger(CloseTherapy.class)));
        map.put(CommandName.DIAGNOSE_DISEASE,
                new DiagnoseDisease(getDoctorService(),
                        Logger.getLogger(DiagnoseDisease.class)));
        map.put(CommandName.FIND_OPEN_DOCTOR_THERAPIES,
                new FindOpenDoctorTherapies(getDoctorService(),
                        Logger.getLogger(FindOpenDoctorTherapies.class)));
        map.put(CommandName.FIND_PATIENT_THERAPIES,
                new FindPatientTherapies(getDoctorService(),
                        Logger.getLogger(FindPatientTherapies.class)));
        map.put(CommandName.MAKE_LAST_DIAGNOSIS_FINAL,
                new MakeLastDiagnosisFinal(getDoctorService(),
                        Logger.getLogger(MakeLastDiagnosisFinal.class)));
    }

    public HttpCommand getCommand(CommandName command) {
        return map.get(command);
    }

    private ClientService getClientService() {
        return new ClientServiceImpl(new UserDaoImpl(),
                new UserDetailsDaoImpl());
    }

    private ReceptionistService getReceptionistService() {
        return new ReceptionistServiceImpl(new UserDaoImpl());
    }

    private AdminHeadService getAdminHeadService() {
        return new AdminHeadServiceImpl(new UserDaoImpl(),
                new DepartmentDaoImpl(),
                new DepartmentStaffDaoImpl(),
                new ProceduresDaoImpl(),
                new MedicamentDaoImpl());
    }

    private DoctorService getDoctorService() {
        return new DoctorServiceImpl(new IcdDaoImpl(),
                new UserDaoImpl(),
                new TherapyDaoImpl(),
                new DiagnosisDaoImpl());
    }
}
