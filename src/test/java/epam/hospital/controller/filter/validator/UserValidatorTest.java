package epam.hospital.controller.filter.validator;

import by.epam.hospital.controller.filter.validator.UserValidator;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UserValidatorTest {
    private UserValidator userValidator;

    @BeforeMethod
    public void setUp() {
        userValidator = new UserValidator();
    }

    @Test
    public void isValidDate() {
        String validData = "1965-10-21";
        String dataWithInvalidYear = "11965-10-21";
        String dataWithInvalidMonth = "1965-20-21";
        String dataWithInvalidDay = "1965-10-41";
        String birthdayTooYoungDoctor = "2000-10-41";
        String birthdayTooOldDoctor = "1945-10-41";

        if (!userValidator.isValidDate(validData)) {
            Assert.fail("IsValidDate failed.");
        }
        if (userValidator.isValidDate(dataWithInvalidYear)) {
            Assert.fail("IsValidDate failed.");
        }
        if (userValidator.isValidDate(dataWithInvalidMonth)) {
            Assert.fail("IsValidDate failed.");
        }
        if (userValidator.isValidDate(dataWithInvalidDay)) {
            Assert.fail("IsValidDate failed.");
        }
        if (userValidator.isValidDate(birthdayTooYoungDoctor)) {
            Assert.fail("IsValidDate failed.");
        }
        if (userValidator.isValidDate(birthdayTooOldDoctor)) {
            Assert.fail("IsValidDate failed.");
        }
    }
}
