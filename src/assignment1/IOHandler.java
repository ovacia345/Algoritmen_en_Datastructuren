package assignment1;

import java.util.Scanner;
import java.util.stream.IntStream;

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
    
    public static void main(String[] args) {
        String p = "10010101010110";
        int[] bb = new int[p.length()];
        
        for(int i =0; i<p.length(); i++) {
            System.out.println(p.charAt(i));
            bb[i] = Character.getNumericValue(p.charAt(i));
            System.out.println(" bb " + bb[i]);
        }
        
    }
}
