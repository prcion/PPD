package file;

import java.io.*;
import java.util.Random;

public class WriteToFile {
    private final String fileName;
    private final int size, min, max;

    public WriteToFile(String fileName, int size, int min, int max) {
        this.fileName = fileName;
        this.size = size;
        this.min = min;
        this.max = max;
    }

    public void writeToFileRandomNumbers() throws IOException {
        Random random = new Random();
        PrintWriter writer = new PrintWriter(createFile(this.fileName));
        for (int i = 0; i < size; ++i) {
            int randomNumber = random.nextInt(max + 1 - min) + min;
            writer.write(randomNumber + " ");
        }
        writer.close();
    }

    public File createFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.createNewFile()) {
            deleteContentFromFileIfFileExists(file);
        }
        return file;
    }

    public void deleteContentFromFileIfFileExists(File file) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(file);
        writer.write("");
        writer.close();
    }
}
