package users;

import newsfeed.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

/**
 * User class contains the details of an user
 * This class is serialized in the Users details file.
 */
public class User implements Serializable {
    private String username;
    private String password;

    public User() {
        try {
            System.out.println("Enter username :");
            username = Globals.input.readLine();
            System.out.println("Enter password :");
            password = Globals.input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
