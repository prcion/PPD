package file;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadFromFile {

    public Scanner createScanner(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        return new Scanner(file);
    }

    public int[][] readFromFile(String fileName, int N, int M) throws FileNotFoundException {

        Scanner scanner = createScanner(fileName);
        int[][] matrix = new int[N][M];

        int n = 0, m = 0;
        while (scanner.hasNext()) {
            if (m == M){
                n ++;
                m = 0;
            } else {
                matrix[n][m] = Integer.parseInt(scanner.next());
                m ++;
            }
            if (n == N - 1 && m == M) break;
        }

        return matrix;
    }

}
