package by.epam.hospital.entity;

public enum  Department {
    INFECTIOUS(1),
    CARDIOLOGY(2),
    NEUROLOGY(3),
    OTORHINOLARYNGOLOGY(4),
    PEDIATRIC(5),
    THERAPEUTIC(6),
    UROLOGY(7),
    TRAUMATOLOGY(8),
    SURGERY(9);

    public final int ID;

    Department(int departmentId) {
        this.ID = departmentId;
    }
}
