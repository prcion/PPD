package createfile;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CreateFile {
    public static String fileName;
    public static int size, min, max;

    public static void main(String[] args) throws IOException {

        readFromConsole();

        writeToFile();
    }

    public static void readFromConsole() {
        Scanner console = new Scanner(System.in);

        ReadFromConsole readFromConsole = new ReadFromConsole();

        fileName = readFromConsole.readFromConsoleFileName(console);
        size = readFromConsole.readFromConsoleSize(console);
        min = readFromConsole.readFromConsoleMin(console);
        max = readFromConsole.readFromConsoleMax(console, min);
    }


    public static void writeToFile() throws IOException {
        WriteToFile writeToFile = new WriteToFile(fileName, size, min, max);

        File file = writeToFile.createFile(fileName);
        writeToFile.writeToFileRandomNumbers(file);
    }
}
