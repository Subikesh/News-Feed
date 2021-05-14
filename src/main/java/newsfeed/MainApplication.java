package newsfeed;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;

public class MainApplication implements ShowsMenu {

    @Override
    public void showMenu() {
        String mainMenu = "\n\n-------------------- Main Menu --------------------\n" +
                "1. Today's Highlights\n" +
                "2. Top Headlines\n" +
                "3. View news sources\n" +
                "4. How to use the interface?\n" +
                "0. Exit\n";
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
        switch (option) {
            case "1":
                System.out.println("Here is today's highlights");
                break;
            case "2":
                System.out.println("Here is top headlines");
                break;
            case "3":
                System.out.println("Showing Sources!");
                break;
            case "4":
                System.out.println("Redirecting to the Help markdown.");
                break;
            default:
                System.out.println("Thank you for using the application");
                System.exit(0);
        }
    }
}
