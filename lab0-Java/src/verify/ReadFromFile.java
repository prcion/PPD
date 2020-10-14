package verify;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ReadFromFile {

    public Scanner createScanner(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        return new Scanner(file);
    }

    public List<Double> readFromFile(String fileName) throws FileNotFoundException {

        Scanner scanner = createScanner(fileName);
        List<Double> numbers = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();

            Arrays.stream(data.split(" "))
                    .map(Double::parseDouble)
                    .forEach(numbers::add);
        }

        return numbers.parallelStream()
                .sorted()
                .collect(Collectors.toList());
    }

}
