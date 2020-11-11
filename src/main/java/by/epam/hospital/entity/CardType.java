package by.epam.hospital.entity;

public enum CardType {
    STATIONARY, AMBULATORY;

    public static boolean hasValue(String value) {
        if (value != null) {
            for (CardType v : CardType.values()) {
                if (v.name().equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }
}
