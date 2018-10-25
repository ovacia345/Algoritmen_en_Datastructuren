/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import java.util.Scanner;

/**
 *
 * @author chihab
 */
public class IOHandler {
    static Scanner input = new Scanner(System.in);
    
    
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
    
    public static int getIntegers() {
        return input.nextInt();
    }
    
    public static double getDouble() {
        return input.nextDouble();
    }
}
