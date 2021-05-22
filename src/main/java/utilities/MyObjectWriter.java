package utilities;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class MyObjectWriter extends ObjectOutputStream {
    MyObjectWriter() throws IOException {
        super();
    }

    // Constructor of ths class
    // 1. Parameterized constructor
    MyObjectWriter(OutputStream o) throws IOException {
        super(o);
    }

    // Method of this class
    public void writeStreamHeader() throws IOException {
    }
}
