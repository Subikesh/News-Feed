package article;

import com.google.gson.JsonObject;
import utilities.ShowsMenu;

/**
 * Implemented by single news/source class
 * also contains functionality to show menu
 */
public interface Article extends ShowsMenu {
    /**
     * Assignes the fields' values from JsonObject
     * @param obj JsonObject from which values are copied
     */
    void copyFromJson(JsonObject obj);

    /**
     * Open the url of the article in a browser
     */
    void gotoWebsite();

    /**
     * Show all the details of this article
     */
    void showDetails();

    /**
     * @return title of the article as shown in menus
     */
    String getTitle();

    /**
     * Compares two articles
     * @return hashcode of object
     */
    int hashCode();

    boolean equals(Object o);
}
