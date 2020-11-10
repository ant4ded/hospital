package by.epam.hospital.controller.filter.validator;

import java.time.LocalDate;

public class UserValidator {
    private static final String PASSPORT_ID_REGEX = "[\\d\\p{L}]{14}";
    private static final String NAME_REGEX = "\\p{Lu}\\p{Ll}{2,14}";
    private static final String PHONE_REGEX = "\\d{12}";
    private static final String LOGIN_REGEX = "\\p{Ll}{3,15}_\\p{Ll}_\\p{Ll}{3,15}";
    private static final String ICD_CODE_REGEX = "[\\d-\\p{Lu}]{7}";

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
        final int MULTIPLICITY_LEAP_YEAR = 4;
        final int MONTH_LENGTH = 2;
        final int DAY_LENGTH = 2;
        final int MAX_MONTH = 12;
        final int DEFAULT_DAY = 30;
        final int MAX_DAY = 31;
        final int MAX_AGE = 120;
        final int LEAP_YEAR_DAY = 29;
        final int NON_LEAP_YEAR_DAY = 28;
        final int MIN_YEAR_BIRTH = LocalDate.now().getYear() - MAX_AGE;

        int firstDashPosition = s.indexOf('-');
        int secondDashPosition = s.indexOf('-', firstDashPosition + 1);
        int len = s.length();

        boolean isValidYearLength = (secondDashPosition > 0) && (secondDashPosition < len - 1) &&
                firstDashPosition == YEAR_LENGTH;
        boolean isValidMonthLength = (secondDashPosition - firstDashPosition > 1 &&
                secondDashPosition - firstDashPosition <= MONTH_LENGTH + 1);
        boolean isValidDayLength = (len - secondDashPosition > 1 && len - secondDashPosition <= DAY_LENGTH + 1);

        if (isValidYearLength && isValidMonthLength && isValidDayLength) {
            int year = Integer.parseInt(s, 0, firstDashPosition, 10);
            int month = Integer.parseInt(s, firstDashPosition + 1, secondDashPosition, 10);
            int day = Integer.parseInt(s, secondDashPosition + 1, len, 10);

            boolean isValidMonth = (month >= 1 && month <= MAX_MONTH);
            boolean isValidYear = year >= MIN_YEAR_BIRTH && year <= LocalDate.now().getYear();

            boolean isValidDaysInThirtyDayMonth = (month == 4 || month == 6 || month == 9 || month == 11)
                    && day <= DEFAULT_DAY;
            boolean isValidDaysInThirtyOneDayMonth = (month == 1 || month == 3 || month == 5 || month == 7 ||
                    month == 8 || month == 10 || month == 12) && day <= MAX_DAY;
            boolean isValidDaysInFebruary = month == 2 &&
                    (day <= NON_LEAP_YEAR_DAY || (day == LEAP_YEAR_DAY && year % MULTIPLICITY_LEAP_YEAR == 0));

            result = isValidMonth && isValidYear &&
                    (isValidDaysInThirtyDayMonth || isValidDaysInThirtyOneDayMonth || isValidDaysInFebruary);
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
