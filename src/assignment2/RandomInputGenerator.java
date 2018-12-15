package assignment2;

import java.io.ByteArrayInputStream;
import java.util.Random;

/**
 *
 * @author N.C.M. van Nistelrooij
 */
public class RandomInputGenerator {
    private final static Random rand = new Random();

    public static void generateRandomInput(int nrOfStudents, int nrOfQuestions) {
        StringBuilder input = new StringBuilder();
        input.append(nrOfStudents).append(' ').append(nrOfQuestions).append('\n');
        
        for(int i = 0; i < nrOfStudents; i++) {
            for(int j = 0; j < nrOfQuestions; j++) {
                if(rand.nextBoolean()) {
                    input.append('1');
                } else {
                    input.append('0');
                }
            }

            int score = nrOfQuestions / 2;
            input.append(' ').append(score).append('\n');
        }

        System.out.print(input.toString());
        System.setIn(new ByteArrayInputStream(input.toString().getBytes()));
    }
}
