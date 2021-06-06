package users;

import article.*;
import query.NewsQuery;
import query.TopNewsQuery;
import utilities.Globals;
import utilities.ShowsMenu;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User class contains the details of an user
 * This class is serialized in the Users details file.
 */
public class User implements ShowsMenu, Serializable {
    private String username;
    private String password;
    public transient OfflineArticles<News> offlineNews;
    public transient OfflineArticles<News> bookmarkNews;
    public transient OfflineArticles<Source> subscriptions;

    public User() {
        username = null;
        password = null;
    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the user details from std input
     */
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
        this.subscriptions = new OfflineArticles<>(this, "sub", "Subscriptions");
    }

    /**
     * Writes the changed article lists to the corresponding files
     */
    public void writeOffline() {
        offlineNews.writeOffline();
        bookmarkNews.writeOffline();
        subscriptions.writeOffline();
    }

    /**
     * Deleted all the files for this user. To be done while deleting the user.
     */
    public void deleteOffline() {
        offlineNews.deleteFile();
        bookmarkNews.deleteFile();
        subscriptions.deleteFile();
    }

    /**
     * @return username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets username from input
     * @throws IOException raised when invalid input is provided
     */
    public void setUsername() throws IOException {
        do {
            System.out.println("Enter username :");
            username = Globals.input.readLine();
            if (username.isEmpty()) {
                System.out.println("Invalid username.");
            }
        } while (username.isEmpty());
    }

    /**
     * @return password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets password from input
     * @throws IOException raised when invalid input is provided
     */
    public void setPassword() throws IOException {
        do {
            System.out.println("Enter password :");
            password = Globals.input.readLine();
            if (password.isEmpty()) {
                System.out.println("Invalid password.");
            }
        } while (password.isEmpty());
    }

    /**
     * Shows the list of subscriptions and actions related to that
     */
    public void viewSubscriptions() {
        subscriptions.showMenu();
    }

    /**
     * View the news from the sources that user has subscribed
     */
    public void viewSubNews() {
        String sources = subscriptions.articleList.stream()
                .map((object) -> object.id)
                .reduce((prev, curr) -> prev + "," + curr)
                .orElse(null);
        TopNewsQuery news = new TopNewsQuery();
        System.out.println(sources);
        news.filterQuery("sources", sources);
        news.showMenu();
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
    public void showMenu() {
        String option;
        try {
            do {
                System.out.println("\n-------------------- User Profile --------------------\n");
                System.out.println(this);
                System.out.println("1. View Subscriptions\n" +
                        "2. View Bookmarks\n" +
                        "3. View offline news\n" +
                        "4. News from subscribed sources\n" +
                        "\n0. Go back previous menu\n" +
                        "Your option: ");
                option = Globals.input.readLine();
                try {
                    performAction(option);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Invalid input. Try again...");
                }
            } while (!option.equals("0"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("No input received.");
        }
    }

    @Override
    public void performAction(String option) throws NumberFormatException {
        int opt = Integer.parseInt(option);
        switch (opt) {
            case 1:
                viewSubscriptions();
                break;
            case 2:
                viewBookmarks();
                break;
            case 3:
                viewOffline();
                break;
            case 4:
                System.out.println("Show news of subscriptions.");
                viewSubNews();
                break;
            case 0:
                break;
            default:
                System.out.println("Invalid input. Try again...");
        }
    }

    @Override
    public String toString() {
        return "User details\n" +
                "username : " + username + "\n";
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
