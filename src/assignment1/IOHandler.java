package assignment1;

import java.util.Scanner;

/**
 *
 * @author N.C.M van Nistelrooij
 * @author C Amghane
 */
public class IOHandler {
    private final static Scanner stdin = new Scanner(System.in);
    
    public static int readInteger() {
        return stdin.nextInt();
    }
    
    public static double readDouble() {
        return stdin.nextDouble();
    }

    public static void write(String string) {
        System.out.println(string);
    }
}
