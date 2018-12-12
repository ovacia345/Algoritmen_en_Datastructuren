package assignment2;

import java.util.Scanner;

/**
 *
 * @author N.C.M. van Nistelrooij
 */
public class IOHandler {
    private final static Scanner stdIn = new Scanner(System.in);

    public static int readInteger() {
        return stdIn.nextInt();
    }

    public static long readLong() {
        return stdIn.nextLong(2);
    }

    public static void write(String string) {
        System.out.println(string);
    }
}
