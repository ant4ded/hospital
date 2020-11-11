package by.epam.hospital.entity;

public enum Role {
    ADMIN_HEAD(1),
    ADMIN(2),
    RECEPTIONIST(3),
    DOCTOR(4),
    DEPARTMENT_HEAD(5),
    CLIENT(6),
    MEDICAL_ASSISTANT(7);

    public final int id;

    Role(int id) {
        this.id = id;
    }

    public static boolean hasValue(String value) {
        if (value != null) {
            for (Role v : Role.values()) {
                if (v.name().equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }
}
