package users;

import utilities.Globals;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * User class contains the details of an user
 * This class is serialized in the Users details file.
 */
public class User implements Serializable {
    private String username;
    private String password;
    public Set<String> bookmarks;
    private String bkFile;

    public User() {
        username = null;
        password = null;
        bookmarks = new HashSet<>();
    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        bookmarks = new HashSet<>();
        bkFile = username + "_bk.txt";
    }
    
    public void getInputs() {
        try {
            setUsername();
            setPassword();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUsername() throws IOException {
        System.out.println("Enter username :");
        username = Globals.input.readLine();
        bkFile = username + "_bk.txt";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword() throws IOException {
        System.out.println("Enter password :");
        password = Globals.input.readLine();
    }

    public String getUsername() {
        return username;
    }

    public void readOffline() {
        bookmarks = (HashSet<String>) Globals.readObjects(bookmarks, bkFile);
    }

    public void viewSubscriptions() {
        System.out.println("Here the sources set is printed");
    }

    public void addBookmark(String title) {
        bookmarks.add(title);
    }

    public void viewBookmarks() {
        // Print news of corresponding bookmarks
        System.out.println(bookmarks);
    }

    public void saveOffline() throws IOException {
        Globals.writeObjects(bookmarks, bkFile);
    }


    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", bookmarks=" + bookmarks +
                ", bkFile='" + bkFile + '\'' +
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
