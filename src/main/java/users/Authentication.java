package users;

import newsfeed.Globals;

import java.io.*;

/**
 * This class contains static methods and session variables related to authenticating user
 * If currUser is equal to guestUser, then user is not authenticated.
 */
public class Authentication {
    public static final User guestUser = new User("_guest_", "_guest_");
    public static User currUser = guestUser;
    // User count is used to keep track of total user count. Used for object traversal in users file.
    private static int usersCount = 0;

    public Authentication() {

    }

    public static boolean isLoggedIn() {
        return !Authentication.currUser.equals(guestUser);
    }

    public static void register() {
        User newUser;
        try {
            File userFile = new File(Globals.USER_FILE);
            userFile.createNewFile(); // Creates if file does not exist
            ObjectOutputStream ob = new ObjectOutputStream(new FileOutputStream(userFile, true));
            // Getting input till the user entered is not registered already
            User user = new User();
            user.setUsername();
            // Iterates till user enters a valid username
            while(isRegistered(user)) {
                System.out.println("Username already registered! Try another or try logging in.");
                user.setUsername();
            }
            System.out.println("Username accepted");
            user.setPassword();

            // Write new user to file
            ob.writeObject(user);
            usersCount++;
            // Log-in newly created user
            login(user);
            System.out.println("'" + user.getUsername() + "' is logged in.");

            ob.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean login() {
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

    public static void login(User user) {
        currUser = user;
    }

    public static void logout() {
        currUser = guestUser;
    }

    /**
     * Says if an user is already registered.
     * @param user the user to be checked with the file
     * @return true if user is found, else false
     */
    public static boolean isRegistered(User user) {
        User tempUser;
        try (ObjectInputStream ob = new ObjectInputStream(new FileInputStream(Globals.USER_FILE))) {
            for (int i = 0; i < usersCount; i++) {
                tempUser = (User) ob.readObject();
                if (user.equals(tempUser)) {
                    return true;
                }
            }
            return false;
        } catch (FileNotFoundException ex) {
            // No users registered yet
            return false;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
