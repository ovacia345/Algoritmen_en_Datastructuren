package assignment1;

import java.util.Scanner;

/**
 *
 * @author N.C.M van Nistelrooij
 * @author C Amghane
 */
public class IOHandler {
    private final static Scanner input = new Scanner(System.in);
        
    public static String getText() {
        String textualInput = input.next();
        
        while(!textualInput.chars().allMatch(Character::isLetter)){
           System.out.println("only strings allowed, try again!");
           textualInput = input.next("\\S+");
       }
       return textualInput;
        
    }
    
    public static String getLine() {
        String line = input.nextLine();
        return line;
    }
    
    public static int getInteger() {
        return input.nextInt();
    }
    
    public static double getDouble() {
        return input.nextDouble();
    }

    public static void print(String string) {
        System.out.println(string);
    }
}
