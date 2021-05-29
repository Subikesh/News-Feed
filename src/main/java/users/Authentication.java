package users;

import utilities.FileHandling;
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
    // User list is stored so in queue. If more than 15 users come, the oldest registered user is popped
    private Queue<User> registeredUsers;

    /**
     * Gets all the user's details from userFile
     */
    public Authentication() {
        User user;
        registeredUsers = new LinkedList<>();
        // Create new file if doesn't exist
        registeredUsers = (Queue<User>) FileHandling.readObjects(registeredUsers, Globals.USER_FILE);
    }

    /**
     * @return true if user is logged in
     */
    public boolean isLoggedIn() {
        return !currUser.equals(guestUser);
    }

    /**
     * Create a new user if the username is not already registered.
     * Deleted the oldest registered user if total users registered goes more than MAX_USERS.
     */
    public void register() {
        try {
            // Getting input till the user entered is not registered already
            User user = new User();
            user.setUsername();
            // Iterates till user enters a valid username
            if(isRegistered(user)) {
                System.out.println("Username already registered! Try another or try logging in.");
                return;
            }
            if (!user.getUsername().matches("^[a-z][a-z_]*$")) {
                System.out.println("Username not valid. Try again...");
                return;
            }
            System.out.println("Username accepted");
            user.setPassword();

            // If total number of users are exceeding the MAX_USERS, then the oldest registration is deleted
            if (registeredUsers.size() >= MAX_USERS) {
                deleteUser(registeredUsers.peek());
            }
            registeredUsers.add(user);

            // Login the newly created user
            login(user);
            System.out.println("'" + user.getUsername() + "' is logged in.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets user credentials as input and logs in the user if he/she is registered.
     * @return true if user successfully logged in.
     */
    public boolean login() {
        User user = new User();
        user.getInputs();
        for (User reg : registeredUsers) {
            if (reg.equals(user) && reg.getPassword().equals(user.getPassword())) {
                login(reg);
                System.out.println("User logged in successfully.");
                return true;
            }
        }
        System.out.println("User credentials incorrect.");
        return false;
    }

    /**
     * Logs in the user and read the corresponding offline news, bookmarks and store it locally
     * @param user the user Object to be logged-in
     */
    public void login(User user) {
        user.readOffline();
        currUser = user;
    }

    /**
     * Writes the currUser's data to the files and logs out the user
     */
    public void logout() {
        if(currUser != guestUser) {
            System.out.println("Saving cache data...");
            currUser.writeOffline();
            currUser = guestUser;
        }
    }

    /**
     * Delete the currUser delete the files corresponding to that user
     */
    public void deleteUser() {
        try {
            System.out.println("Are you sure you want to delete your account? y/n: ");
            String inp = Globals.input.readLine();
            if (inp.equalsIgnoreCase("y") && isLoggedIn()) {
                User temp = currUser;
                logout();
                deleteUser(temp);
            } else {
                System.out.println("User not deleted");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete the user passed as parameter. Deletes all the files corresponding to that user.
     * @param user the User object to be deleted
     */
    public void deleteUser(User user) {
        user.deleteOffline();
        registeredUsers.remove(user);
        System.out.println("User deleted.");
    }

    /**
     * Saves the users data to local file and logs out the user
     */
    public void saveFiles() {
        try {
            FileHandling.writeObjects(Globals.SESSION.getRegisteredUsers(), Globals.USER_FILE);
            logout();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return list of registered users list
     */
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
