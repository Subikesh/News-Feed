package article;

import com.google.gson.JsonObject;
import query.NewsQuery;
import query.TopNewsQuery;
import utilities.Globals;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class Source implements Article, Serializable {
    public String id;
    public String name;
    private String description;
    private String language;
    private String country;
    private String category;
    private String url;
    transient JsonObject newsJson;

    /**
     * Constructor to copy data from Json. (calls the copyFromJson())
     * @param obj JsonObject from which data is copied
     */
    public Source(JsonObject obj) {
        newsJson = obj;
        copyFromJson(obj);
    }

    /**
     * copies the contents of JSONObject to the class fields
     * @param obj is the JSONObject passed
     */
    @Override
    public void copyFromJson(JsonObject obj) {
        id = obj.get("id").getAsString();
        name = obj.get("name").getAsString();
        description = obj.get("description").getAsString();
        language = obj.get("language").getAsString();
        country = obj.get("country").getAsString();
        category = obj.get("category").getAsString();
        url = obj.get("url").getAsString();
    }

    @Override
    public void gotoWebsite() {
        if(url != null) {
            System.out.println("Opening the url in browser...");
            try {
                Desktop desk = Desktop.getDesktop();
                desk.browse(new URI(url));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                System.out.println("Invalid URL provided.");
            }
        }
    }

    @Override
    public void showDetails() {
        String details = "\nName: " + name +
                "\nid: " + id +
                "\nDescription: " + description +
                "\nLanguage: " + language +
                "\nCountry: " + country +
                "\nCategory: " + category +
                "\nURL: " + url;
        System.out.println(details);
    }

    /**
     * @return title of source to be shown in menus
     */
    @Override
    public String getTitle() {
        return id;
    }

    @Override
    public void showMenu() {
        String option;
        try {
            do {
                showDetails();
                String menu = "\nOptions: \n" +
                        "1. View source' website\n" +
                        "2. View news from this source\n";
                if (Globals.SESSION.isLoggedIn()) {
                    if (Globals.SESSION.currUser.subscriptions.contains(this)) {
                        menu += "3. Remove subscriptions\n";
                    } else {
                        menu += "3. Subscribe to the source\n";
                    }
                } else {
                    menu += "(Please login to follow this source)\n";
                }

                menu += "\n0. Go to main menu\n" +
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
                    TopNewsQuery news = new TopNewsQuery();
                    news.filterQuery("sources", id);
                    news.showMenu();
                    break;
                case 3:
                    if(Globals.SESSION.isLoggedIn()) {
                        if (Globals.SESSION.currUser.subscriptions.contains(this)) {
                            // Remove offline news
                            Globals.SESSION.currUser.subscriptions.remove(this);
                            System.out.println("Subscription removed");
                        } else {
                            // Add offline news
                            Globals.SESSION.currUser.subscriptions.add(this);
                            System.out.println("Subscription added");
                        }
                    } else
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
