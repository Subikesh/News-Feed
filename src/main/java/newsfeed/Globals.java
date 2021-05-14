package newsfeed;

import java.io.*;

/**
 * Contains the globally accessible variables for the project
 */
public class Globals {
    public static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private static final String API_KEY = "27eee780f9e5438091906ffff0548b73";

    /**
     * Get the API key for accessing newsapi content
     * @return API key as string
     */
    public static String getApiKey() {
        return API_KEY;
    }
}

