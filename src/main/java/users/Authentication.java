package users;

import newsfeed.Globals;

import java.io.*;

/**
 * This class contains the methods related to authenticating user
 * If currUser is equal to guestUser, then user is not logged in
 */
public class Authentication {
    public static final User guestUser = new User("_guest_", "_guest_");
    public static User currUser = guestUser;

    public Authentication() {
        try (ObjectOutputStream ob = new ObjectOutputStream(new FileOutputStream(Globals.USER_FILE, true))) {
            ob.writeObject(Authentication.currUser);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("Exception is raised. FIle not found");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isLoggedIn() {
        return !Authentication.currUser.equals(guestUser);
    }
}
