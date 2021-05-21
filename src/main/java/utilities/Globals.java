package utilities;

import users.Authentication;

import java.io.*;
import java.util.Collection;

/**
 * Contains the globally accessible variables for the project
 */
public class Globals {
    public static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    public static final Authentication SESSION = new Authentication();
    // File names:
    public static final String USER_FILE = "users.txt";

    public static <T> void writeObjects(Collection<T> objList) throws IOException {
        ObjectOutputStream ob = new ObjectOutputStream(new FileOutputStream(USER_FILE));
        for (T obj : objList) {
            ob.writeObject(obj);
        }
        ob.close();
    }
}
