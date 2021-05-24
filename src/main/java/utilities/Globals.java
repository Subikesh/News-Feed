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

    /**
     * Writes a list of serializable objects to a particular file
     * @param objList list of objects to write to file
     * @param fileName filename to which objects are written
     * @param <T> a class which is serializable whose object list is written
     * @throws IOException when some error occurs with file writing
     */
    public static <T> void writeObjects(Collection<T> objList, String fileName) throws IOException {
        File f = new File(fileName);
        f.createNewFile();
        ObjectOutputStream ob = new ObjectOutputStream(new FileOutputStream(fileName));
        for (T obj : objList) {
            ob.writeObject(obj);
        }
        ob.close();
    }

    @SuppressWarnings("unchecked")
    public static <T> Collection<T> readObjects(Collection<T> list, String fileName) {
        File f = new File(fileName);
        T obj = null;
        if (f.length() != 0) {
            try {
                FileInputStream file = null;
                file = new FileInputStream(fileName);
                ObjectInputStream objRead = new ObjectInputStream(file);

                while (file.available() != 0) {
                    obj = (T) objRead.readObject();
                    list.add(obj);
                }
                objRead.close();
            } catch (EOFException e) {
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}

