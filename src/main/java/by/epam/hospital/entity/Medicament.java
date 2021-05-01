package by.epam.hospital.entity;

import java.util.StringJoiner;

public class Medicament {
    private int id;
    private String name;
    private boolean isEnabled;

    public Medicament() {
    }

    public Medicament(String name) {
        this.name = name;
    }

    public Medicament(String name, boolean isEnabled) {
        this.name = name;
        this.isEnabled = isEnabled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Medicament that = (Medicament) o;

        if (id != that.id) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Medicament.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("isEnabled=" + isEnabled)
                .toString();
    }
}
