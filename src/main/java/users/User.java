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

    public void readOffline() {
        this.offlineNews = new OfflineArticles<>(this, "off", "Offline News");
        this.bookmarkNews = new OfflineArticles<>(this, "bk", "Bookmark News");
    }

    public void writeOffline() {
        offlineNews.writeOffline();
        bookmarkNews.writeOffline();
    }

    public void deleteOffline() {
        offlineNews.deleteFile();
        bookmarkNews.deleteFile();
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

    public String getUsername() {
        return username;
    }

    public void viewSubscriptions() {
        System.out.println("Here the sources set is printed");
    }

    public void viewBookmarks() {
        bookmarkNews.showMenu();
    }

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
