package epam.hospital.util;

import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import org.testng.annotations.DataProvider;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class Provider {

    @DataProvider
    public Object[][] getCorrectUser() {
        Map<String, Role> roles = new HashMap<>();
        roles.put(Role.CLIENT.name(), Role.CLIENT);
        return new Object[][]{{
                new User(0, "qwe", "qwe", roles,
                        new UserDetails("5f676a86c1dd18", 0, UserDetails.Gender.FEMALE,
                                "TEST", "TEST", "TEST",
                                new Date(2000, 1, 1), "TEST", "TEST"))
        }};
    }
}
