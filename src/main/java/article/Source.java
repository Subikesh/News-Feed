package article;

import com.google.gson.JsonObject;
import query.NewsQuery;
import query.TopNewsQuery;
import utilities.Globals;

import java.io.*;

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
        Globals.openWebsite(url);
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

                menu += "\n0. Go back previous menu\n" +
                        "Your option: ";
                System.out.println(menu);
                option = Globals.input.readLine();
                performAction(option);
            } while (!option.equals("0"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("No input received.");
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

    /**
     * Compares two source objects with source-id
     * @param o the object to compare
     * @return true if both objects are same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Source source = (Source) o;

        return id.equals(source.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
