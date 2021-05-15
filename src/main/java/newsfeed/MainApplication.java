package newsfeed;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import users.*;

public class MainApplication implements ShowsMenu {

    public void generalActions(int option) {
        switch (option) {
            case 1:
                System.out.println("Here is today's highlights");
                break;
            case 2:
                System.out.println("Here is top headlines");
                break;
            case 3:
                System.out.println("Showing Sources!");
                break;
            case 4:
                System.out.println("Redirecting to the Help markdown.");
                break;
            case 0:
                System.out.println("Thank you for using the application");
                break;
            default:
                System.out.println("Invalid Input! Try again...");
        }
    }

    public void userActions(int option) {
        if (Authentication.isLoggedIn()) {
            switch (option) {
                case 1:
                    Authentication.currUser.viewSubscriptions();
                    break;
                case 2:
                    Authentication.currUser.viewBookmarks();
                    break;
                case 3:
                    Authentication.logout();
                    break;
                default:
                    System.out.println("Invalid input!");
            }
        } else {
            if(option == 1)
                Authentication.login();
            else
                System.out.println("Invalid input! Try again...");
        }
    }

    @Override
    public void showMenu() {
        String mainMenu = "\n\n-------------------- Main Menu --------------------\n" +
                "1. Today's Highlights\n" +
                "2. Top Headlines\n" +
                "3. View news sources\n" +
                "4. How to use the interface?\n\n";
        StringBuilder userMenu = new StringBuilder(mainMenu);
        userMenu.append("user\n");
        if(Authentication.isLoggedIn())
            userMenu.append("1. View / Manage Subscriptions\n" +
                    "2. View Bookmarks\n" +
                    "3. Logout\n");
        else
            userMenu.append("1. Log-in\n");
        mainMenu = userMenu.append("\n0. Exit\n").toString();

        BufferedReader in = Globals.input;
        try {
            String option;
            do {
                System.out.println(mainMenu + "Your option: ");
                option = in.readLine();
                performAction(option);
            } while(!option.equals("0"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void performAction(@NotNull String option) {
        String[] action = option.split("\\s+");
        if(action[0].equalsIgnoreCase("user"))
            userActions(Integer.parseInt(action[1]));
        else
            generalActions(Integer.parseInt(option));
    }
}
