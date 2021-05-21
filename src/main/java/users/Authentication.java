package users;

import utilities.Globals;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class contains static methods and session variables related to authenticating user
 * If currUser is equal to guestUser, then user is not authenticated.
 */
public class Authentication {
    public final User guestUser = new User("_guest_", "_guest_");
    public User currUser = guestUser;
    final int MAX_USERS = 15;
    public static File f = new File(Globals.USER_FILE);
    // User list is stored so in queue. If more than 15 users come, the oldest registered user is popped
    private Queue<User> registeredUsers;

    public Authentication() {
        User user;
        registeredUsers = new LinkedList<>();
        // Create new file if doesnt exist
        try {
            f.createNewFile();
        } catch (Exception e) {}

        if (f.length() != 0) {
            try {
                FileInputStream file = null;
                file = new FileInputStream(Globals.USER_FILE);
                ObjectInputStream objRead = new ObjectInputStream(file);

                while (registeredUsers.size() < MAX_USERS && file.available() != 0) {
                    user = (User) objRead.readObject();
                    registeredUsers.add(user);
                    System.out.println(user);
                }
                objRead.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isLoggedIn() {
        return !currUser.equals(guestUser);
    }

    public void register() {
        try {
            // Getting input till the user entered is not registered already
            User user = new User();
            user.setUsername();
            // Iterates till user enters a valid username
            while (isRegistered(user)) {
                System.out.println("Username already registered! Try another or try logging in.");
                user.setUsername();
            }
            System.out.println("Username accepted");
            user.setPassword();

            // If total number of users are exceeding the MAX_USERS, then the oldest registration is deleted
            if (registeredUsers.size() >= MAX_USERS) {
                registeredUsers.poll();
            }
            System.out.println(registeredUsers.size());
            registeredUsers.add(user);

            // Login the newly created user
            login(user);
            System.out.println("'" + user.getUsername() + "' is logged in.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean login() {
        User user = new User();
        user.getInputs();
        if (!isRegistered(user)) {
            System.out.println("User credentials incorrect.");
            return false;
        } else {
            login(user);
            System.out.println("User logged in successfully.");
            return true;
        }
    }

    public void login(User user) {
        currUser = user;
    }

    public void logout() {
        currUser = guestUser;
    }

    public Queue<User> getRegisteredUsers() {
        return registeredUsers;
    }

    /**
     * Says if an user is already registered.
     * @param user the user to be checked with the file
     * @return true if user is found, else false
     */
    public boolean isRegistered(User user) {
        return registeredUsers.contains(user);
    }
}
