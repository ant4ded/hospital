package epam.hospital.controller.filter.validator;

import by.epam.hospital.controller.filter.validator.DataValidator;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Date;
import java.time.LocalDate;

public class DataValidatorTest {
    private DataValidator dataValidator;

    @BeforeMethod
    public void setUp() {
        dataValidator = new DataValidator();
    }

    @Test
    public void isValidBirthDate_validDayInJanuary_true() {
        Assert.assertTrue(dataValidator.isValidBirthDate("1965-01-31"));
    }

    @Test
    public void isValidBirthDate_validDayInDecember_true() {
        Assert.assertTrue(dataValidator.isValidBirthDate("1965-11-30"));
    }

    @Test
    public void isValidBirthDate_dataWithValidDayInNonLeapYear_true() {
        Assert.assertTrue(dataValidator.isValidBirthDate("2001-02-28"));
    }

    @Test
    public void isValidBirthDate_dataWithValidDayInLeapYear_true() {
        Assert.assertTrue(dataValidator.isValidBirthDate("2000-02-29"));
    }

    @Test
    public void isValidBirthDate_null_false() {
        Assert.assertFalse(dataValidator.isValidBirthDate(null));
    }

    @Test
    public void isValidBirthDate_withoutDash_false() {
        Assert.assertFalse(dataValidator.isValidBirthDate("19652021"));
    }

    @Test
    public void isValidBirthDate_dataWithInvalidYearLength_false() {
        Assert.assertFalse(dataValidator.isValidBirthDate("11965-10-21"));
    }

    @Test
    public void isValidBirthDate_dataWithInvalidMonthLength_false() {
        Assert.assertFalse(dataValidator.isValidBirthDate("1965-002-21"));
    }

    @Test
    public void isValidBirthDate_dataWithInvalidDayLength_false() {
        Assert.assertFalse(dataValidator.isValidBirthDate("1965-02-001"));
    }

    @Test
    public void isValidBirthDate_dataWithInvalidMonth_false() {
        Assert.assertFalse(dataValidator.isValidBirthDate("1965-20-21"));
    }

    @Test
    public void isValidBirthDate_dataWithInvalidDay_false() {
        Assert.assertFalse(dataValidator.isValidBirthDate("1965-10-41"));
    }

    @Test
    public void isValidBirthDate_dataWithInvalidDayInNonLeapYear_false() {
        Assert.assertFalse(dataValidator.isValidBirthDate("2001-02-29"));
    }

    @Test
    public void isValidBirthDate_dataWithInvalidDayInThirtyDayMonth_false() {
        Assert.assertFalse(dataValidator.isValidBirthDate("2001-04-31"));
    }

    @Test
    public void isValidBirthDate_birthdayTooOldDoctor_false() {
        final int TOO_OLD_AGE = 121;
        Date date = new Date(LocalDate.now().getYear() - TOO_OLD_AGE,
                LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
        Assert.assertFalse(dataValidator.isValidBirthDate(date.toString()));
    }
}
