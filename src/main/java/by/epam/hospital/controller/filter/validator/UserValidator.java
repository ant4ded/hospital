package by.epam.hospital.controller.filter.validator;

import java.time.LocalDate;

public class UserValidator {
    private static final String PASSPORT_ID_REGEX = "[\\d\\p{L}]{14}";
    private static final String NAME_REGEX = "\\p{Lu}\\p{Ll}{2,14}";
    private static final String PHONE_REGEX = "\\d{12}";
    private static final String LOGIN_REGEX = "\\p{Ll}{3,15}_\\p{Ll}_\\p{Ll}{3,15}";
    private static final String ICD_CODE_REGEX = "[\\d-\\p{Lu}]{7}";

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isValidLogin(String login) {
        return login.matches(LOGIN_REGEX);
    }

    public boolean isValidPassportId(String passportId) {
        return passportId.matches(PASSPORT_ID_REGEX);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isValidName(String name) {
        return name.matches(NAME_REGEX);
    }

    public boolean isValidBirthDate(String s) {
        boolean result = false;
        final int YEAR_LENGTH = 4;
        final int MONTH_LENGTH = 2;
        final int DAY_LENGTH = 2;
        final int MAX_MONTH = 12;
        final int DEFAULT_DAY = 30;
        final int MAX_DAY = 31;
        final int MAX_AGE = 120;
        final int LEAP_YEAR_DAY = 29;
        final int NON_LEAP_YEAR_DAY = 28;
        final int MIN_YEAR_BIRTH = LocalDate.now().getYear() - MAX_AGE;

        int firstDash = s.indexOf('-');
        int secondDash = s.indexOf('-', firstDash + 1);
        int len = s.length();

        if ((secondDash > 0) && (secondDash < len - 1) && firstDash == YEAR_LENGTH &&
                (secondDash - firstDash > 1 && secondDash - firstDash <= MONTH_LENGTH + 1) &&
                (len - secondDash > 1 && len - secondDash <= DAY_LENGTH + 1)) {
            int year = Integer.parseInt(s, 0, firstDash, 10);
            int month = Integer.parseInt(s, firstDash + 1, secondDash, 10);
            int day = Integer.parseInt(s, secondDash + 1, len, 10);
            if ((month >= 1 && month <= MAX_MONTH) &&
                    year >= MIN_YEAR_BIRTH && year <= LocalDate.now().getYear()) {
                if ((month == 4 || month == 6 || month == 9 || month == 11) && day <= DEFAULT_DAY) {
                    result = true;
                }
                if ((month == 1 || month == 3 || month == 5 || month == 7 ||
                        month == 8 || month == 10 || month == 12) && day <= MAX_DAY) {
                    result = true;
                }
                boolean isValidFebruaryDay = day <= NON_LEAP_YEAR_DAY || (day == LEAP_YEAR_DAY && year % 4 == 0);
                if (month == 2 && isValidFebruaryDay) {
                    result = true;
                }
            }
        }
        return result;
    }

    public boolean isValidPhone(String phone) {
        return phone.matches(PHONE_REGEX);
    }

    public boolean isValidIcdCode(String icdCode) {
        return icdCode.matches(ICD_CODE_REGEX);
    }
}
