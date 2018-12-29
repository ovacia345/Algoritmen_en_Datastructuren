package assignment2;

import java.util.Scanner;

/**
 * Class that reads from standard input and writes to standard output.
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class IOHandler {
    private final static Scanner stdIn = new Scanner(System.in);

    public static int readInteger() {
        return stdIn.nextInt();
    }

    /**
     * A long is read with a radix of 2. So the input is a string of ones and
     * zeroes and the scanner reads that string as a binary string as opposed to
     * a decimal string.
     * @return {@code long} read in as binary string.
     */
    public static long readLong() {
        return stdIn.nextLong(2);
    }

    public static void write(String string) {
        System.out.println(string);
    }
}
