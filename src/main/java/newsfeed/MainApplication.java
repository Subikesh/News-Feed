package newsfeed;

import article.Source;
import org.jetbrains.annotations.NotNull;

import java.io.*;

import query.AllNewsQuery;
import query.SourceQuery;
import query.TopNewsQuery;
import utilities.Globals;
import utilities.ShowsMenu;

/**
 * This class contains the main menu and redirects to various functionalities of the project.
 */
public class MainApplication implements ShowsMenu {

    /**
     * Perform actions for user inputs related to api calls
     * @param option input from user
     */
    public void generalActions(int option) {
        switch (option) {
            case 1:
                new TopNewsQuery().showMenu();
                break;
            case 2:
                new AllNewsQuery().showMenu();
                break;
            case 3:
                new SourceQuery().showMenu();
                break;
            case 4:
                System.out.println("Redirecting to the Help markdown file.");
                Globals.openWebsite("https://github.com/Subikesh/News-Feed#readme");
                break;
            case 0:
                break;
            default:
                System.out.println("Invalid Input! Try again...");
        }
    }

    /**
     * Performs actions related to user authentication.
     * @param option input from user
     */
    public void userActions(int option) {
        if (Globals.SESSION.isLoggedIn()) {
            switch (option) {
                case 1:
                    Globals.SESSION.currUser.showMenu();
                    break;
                case 2:
                    Globals.SESSION.deleteUser();
                    break;
                case 3:
                    Globals.SESSION.logout();
                    break;
                default:
                    System.out.println("Invalid input! Try again...");
                    break;
            }
        } else {
            switch (option) {
                case 1:
                    Globals.SESSION.login();
                    break;
                case 2:
                    Globals.SESSION.register();
                    break;
                default:
                    System.out.println("Invalid input! Try again...");
                    break;
            }
        }
    }

    @Override
    public void showMenu() {
        try {
            String option;
            do {
                String mainMenu = "\n-------------------- Instructions --------------------\n" +
                        "Welcome to the news-feed application.\n" +
                        " * Enter the numbers from the menu options to perform corresponding actions.\n" +
                        " * For the menus with (command: xxxx) type xxxx followed by the option number\n" +
                        " * For example: User (command: user)\n" +
                        "\t 1. Logout\n" +
                        "\t 2. Profile\n" +
                        "   Type \"user 2\" to open user profile.\n" +
                        " * To exit or go back to the previous menu press 0\n";
                mainMenu += "\n-------------------- Main Menu --------------------\n" +
                        "1. Top Headlines\n" +
                        "2. Advanced filter options\n" +
                        "3. View news sources\n" +
                        "4. App instructions and source code\n\n";
                StringBuilder userMenu = new StringBuilder(mainMenu);
                userMenu.append("User (command: user)\n");
                if(Globals.SESSION.isLoggedIn())
                    userMenu.append("1. View Profile\n" +
                        "2. Delete user\n" +
                        "3. Logout\n");
                else
                    userMenu.append("1. Log-in\n" +
                            "2. Register\n");
                mainMenu = userMenu.append("\n0. Exit\n").toString();
                System.out.println(mainMenu + "Your option: ");
                option = Globals.input.readLine();
                performAction(option);
            } while(!(option.charAt(0) == '0'));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Globals.SESSION.saveFiles();
            System.out.println("Thank you for using the application");
        }
    }

    @Override
    public void performAction(@NotNull String option) {
        try {
            String[] action = option.split("\\s+");
            if(action.length == 1)
                generalActions(Integer.parseInt(option));
            else if(action[0].equalsIgnoreCase("user"))
                userActions(Integer.parseInt(action[1]));
            else
                generalActions(Integer.parseInt(option));
        } catch (NumberFormatException e) {
            System.out.println("Illegal input. Try again...");
        }
    }
}
