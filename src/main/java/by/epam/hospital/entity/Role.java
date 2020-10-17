package by.epam.hospital.entity;

public enum Role {
    ADMIN_HEAD(1),
    ADMIN(2),
    RECEPTIONIST(3),
    DOCTOR(4),
    DEPARTMENT_HEAD(5),
    CLIENT(6),
    MEDICAL_ASSISTANT(7);

    public int ID;

    Role(int ID) {
        this.ID = ID;
    }
}
