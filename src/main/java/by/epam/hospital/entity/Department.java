package by.epam.hospital.entity;

public enum Department {
    INFECTIOUS(1),
    CARDIOLOGY(2),
    NEUROLOGY(3),
    OTORHINOLARYNGOLOGY(4),
    PEDIATRIC(5),
    THERAPEUTIC(6),
    UROLOGY(7),
    TRAUMATOLOGY(8),
    SURGERY(9);

    public final int id;

    Department(int departmentId) {
        this.id = departmentId;
    }

    public static boolean hasDepartment(String department) {
        if (department != null) {
            for (Department value : Department.values()) {
                if (value.name().equals(department)) {
                    return true;
                }
            }
        }
        return false;
    }
}
