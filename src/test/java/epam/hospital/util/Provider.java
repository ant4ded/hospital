package epam.hospital.util;

import by.epam.hospital.entity.*;
import org.testng.annotations.DataProvider;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class Provider {
    private static final String STRING_VALUE = "TEST";
    private static final String SPACE = " ";

    @DataProvider
    public Object[][] getCorrectUser() {
        return new Object[][]{{
                new User(0, STRING_VALUE, STRING_VALUE, getUserRoles(),
                        getUserDetails(STRING_VALUE))
        }};
    }

    @DataProvider
    public Object[][] getCorrectDoctorAndPatient() {
        return new Object[][]{{
                new User(0, STRING_VALUE, STRING_VALUE, getUserRoles(),
                        getUserDetails(STRING_VALUE)),
                new User(0, STRING_VALUE + STRING_VALUE, STRING_VALUE + STRING_VALUE, getUserRoles(),
                        getUserDetails(STRING_VALUE + STRING_VALUE))
        }};
    }

    @DataProvider
    public Object[][] getCorrectDiagnosisAndPatient() {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setId(0);
        diagnosis.setIcd(getIcd());

        ArrayList<Role> doctorRoles = getUserRoles();
        doctorRoles.add(Role.DOCTOR);
        diagnosis.setDoctor(new User(0, STRING_VALUE, STRING_VALUE, doctorRoles, getUserDetails(STRING_VALUE)));
        diagnosis.setDiagnosisDate(Date.valueOf(LocalDate.now()));
        diagnosis.setReason(STRING_VALUE);

        User user = new User(0, STRING_VALUE + STRING_VALUE, STRING_VALUE + STRING_VALUE,
                getUserRoles(), getUserDetails(STRING_VALUE + STRING_VALUE));
        return new Object[][]{{
                diagnosis, user
        }};
    }

    private ArrayList<Role> getUserRoles() {
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.CLIENT);
        return roles;
    }

    private UserDetails getUserDetails(String parameter) {
        UserDetails userDetails = new UserDetails();
        userDetails.setPassportId(parameter);
        userDetails.setUserId(0);
        userDetails.setGender(UserDetails.Gender.FEMALE);
        userDetails.setFirstName(parameter);
        userDetails.setSurname(parameter);
        userDetails.setLastName(parameter);
        userDetails.setBirthday(new Date(1990, 1, 1));
        userDetails.setAddress(parameter);
        userDetails.setPhone(parameter);
        return userDetails;
    }

    private Icd getIcd() {
        return new Icd(1, "0016070",
                "Bypass Cerebral Ventricle to Nasopharynx with Autologous Tissue Substitute, Open Approach");
    }

    @DataProvider
    public Object[][] getCorrectProcedure() {
        return new Object[][]{{
                new Procedure(STRING_VALUE)
        }};
    }

    @DataProvider
    public Object[][] getCorrectProcedures() {
        Object[][] objects = new Object[1][10];
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                objects[0][i] = new Procedure(i + SPACE + STRING_VALUE + " two ");
            }
            if (i % 2 != 0) {
                objects[0][i] = new Procedure(i + SPACE + STRING_VALUE + " not two ");
            }
            if (i % 3 == 0) {
                objects[0][i] = new Procedure(i + SPACE + STRING_VALUE + " three ");
            }
        }
        return objects;
    }

    @DataProvider
    public Object[][] getCorrectMedication() {
        return new Object[][]{{
                new Medication(STRING_VALUE)
        }};
    }

    @DataProvider
    public Object[][] getCorrectMedications() {
        Object[][] objects = new Object[1][10];
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                objects[0][i] = new Medication(i + SPACE + STRING_VALUE + " two ");
            }
            if (i % 2 != 0) {
                objects[0][i] = new Medication(i + SPACE + STRING_VALUE + " not two ");
            }
            if (i % 3 == 0) {
                objects[0][i] = new Medication(i + SPACE + STRING_VALUE + " three ");
            }
        }
        return objects;
    }
}
