package utilities;

import users.Authentication;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * Contains the globally accessible variables for the project
 */
public class Globals {
    public static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    public static final Authentication SESSION = new Authentication();
    // File names:
    public static final String USER_FILE = "data//users.txt";

    /**
     * Opens an url in browser
     * @param url url to open
     */
    public static void openWebsite(String url) {
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

}

