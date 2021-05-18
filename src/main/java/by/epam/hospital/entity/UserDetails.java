package by.epam.hospital.entity;

import by.epam.hospital.service.util.JsonConverter;

import java.io.Serializable;
import java.sql.Date;

public class UserDetails implements Serializable {
    private String passportId;
    private int userId;
    private Gender gender;
    private String firstName;
    private String surname;
    private String lastName;
    private Date birthday;
    private String address;
    private String phone;

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDetails that = (UserDetails) o;

        if (userId != that.userId) return false;
        if (!firstName.equals(that.firstName)) return false;
        if (!surname.equals(that.surname)) return false;
        if (!lastName.equals(that.lastName)) return false;
        return birthday.equals(that.birthday);
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + firstName.hashCode();
        result = 31 * result + surname.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + birthday.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return JsonConverter.toJson(this);
    }

    public enum Gender {
        MALE, FEMALE;

        public static boolean hasValue(String value) {
            if (value != null) {
                for (Gender v : Gender.values()) {
                    if (v.name().equals(value)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
