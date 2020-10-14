package verify;

import createfile.ReadFromConsole;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Verify {

    public static void main(String[] args) throws FileNotFoundException {
        ReadFromConsole readFromConsole = new ReadFromConsole();
        Scanner console = new Scanner(System.in);
        String fileOne = readFromConsole.readFromConsoleFileName(console);
        String fileTwo = readFromConsole.readFromConsoleFileName(console);

        ReadFromFile readFromFile = new ReadFromFile();

        long start = System.currentTimeMillis();

        var numbersFromFileOne = readFromFile.readFromFile(fileOne);
        var numbersFromFileTwo = readFromFile.readFromFile(fileTwo);

        if (numbersFromFileOne.size() != numbersFromFileTwo.size()) {
            System.out.println("fisierele nu contin aceleasi date");

        }

        if (numbersFromFileOne.equals(numbersFromFileTwo)) {
            System.out.println("fisierele contin aceleasi date");
        } else {
            System.out.println("fisierele nu contin aceleasi date");
        }

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
