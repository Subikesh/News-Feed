package article;

import utilities.ShowsMenu;
import users.User;
import utilities.Globals;

import java.io.*;
import java.util.ArrayList;

/**
 * Stores the list of news or source for offline, bookmark and subscriptions
 * @param <T> Type of list - News or Source
 */
public class OfflineArticles<T extends Article> implements ShowsMenu {
    public User userObj;
    public ArrayList<T> articleList;
    public String fileCode;
    public String fileName;
    public int max_len;
    public String title;

    /**
     * Constructor initializes the fields, and reads the corresponding files to the articleList
     * @param user the User for which articles are stored
     * @param fileCode "off", "bk" or "sub" based on its functionality
     * @param title  the title to show for showing menu
     */
    public OfflineArticles(User user, String fileCode, String title) {
        this.title = title;
        userObj = user;
        this.fileCode = fileCode;
        fileName = "data//" + user.getUsername() + fileCode + ".txt";
        articleList = new ArrayList<>();
        max_len = 15;
        articleList = (ArrayList<T>) Globals.readObjects(articleList, fileName);
    }

    /**
     * Writes the current list of articles to the file named fileName
     */
    public void writeOffline() {
        // Write the final changes to the file
        try {
            Globals.writeObjects(articleList, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deleted the file named fileName
     */
    public void deleteFile() {
        File file = new File(fileName);
        if (file.delete()) {
            System.out.println("File deleted");
        } else
            System.out.println("Not deleted");
    }

    /**
     * adds a object to the article list
     * @param a the object to be added
     */
    public void add(T a) {
        if (articleList.size() == max_len) {
            articleList.remove(0);
        }
        articleList.add(a);
    }

    /**
     * Deleted an object from articleLIst
     * @param a the object to be deleted
     */
    public void remove(T a) {
        articleList.remove(a);
    }

    public boolean contains(T a) {
        return articleList.contains(a);
    }

    /**
     * Shows the menu and actions to be done there
     * Writes the changes done to the articles to the file at the end
     */
    @Override
    public void showMenu() {
        String option = "0";
        do {
            System.out.println("-------------------- "+ title + " --------------------");
            if (articleList.size() == 0) {
                System.out.println("---No articles to display here---");
                break;
            } else {
                for (int i = 0; i < articleList.size(); i++) {
                    System.out.println((i+1) + ". " + articleList.get(i).getTitle());
                }
                System.out.println("0. Go to main menu\nYour option: ");
            }
            try {
                option = Globals.input.readLine();
                performAction(option);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!option.equals("0"));
    }

    @Override
    public void performAction(String option) {
        try {
            int opt = Integer.parseInt(option);
            if (opt < 0 || opt > articleList.size()) {
                System.out.println("Invalid input. Try again...\n");
            } else if(opt != 0){
                articleList.get(opt-1).showMenu();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Try again...");
        }

    }
}


