package users;

import article.News;
import article.OfflineArticles;
import query.AllNewsQuery;
import query.ApiQuery;
import utilities.Globals;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * User class contains the details of an user
 * This class is serialized in the Users details file.
 */
public class User implements Serializable {
    private String username;
    private String password;
    public ArrayList<String> bookmarks;
    public transient OfflineArticles<News> offlineNews;
    public transient OfflineArticles<News> bookmarkNews;

    public User() {
        username = null;
        password = null;
        bookmarks = new ArrayList<>();
    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        bookmarks = new ArrayList<>();
    }
    
    public void getInputs() {
        try {
            setUsername();
            setPassword();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialized the sources, bookmarks and offline with corresponding objects
     */
    public void readOffline() {
        this.offlineNews = new OfflineArticles<>(this, "off", "Offline News");
        this.bookmarkNews = new OfflineArticles<>(this, "bk", "Bookmark News");
    }

    /**
     * Writes the changed article lists to the corresponding files
     */
    public void writeOffline() {
        offlineNews.writeOffline();
        bookmarkNews.writeOffline();
    }

    /**
     * Deleted all the files for this user. To be done while deleting the user.
     */
    public void deleteOffline() {
        offlineNews.deleteFile();
        bookmarkNews.deleteFile();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername() throws IOException {
        System.out.println("Enter username :");
        username = Globals.input.readLine();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword() throws IOException {
        System.out.println("Enter password :");
        password = Globals.input.readLine();
    }

    public void viewSubscriptions() {
        System.out.println("Here the sources set is printed");
    }

    /**
     * Shows the menu of options and list of news bookmarked
     */
    public void viewBookmarks() {
        bookmarkNews.showMenu();
    }

    /**
     * Shows the menu of options and list of news shown offline
     */
    public void viewOffline() {
        offlineNews.showMenu();
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", bookmarks=" + bookmarks +
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
