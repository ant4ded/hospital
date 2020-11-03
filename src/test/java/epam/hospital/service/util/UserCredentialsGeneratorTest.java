package epam.hospital.service.util;

import by.epam.hospital.service.util.UserCredentialsGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserCredentialsGeneratorTest {
    @Test
    public void generateLogin() {
        String firstName = "Anton";
        String surname = "Dedik";
        String lastName = "Andreevich";

        String expected = "anton-d-andreevich";
        String actual = UserCredentialsGenerator.generateLogin(firstName, surname, lastName);

        Assert.assertEquals(actual, expected);
    }
}
