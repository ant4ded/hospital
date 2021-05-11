package by.epam.hospital.entity;

import by.epam.hospital.service.util.JsonConverter;

import java.io.Serializable;

public class Procedure implements Serializable {
    private int id;
    private String name;
    private int cost;
    private boolean isEnabled;

    public Procedure(String name) {
        this.name = name;
    }

    public Procedure() {
    }

    public Procedure(String name, int cost, boolean isEnabled) {
        this.name = name;
        this.cost = cost;
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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
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

        Procedure procedure = (Procedure) o;

        if (id != procedure.id) return false;
        if (cost != procedure.cost) return false;
        return name.equals(procedure.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + cost;
        return result;
    }

    @Override
    public String toString() {
        return JsonConverter.toJson(this);
    }
}
