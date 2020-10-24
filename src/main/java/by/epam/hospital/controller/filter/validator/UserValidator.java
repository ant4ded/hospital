package by.epam.hospital.controller.filter.validator;

public class UserValidator {
    private static final String PASSPORT_ID_REGEX = "[\\d\\p{L}]{14}";
    private static final String NAME_REGEX = "\\p{Lu}\\p{Ll}{2,14}";
    private static final String PHONE_REGEX = "\\d{12}";
    private static final String LOGIN_REGEX = "\\p{Ll}{3,15}-\\p{Ll}-\\p{Ll}{3,15}";

    public boolean isValidLogin(String login) {
        return login.matches(LOGIN_REGEX);
    }

    public boolean isValidPassportId(String passportId) {
        return passportId.matches(PASSPORT_ID_REGEX);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isValidFirstLastNameOrSurname(String name) {
        return name.matches(NAME_REGEX);
    }

    public boolean isValidDate(String s) {
        if (s == null) {
            throw new java.lang.IllegalArgumentException();
        }
        final int YEAR_LENGTH = 4;
        final int MONTH_LENGTH = 2;
        final int DAY_LENGTH = 2;
        final int MAX_MONTH = 12;
        final int MAX_DAY = 31;
        final int MAX_YEAR_BIRTH = 1998;
        final int MIN_YEAR_BIRTH = 1955;

        int firstDash = s.indexOf('-');
        int secondDash = s.indexOf('-', firstDash + 1);
        int len = s.length();

        if ((secondDash > 0) && (secondDash < len - 1) && firstDash == YEAR_LENGTH &&
                (secondDash - firstDash > 1 && secondDash - firstDash <= MONTH_LENGTH + 1) &&
                (len - secondDash > 1 && len - secondDash <= DAY_LENGTH + 1)) {
            int year = Integer.parseInt(s, 0, firstDash, 10);
            int month = Integer.parseInt(s, firstDash + 1, secondDash, 10);
            int day = Integer.parseInt(s, secondDash + 1, len, 10);

            return (month >= 1 && month <= MAX_MONTH) &&
                    (day >= 1 && day <= MAX_DAY) &&
                    (year <= MAX_YEAR_BIRTH && year >= MIN_YEAR_BIRTH);

        }
        return false;
    }

    public boolean isValidPhone(String phone) {
        return phone.matches(PHONE_REGEX);
    }
}
