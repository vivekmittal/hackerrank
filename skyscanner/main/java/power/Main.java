package power;

import java.util.Scanner;

/**
 * @author Vivek Mittal
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        final int n = sc.nextInt();

        System.out.println((2 << n) - 2);
    }
}