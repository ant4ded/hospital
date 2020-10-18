package epam.hospital.util;

import by.epam.hospital.entity.Role;
import by.epam.hospital.entity.User;
import by.epam.hospital.entity.UserDetails;
import org.testng.annotations.DataProvider;

import java.sql.Date;
import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class Provider {
    private static final String STRING_VALUE = "TEST";

    @DataProvider
    public Object[][] getCorrectUser() {
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.CLIENT);
        UserDetails userDetails = new UserDetails();
        userDetails.setPassportId("5f676a86c1dd18");
        userDetails.setUserId(0);
        userDetails.setGender(UserDetails.Gender.FEMALE);
        userDetails.setFirstName(STRING_VALUE);
        userDetails.setSurname(STRING_VALUE);
        userDetails.setLastName(STRING_VALUE);
        userDetails.setBirthday(new Date(2000, 1, 1));
        userDetails.setAddress(STRING_VALUE);
        userDetails.setPhone(STRING_VALUE);

        return new Object[][]{{
                new User(0, "qwe", "qwe", roles, userDetails)
        }};
    }
}
