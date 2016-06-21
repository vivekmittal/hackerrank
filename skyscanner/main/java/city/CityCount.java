package city;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;

import static java.util.Collections.reverseOrder;

/**
 * @author Vivek Mittal
 */
public class CityCount {
    public static void main(String[] args) {
        Map<String, Integer> cityCount = new HashMap<>();
        forEveryInput(city -> {
            int count = Optional.ofNullable(cityCount.get(city)).orElse(0);
            cityCount.put(city, count + 1);
        });

        cityCount
                .entrySet()
                .stream()
                .sorted(reverseOrder(Map.Entry.comparingByValue()))
                .limit(1)
                .forEach(entry -> System.out.println(entry.getKey()));
    }

    public static void forEveryInput(Consumer<String> doWithEachInput) {
        Scanner sc = new Scanner(System.in);
        int linesToRead = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < linesToRead; i++) {
            final String inputString = sc.nextLine();
            doWithEachInput.accept(inputString);
        }
    }
}
