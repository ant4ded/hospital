package by.epam.hospital.service;

import by.epam.hospital.entity.*;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    Optional<User> findPatientByUserDetails(UserDetails userDetails) throws ServiceException;

    Optional<Therapy> findCurrentPatientTherapy(String doctorLogin, String patientLogin,
                                                CardType cardType) throws ServiceException;

    boolean diagnoseDisease(String icdCode, String reason, String doctorLogin,
                            String patientLogin, CardType cardType) throws ServiceException;

    List<Therapy> findPatientTherapies(UserDetails userDetails, CardType cardType) throws ServiceException;

    List<Therapy> findOpenDoctorTherapies(String doctorLogin, CardType cardType) throws ServiceException;

    boolean makeLastDiagnosisFinal(String doctorLogin, String patientLogin, CardType cardType) throws ServiceException;

    boolean closeTherapy(String doctorLogin, String patientLogin, CardType cardType) throws ServiceException;

    boolean assignProcedureToLastDiagnosis(ProcedureAssignment assignment, String doctorLogin, String patientLogin, CardType cardType) throws ServiceException;

    boolean assignMedicamentToLastDiagnosis(MedicamentAssignment assignment, String doctorLogin, String patientLogin, CardType cardType) throws ServiceException;

    List<ProcedureAssignment> findAllAssignmentProceduresToDiagnosis(int diagnosisId) throws ServiceException;

    List<MedicamentAssignment> findAllAssignmentMedicationsToDiagnosis(int diagnosisId) throws ServiceException;

    PageResult<Procedure> findAllProceduresByNamePartPaging(String namePart, int page) throws ServiceException;

    PageResult<Medicament> findAllMedicationsByNamePartPaging(String namePart, int page) throws ServiceException;
}
