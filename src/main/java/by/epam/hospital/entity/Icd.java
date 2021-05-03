package by.epam.hospital.entity;

import java.io.Serializable;
import java.util.StringJoiner;

public class Icd implements Serializable {
    private int id;
    private String code;
    private String title;

    public Icd() {
    }

    public Icd(int id, String code, String title) {
        this.id = id;
        this.code = code;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Icd icd = (Icd) o;

        if (id != icd.id) return false;
        if (code != null ? !code.equals(icd.code) : icd.code != null) return false;
        return title != null ? title.equals(icd.title) : icd.title == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Icd.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("code='" + code + "'")
                .add("title='" + title + "'")
                .toString();
    }
}
