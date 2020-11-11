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

    public static boolean hasRole(String role) {
        for (Role value : Role.values()) {
            if (value.name().equals(role)) {
                return true;
            }
        }
        return false;
    }
}
