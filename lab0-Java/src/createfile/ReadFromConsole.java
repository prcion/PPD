package createfile;

import java.util.Scanner;

public class ReadFromConsole {

    public String readFromConsoleFileName(Scanner console) {
        System.out.println("File name:");
        String fileName = console.nextLine();

        while (fileName.isEmpty()) {
            System.out.println("File name is invalid!");
            System.out.println("File name:");
            fileName = console.nextLine();
        }
        return fileName;
    }

    public int readFromConsoleSize(Scanner console) {
        System.out.println("Size:");
        int size = console.nextInt();
        while (size <= 0) {
            System.out.println("Size is invalid!");
            System.out.println("Size:");
            size = console.nextInt();
        }
        return size;
    }

    public int readFromConsoleMin(Scanner console) {
        System.out.println("Min:");
        return console.nextInt();
    }

    public int readFromConsoleMax(Scanner console, int min) {
        System.out.println("Max:");
        int max = console.nextInt();

        while (max < min) {
            System.out.println("Max is invalid, max < min!");
            System.out.println("Max:");
            max = console.nextInt();
        }
        return max;
    }
}
