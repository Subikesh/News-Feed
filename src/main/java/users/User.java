package users;

import newsfeed.*;

import java.io.IOException;

public class User {
    private String username;
    private String password;

    public static User currUser = new User("guest", "guest");

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
}
