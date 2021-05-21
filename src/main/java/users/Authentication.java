package users;

import newsfeed.Globals;

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
    public static ObjectOutputStream objWrite;
    public static ObjectInputStream objRead;
    // User list is stored so in queue. If more than 15 users come, the oldest registered user is popped
    private Queue<User> registeredUsers;

    public Authentication() {
        User user;
        registeredUsers = new LinkedList<>();
        try {
            objWrite = new ObjectOutputStream(new FileOutputStream(Globals.USER_FILE, true));
            objRead = new ObjectInputStream(new FileInputStream(Globals.USER_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while ((user = (User) objRead.readObject()) != null) {
                registeredUsers.add(user);
                System.out.println(registeredUsers);
            }
        } catch (FileNotFoundException | EOFException | StreamCorruptedException e) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean isLoggedIn() {
        return !currUser.equals(guestUser);
    }

    public void register() {
        try {
            File userFile = new File(Globals.USER_FILE);
            userFile.createNewFile(); // Creates if file does not exist
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

                if (registeredUsers.size() < MAX_USERS) {
                    // Write new user to file
                    objWrite.writeObject(user);
                    registeredUsers.add(user);
                } else {
                    registeredUsers.poll();
                    registeredUsers.add(user);
                    Globals.writeObjects(objWrite, registeredUsers);
                }
                // Log-in newly created user
                login(user);
                System.out.println("'" + user.getUsername() + "' is logged in.");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
//        System.out.println("Registered users: \n");
//        getUsers();
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

    /**
     * Says if an user is already registered.
     * @param user the user to be checked with the file
     * @return true if user is found, else false
     */
    public boolean isRegistered(User user) {
        return registeredUsers.contains(user);
    }
}
