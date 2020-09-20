package epam.hospital.data;

import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import org.testng.annotations.DataProvider;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

public class Provider {

    @DataProvider
    public Object[][] getCorrectUserAndUserDetails() {
        return new Object[][]{{
                new UserDetails("5f676a86c1dd18", 1, UserDetails.Gender.FEMALE,
                        "TEST", "TEST", "TEST",
                        new Date(2000, 1, 1), "TEST", "TEST")
        }};
    }

    @DataProvider
    public Object[][] getCorrectUser() {
        return new Object[][]{{
                new User(0, "qwe", "qwe", new ArrayList<Role>(Arrays.asList(new Role[]{Role.CLIENT})),
                        new UserDetails("5f676a86c1dd18", 1, UserDetails.Gender.FEMALE,
                                "TEST", "TEST", "TEST",
                                new Date(2000, 1, 1), "TEST", "TEST"))
        }};
    }
}
