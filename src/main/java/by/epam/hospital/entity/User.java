package by.epam.hospital.entity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class User {
    private int id;
    private String login;
    private String password;

    private Map<String,Role> roles;
    private UserDetails userDetails;

    public User() {
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(int id, String login, String password, Map<String,Role> roles, UserDetails userDetails) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.roles = roles;
        this.userDetails = userDetails;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String,Role> getRoles() {
        return Collections.unmodifiableMap(roles);
    }

    public void setRoles(Map<String,Role> roles) {
        this.roles = roles;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!login.equals(user.login)) return false;
        return password.equals(user.password);
    }

    @Override
    public int hashCode() {
        int result = login.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("login='" + login + "'")
                .add("password='" + password + "'")
                .add("roles=" + roles)
                .add("userDetails=" + userDetails)
                .toString();
    }
}
