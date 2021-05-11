package by.epam.hospital.entity;

import by.epam.hospital.service.util.JsonConverter;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MedicamentAssignment implements Serializable {
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
        return JsonConverter.toJson(this);
    }
}
