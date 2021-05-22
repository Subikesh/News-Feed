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

    public static <T> void writeObjects(Collection<T> objList, String fileName) throws IOException {
        ObjectOutputStream ob = new ObjectOutputStream(new FileOutputStream(fileName));
        for (T obj : objList) {
            ob.writeObject(obj);
        }
        ob.close();
    }

    @SuppressWarnings("unchecked")
    public static <T> Collection<T> readObjects(Collection<T> list, String fileName) {
        File f = new File(fileName);
        try {
            f.createNewFile();
        } catch (Exception e) {}
        T obj;
        if (f.length() != 0) {
            try {
                FileInputStream file = null;
                file = new FileInputStream(Globals.USER_FILE);
                ObjectInputStream objRead = new ObjectInputStream(file);

                while (file.available() != 0) {
                    obj = (T) objRead.readObject();
                    list.add(obj);
                    System.out.println(obj);
                }
                objRead.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}

