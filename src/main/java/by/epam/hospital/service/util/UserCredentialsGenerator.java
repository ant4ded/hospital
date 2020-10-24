package by.epam.hospital.service.util;

public class UserCredentialsGenerator {
    public static String generateLogin(String firstName, String surname, String lastName) {
        return firstName.toLowerCase() + '-' + surname.toLowerCase().charAt(0) + '-' + lastName.toLowerCase();
    }

    public static String generatePassword() {
        // TODO: 23.10.2020 generate user password
        return null;
    }
}
