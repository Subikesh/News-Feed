package article;

import newsfeed.ShowsMenu;
import users.User;
import utilities.Globals;

import java.io.IOException;
import java.util.ArrayList;

public class OfflineArticles<T extends Article> implements ShowsMenu {

    public User userObj;
    public ArrayList<T> articleList;
    public String fileCode;
    public String fileName;

    public OfflineArticles(User user, String fileCode) {
        userObj = user;
        this.fileCode = fileCode;
        fileName = user.getUsername() + fileCode + ".txt";
        articleList = new ArrayList<>();
        articleList = (ArrayList<T>) Globals.readObjects(articleList, fileName);
    }

    public void writeOffline() {
        // Write the final changes to the file
        try {
            Globals.writeObjects(articleList, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(T a) {
        articleList.add(a);
    }

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
            System.out.println("Articles");
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


