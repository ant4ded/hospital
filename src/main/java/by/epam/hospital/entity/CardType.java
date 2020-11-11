package by.epam.hospital.entity;

public enum CardType {
    STATIONARY, AMBULATORY;

    public static boolean isStationary(String card) {
        boolean result = false;
        if (card != null) {
            result = card.equals(STATIONARY.toString());
        }
        return result;
    }

    public static boolean isAmbulatory(String card) {
        boolean result = false;
        if (card != null) {
            result = card.equals(AMBULATORY.toString());
        }
        return result;
    }
}
