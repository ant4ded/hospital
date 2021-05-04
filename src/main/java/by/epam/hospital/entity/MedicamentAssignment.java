package by.epam.hospital.entity;

import java.time.LocalDateTime;
import java.util.StringJoiner;

public class MedicamentAssignment {
    private Medicament medicament;
    private String description;
    private LocalDateTime time;

    public MedicamentAssignment() {
    }

    public MedicamentAssignment(Medicament medicament, String description, LocalDateTime time) {
        this.medicament = medicament;
        this.description = description;
        this.time = time;
    }

    public Medicament getMedicament() {
        return medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MedicamentAssignment that = (MedicamentAssignment) o;

        if (!medicament.equals(that.medicament)) return false;
        if (!description.equals(that.description)) return false;
        return time.equals(that.time);
    }

    @Override
    public int hashCode() {
        int result = medicament.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + time.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MedicamentAssignment.class.getSimpleName() + "[", "]")
                .add("medicament=" + medicament)
                .add("description='" + description + "'")
                .add("time=" + time)
                .toString();
    }
}
