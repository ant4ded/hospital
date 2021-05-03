package by.epam.hospital.entity;


import java.io.Serializable;
import java.sql.Date;
import java.util.*;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class Therapy implements Serializable {
    private int id;
    private User doctor;
    private User patient;
    private CardType cardType;
    private transient Optional<Date> endTherapy = Optional.empty();
    private transient Optional<Diagnosis> finalDiagnosis = Optional.empty();
    private List<Diagnosis> diagnoses = Collections.emptyList();

    public Therapy() {
    }

    public Therapy(int id, User doctor, User patient, CardType cardType, Optional<Date> endTherapy,
                   Optional<Diagnosis> finalDiagnosis, List<Diagnosis> diagnoses) {
        this.id = id;
        this.doctor = doctor;
        this.patient = patient;
        this.cardType = cardType;
        this.endTherapy = endTherapy;
        this.finalDiagnosis = finalDiagnosis;
        this.diagnoses = diagnoses;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public Optional<Date> getEndTherapy() {
        return endTherapy;
    }

    public void setEndTherapy(Date endTherapy) {
        this.endTherapy = Optional.ofNullable(endTherapy);
    }

    public Optional<Diagnosis> getFinalDiagnosis() {
        return finalDiagnosis;
    }

    public void setFinalDiagnosis(Diagnosis finalDiagnosis) {
        this.finalDiagnosis = Optional.ofNullable(finalDiagnosis);
    }

    public List<Diagnosis> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(List<Diagnosis> diagnoses) {
        this.diagnoses = diagnoses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Therapy therapy = (Therapy) o;

        if (id != therapy.id) return false;
        if (!doctor.equals(therapy.doctor)) return false;
        if (!patient.equals(therapy.patient)) return false;
        if (cardType != therapy.cardType) return false;
        if (endTherapy.isPresent() ?
                !endTherapy.equals(therapy.endTherapy) :
                therapy.endTherapy.isPresent()) return false;
        if (finalDiagnosis.isPresent() ?
                !finalDiagnosis.equals(therapy.finalDiagnosis) :
                therapy.finalDiagnosis.isPresent())
            return false;
        return diagnoses.equals(therapy.diagnoses);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + doctor.hashCode();
        result = 31 * result + patient.hashCode();
        result = 31 * result + cardType.hashCode();
        result = 31 * result + (endTherapy.isPresent() ? endTherapy.hashCode() : 0);
        result = 31 * result + (finalDiagnosis.isPresent() ? finalDiagnosis.hashCode() : 0);
        result = 31 * result + diagnoses.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Therapy.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("doctor=" + doctor)
                .add("patient=" + patient)
                .add("cardType=" + cardType)
                .add("endTherapy=" + endTherapy)
                .add("finalDiagnosis=" + finalDiagnosis)
                .add("diagnoses=" + diagnoses)
                .toString();
    }
}
