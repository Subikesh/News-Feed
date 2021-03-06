package utilities;

import java.io.*;
import java.util.Collection;

/**
 * Contains static methods used to handle reading and writing objects to and from files
 */
public class FileHandling {
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

    /**
     * Read objects from a file to a collection object
     * @param list collection to save the file inputs
     * @param fileName file from which input is read
     * @param <T> Type of object to read from file
     * @return Collection object read from the file
     */
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
            } catch (InvalidClassException e) {
                System.out.println("The class structure is different than that of file. Resetting file contents.\n" +
                        "Please register with the old username to retrieve the data.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
