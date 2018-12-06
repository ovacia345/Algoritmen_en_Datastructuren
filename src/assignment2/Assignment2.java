package assignment2;

import java.util.Arrays;

/**
 *
 * @author N.C.M. van Nistelrooij
 */
public class Assignment2 {
    public static void runAssignment() {
        int nrOfStudents = IOHandler.readInteger();
        int nrOfQuestions = IOHandler.readInteger();
        
        Student[] students = readStudents(nrOfStudents, nrOfQuestions);        
        Arrays.sort(students);
        
        int i = 5663;
    }
    
    private static Student[] readStudents(int nrOfStudents, int nrOfQuestions) {
        Student[] students = new Student[nrOfStudents];
        
        for(int i = 0; i < nrOfStudents; i++) {
            int[] answers = readAnswers(IOHandler.read(), nrOfQuestions);
            int score = IOHandler.readInteger();
            students[i] = new Student(answers, score);
        }
        
        return students;
    }
    
    private static int[] readAnswers(String answersString, int nrOfQuestions) {
        int[] answers = new int[nrOfQuestions];
        
        for(int i = 0; i < nrOfQuestions; i++) {
            int answer = Character.getNumericValue(answersString.charAt(i));
            answers[i] = answer;
        }
        
        return answers;
    }
}
