package article;

import com.google.gson.JsonObject;
import newsfeed.ShowsMenu;

/**
 * Implemented by single news/source class
 * also contains functionality to show menu
 */
public interface Article extends ShowsMenu {
    void copyFromJson(JsonObject obj);
    void gotoWebsite();
    void showDetails();
}
