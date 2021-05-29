package article;

import com.google.gson.JsonObject;
import utilities.Globals;

import java.io.IOException;
import java.io.Serializable;

/**
 * Class displays details of a single news object from a JsonObject
 */
public class News implements Article, Serializable {
    private String author;
    public String title;
    private String sourceName;
    private String description;
    private String publishedAt;
    private String content;
    private String url;
    transient JsonObject newsJson;

    /**
     * Constructor to copy data from Json. (calls the copyFromJson())
     * @param obj JsonObject from which data is copied
     */
    public News(JsonObject obj) {
        newsJson = obj;
        copyFromJson(obj);
    }

    /**
     * copies the contents of JSONObject to the class fields
     * @param obj is the JSONObject passed
     */
    @Override
    public void copyFromJson(JsonObject obj) {
        if(obj.get("author").isJsonNull())
            author = null;
        else
            author = obj.get("author").getAsString();
        title = obj.get("title").getAsString();
        sourceName = obj.get("source").getAsJsonObject().get("name").getAsString();
        description = obj.get("description").getAsString();
        publishedAt = obj.get("publishedAt").getAsString();
        if(obj.get("content").isJsonNull())
            content = null;
        else
            content = obj.get("content").getAsString();
        url = obj.get("url").getAsString();
    }

    /**
     * @return title of news to be shown in menus
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * Opens the url of the news article in a browser
     */
    @Override
    public void gotoWebsite() {
        Globals.openWebsite(url);
    }

    @Override
    public void showDetails() {
        String details = "\nTitle: " + title;
        if(author != null) {
            details += "\nAuthor: " + author;
        }
        details += "\nSource name: " + sourceName +
                "\nDescription: " + description +
                "\nPublished time: " + publishedAt +
                "\nContent: " + content +
                "\nURL: " + url;
        System.out.println(details);
    }

    @Override
    public void showMenu() {
        String option;
        try {
            do {
                showDetails();
                String menu = "\nOptions: \n" +
                        "1. View full article (Open website)\n";
                if (Globals.SESSION.isLoggedIn()) {
                    if (Globals.SESSION.currUser.offlineNews.contains(this)) {
                        menu += "2. Remove Offline news\n";
                    } else {
                        menu += "2. Make article offline\n";
                    }
                    if (Globals.SESSION.currUser.bookmarkNews.contains(this)) {
                        menu += "3. Remove Bookmark\n";
                    } else {
                        menu += "3. Bookmark news\n";
                    }
                } else {
                    menu += "(Please login to add bookmark and save news offline)\n";
                }

                menu += "\n0. Go to previous menu\n" +
                        "Your option: ";
                System.out.println(menu);
                option = Globals.input.readLine();
                performAction(option);
            } while (!option.equals("0"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void performAction(String option) {
        try {
            int opt = Integer.parseInt(option);
            switch (opt) {
                case 1:
                    gotoWebsite();
                    break;
                case 2:
                    if(Globals.SESSION.isLoggedIn()) {
                        if (Globals.SESSION.currUser.offlineNews.contains(this)) {
                            // Remove offline news
                            Globals.SESSION.currUser.offlineNews.remove(this);
                            System.out.println("Offline news removed");
                        } else {
                            // Add offline news
                            Globals.SESSION.currUser.offlineNews.add(this);
                            System.out.println("Offline news added");
                        }
                    } else
                        System.out.println("Invalid input. Try again...");
                    break;
                case 3:
                    if(Globals.SESSION.isLoggedIn()) {
                        if (Globals.SESSION.currUser.bookmarkNews.contains(this)) {
                            // Remove bookmark
                            Globals.SESSION.currUser.bookmarkNews.remove(this);
                            System.out.println("Bookmark removed");
                        } else {
                            // Add bookmark
                            Globals.SESSION.currUser.bookmarkNews.add(this);
                            System.out.println("Bookmark added");
                        }
                    }
                    else
                        System.out.println("Invalid input. Try again...");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid input. Try again...");
                    break;
            }
        } catch (NumberFormatException exception) {
            System.out.println("Invalid input. Try again...");
        }
    }
}
