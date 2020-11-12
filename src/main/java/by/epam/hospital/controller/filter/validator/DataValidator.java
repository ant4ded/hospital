package by.epam.hospital.controller.filter.validator;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;

public class DataValidator {
    private static final String PASSPORT_ID_REGEX = "[\\d\\p{L}]{14}";
    private static final String NAME_REGEX = "\\p{Lu}\\p{Ll}{2,14}";
    private static final String PHONE_REGEX = "\\d{12}";
    private static final String LOGIN_REGEX = "\\p{Ll}{3,15}_\\p{Ll}_\\p{Ll}{3,15}";
    private static final String ICD_CODE_REGEX = "[\\d-\\p{Lu}]{7}";
    private static final int YEAR_LENGTH = 4;
    private static final int MULTIPLICITY_LEAP_YEAR = 4;
    private static final int MONTH_LENGTH = 2;
    private static final int DAY_LENGTH = 2;
    private static final int DEFAULT_DAY = 30;
    private static final int MAX_DAY = 31;
    private static final int MAX_AGE = 120;
    private static final int LEAP_YEAR_DAY = 29;
    private static final int NON_LEAP_YEAR_DAY = 28;

    private final int minYearBirth = LocalDate.now().getYear() - MAX_AGE;

    public boolean isValidLogin(String login) {
        boolean result = false;
        if (login != null) {
            result = login.matches(LOGIN_REGEX);
        }
        return result;
    }

    public boolean isValidPassportId(String passportId) {
        boolean result = false;
        if (passportId != null) {
            result = passportId.matches(PASSPORT_ID_REGEX);
        }
        return result;
    }

    public boolean isValidName(String name) {
        boolean result = false;
        if (name != null) {
            result = name.matches(NAME_REGEX);
        }
        return result;
    }

    public boolean isValidDate(String date) {
        boolean result = false;
        if (date != null) {
            int firstDashPosition = date.indexOf('-');
            int secondDashPosition = date.indexOf('-', firstDashPosition + 1);

            boolean isValidYearLength = (secondDashPosition > 0) && (secondDashPosition < date.length() - 1) &&
                    firstDashPosition == YEAR_LENGTH;
            boolean isValidMonthLength = (secondDashPosition - firstDashPosition > 1 &&
                    secondDashPosition - firstDashPosition <= MONTH_LENGTH + 1);
            boolean isValidDayLength = (date.length() - secondDashPosition > 1 &&
                    date.length() - secondDashPosition <= DAY_LENGTH + 1);

            if (isValidYearLength && isValidMonthLength && isValidDayLength) {
                int year = Integer.parseInt(date, 0, firstDashPosition, 10);
                int month = Integer.parseInt(date, firstDashPosition + 1, secondDashPosition, 10);
                int day = Integer.parseInt(date, secondDashPosition + 1, date.length(), 10);

                boolean isValidMonth = (month >= Month.JANUARY.getValue() && month <= Month.DECEMBER.getValue());

                boolean isValidDaysInThirtyDayMonth = (month == Month.APRIL.getValue() ||
                        month == Month.JUNE.getValue() || month == Month.SEPTEMBER.getValue() ||
                        month == Month.NOVEMBER.getValue()) && day <= DEFAULT_DAY;
                boolean isValidDaysInThirtyOneDayMonth = (month == Month.JANUARY.getValue() ||
                        month == Month.MARCH.getValue() || month == Month.MAY.getValue() ||
                        month == Month.JULY.getValue() || month == Month.AUGUST.getValue() ||
                        month == Month.OCTOBER.getValue() ||
                        month == Month.DECEMBER.getValue()) && day <= MAX_DAY;
                boolean isValidDaysInFebruary = month == Month.FEBRUARY.getValue() &&
                        (day <= NON_LEAP_YEAR_DAY || (day == LEAP_YEAR_DAY && year % MULTIPLICITY_LEAP_YEAR == 0));

                result = isValidMonth && (isValidDaysInThirtyDayMonth || isValidDaysInThirtyOneDayMonth ||
                        isValidDaysInFebruary);
            }
        }
        return result;
    }

    public boolean isValidBirthDate(String s) {
        boolean result = false;
        if (isValidDate(s)) {
            Date date = Date.valueOf(s);
            int year = date.toLocalDate().getYear();
            result = year >= minYearBirth && year <= LocalDate.now().getYear();
        }
        return result;
    }

    public boolean isValidPhone(String phone) {
        boolean result = false;
        if (phone != null) {
            result = phone.matches(PHONE_REGEX);
        }
        return result;
    }

    public boolean isValidIcdCode(String icdCode) {
        boolean result = false;
        if (icdCode != null) {
            result = icdCode.matches(ICD_CODE_REGEX);
        }
        return result;
    }
}
