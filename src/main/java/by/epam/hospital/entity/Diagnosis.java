package by.epam.hospital.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.StringJoiner;

public class Diagnosis implements Serializable {
    private int id;
    private Icd icd;
    private User doctor;
    private Date diagnosisDate;
    private String reason;
    private transient List<ProcedureAssignment> assignmentProcedures;
    private transient List<MedicamentAssignment> assignmentMedications;

    public Diagnosis() {
    }

    public Diagnosis(int id, Icd icd, User doctor, Date diagnosisDate, String reason) {
        this.id = id;
        this.icd = icd;
        this.doctor = doctor;
        this.diagnosisDate = diagnosisDate;
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Icd getIcd() {
        return icd;
    }

    public void setIcd(Icd icd) {
        this.icd = icd;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public Date getDiagnosisDate() {
        return diagnosisDate;
    }

    public void setDiagnosisDate(Date diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setAssignmentProcedures(List<ProcedureAssignment> assignmentProcedures) {
        this.assignmentProcedures = assignmentProcedures;
    }

    public List<ProcedureAssignment> getAssignmentProcedures() {
        return assignmentProcedures;
    }

    public void setAssignmentMedications(List<MedicamentAssignment> assignmentMedications) {
        this.assignmentMedications = assignmentMedications;
    }

    public List<MedicamentAssignment> getAssignmentMedications() {
        return assignmentMedications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Diagnosis diagnosis = (Diagnosis) o;

        if (id != diagnosis.id) return false;
        if (!icd.equals(diagnosis.icd)) return false;
        if (!doctor.equals(diagnosis.doctor)) return false;
        if (!diagnosisDate.equals(diagnosis.diagnosisDate)) return false;
        return reason != null ? reason.equals(diagnosis.reason) : diagnosis.reason == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + icd.hashCode();
        result = 31 * result + doctor.hashCode();
        result = 31 * result + diagnosisDate.hashCode();
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Diagnosis.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("icd=" + icd)
                .add("doctor=" + doctor)
                .add("diagnosisDate=" + diagnosisDate)
                .add("reason='" + reason + "'")
                .add("assignmentProcedures=" + assignmentProcedures)
                .add("assignmentMedications=" + assignmentMedications)
                .toString();
    }
}
