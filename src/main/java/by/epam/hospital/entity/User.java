package by.epam.hospital.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class User implements Serializable {
    private int id;
    private String login;
    private String password;

    private ArrayList<Role> roles;
    private UserDetails userDetails;

    public User() {
        roles = new ArrayList<>();
        userDetails = new UserDetails();
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(int id, String login, String password, ArrayList<Role> roles, UserDetails userDetails) {
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

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = new ArrayList<>(roles);
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
