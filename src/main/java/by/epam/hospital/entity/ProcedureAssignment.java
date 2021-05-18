package by.epam.hospital.entity;

import by.epam.hospital.service.util.JsonConverter;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ProcedureAssignment implements Serializable {
    private Procedure procedure;
    private String description;
    private LocalDateTime time;

    public ProcedureAssignment() {
    }

    public ProcedureAssignment(Procedure procedure, String description, LocalDateTime time) {
        this.procedure = procedure;
        this.description = description;
        this.time = time;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
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

        ProcedureAssignment that = (ProcedureAssignment) o;

        if (!procedure.equals(that.procedure)) return false;
        if (!description.equals(that.description)) return false;
        return time.equals(that.time);
    }

    @Override
    public int hashCode() {
        int result = procedure.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + time.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return JsonConverter.toJson(this);
    }
}
