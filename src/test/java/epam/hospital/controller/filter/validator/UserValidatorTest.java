package epam.hospital.controller.filter.validator;

import by.epam.hospital.controller.filter.validator.UserValidator;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Date;
import java.time.LocalDate;

public class UserValidatorTest {
    private UserValidator userValidator;

    @BeforeMethod
    public void setUp() {
        userValidator = new UserValidator();
    }

    @Test
    public void isValidBirthDate_validDayInJanuary_true() {
        Assert.assertTrue(userValidator.isValidBirthDate("1965-01-31"));
    }

    @Test
    public void isValidBirthDate_validDayInDecember_true() {
        Assert.assertTrue(userValidator.isValidBirthDate("1965-11-30"));
    }

    @Test
    public void isValidBirthDate_dataWithValidDayInNonLeapYear_true() {
        Assert.assertTrue(userValidator.isValidBirthDate("2001-02-28"));
    }

    @Test
    public void isValidBirthDate_dataWithValidDayInLeapYear_true() {
        Assert.assertTrue(userValidator.isValidBirthDate("2000-02-29"));
    }

    @Test
    public void isValidBirthDate_dataWithInvalidYear_false() {
        Assert.assertFalse(userValidator.isValidBirthDate("11965-10-21"));
    }

    @Test
    public void isValidBirthDate_dataWithInvalidMonth_false() {
        Assert.assertFalse(userValidator.isValidBirthDate("1965-20-21"));
    }

    @Test
    public void isValidBirthDate_dataWithInvalidDay_false() {
        Assert.assertFalse(userValidator.isValidBirthDate("1965-10-41"));
    }

    @Test
    public void isValidBirthDate_dataWithInvalidDayInNonLeapYear_false() {
        Assert.assertFalse(userValidator.isValidBirthDate("2001-02-29"));
    }

    @Test
    public void isValidBirthDate_birthdayTooOldDoctor_false() {
        final int TOO_OLD_AGE = 121;
        Date date = new Date(LocalDate.now().getYear() - TOO_OLD_AGE,
                LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
        Assert.assertFalse(userValidator.isValidBirthDate(date.toString()));
    }
}
