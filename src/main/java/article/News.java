package article;

import com.google.gson.JsonObject;
import newsfeed.Globals;
import users.Authentication;

import java.io.IOException;

/**
 * Class displays details of a single news object from a JsonObject
 */
public class News implements Article {
    private String author;
    private String title;
    private String sourceId;
    private String description;
    private String publishedAt;
    private String content;
    private String url;
    JsonObject newsJson;

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
        author = obj.get("author").getAsString();
        title = obj.get("title").getAsString();
        sourceId = obj.get("sourceId").getAsJsonObject().get("id").getAsString();
        description = obj.get("description").getAsString();
        publishedAt = obj.get("publishedAt").getAsString();
        content = obj.get("content").getAsString();
        url = obj.get("url").getAsString();
    }

    /**
     * Opens the url of the news article in a browser
     */
    @Override
    public void gotoWebsite() {
        if(url != null)
            System.out.println("Opening the url in browser");
    }


    @Override
    public void showDetails() {
        String details = "\nTitle: " + title;
        if(author.equals("null")) {
            details += "\nAuthor: " + author;
        }
        if(sourceId.equals("null"))
            details += "\nSource id: " + sourceId;
        details += "\nDescription: " + description +
                "\nPublished time: " + publishedAt +
                "\nContent: " + content +
                "\nURL: " + url;
        System.out.println(details);
    }

    @Override
    public void showMenu() {
        showDetails();
        String option;
        try {
            do {
                String menu = "Options: \n" +
                        "1. View full article(Open website)\n";
                if (Authentication.isLoggedIn())
                    menu += "2. Make article offline\n" +
                            "3. Bookmark news\n";
                menu += "\n0. Go to main menu\n" +
                        "Your option: ";
                System.out.println(menu);
                option = Globals.input.readLine();
                performAction(option);
            } while (!option.equals("0"));
        } catch (NumberFormatException ex) {
            System.out.println("Invalid input. Try again...");
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
                    if(Authentication.isLoggedIn()) {
                        System.out.println("implement make offline");
                    }
                    break;
                case 3:
                    if(Authentication.isLoggedIn()) {
                        System.out.println("implement add bookmark");
                    }
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
