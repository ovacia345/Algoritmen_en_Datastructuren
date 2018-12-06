package assignment2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author N.C.M. van Nistelrooij
 */
public class Assignment2 {
    public static void runAssignment() {
        int nrOfStudents = IOHandler.readInteger();
        int nrOfQuestions = IOHandler.readInteger();
        int cut = nrOfQuestions / 2; // index start of right half
        
        Student[] students = readStudents(nrOfStudents, nrOfQuestions, cut);
        Arrays.sort(students);
        
        List<BinaryString> bruteForceAnswers = bruteForceLeftHalf(students, cut, nrOfQuestions);

        System.out.println("Accepted binary strings left half:");
        for(BinaryString bs : bruteForceAnswers) {
            System.out.println(bs);
        }
    }
    
    private static Student[] readStudents(int nrOfStudents, int nrOfQuestions,
            int cut) {
        Student[] students = new Student[nrOfStudents];
        
        for(int i = 0; i < nrOfStudents; i++) {
            int[] answers = readAnswers(IOHandler.read(), nrOfQuestions);
            int score = IOHandler.readInteger();
            
            int leftHalfWidth = cut;
            int rightHalfWidth = nrOfQuestions - cut;

            int nrOfCorrectAnswersLeftHalf = score - rightHalfWidth < 0
                    ? 0 : score - rightHalfWidth;
            int nrOfIncorrectAnswersLeftHalf = leftHalfWidth - score < 0
                    ? 0 : leftHalfWidth - score;

            students[i] = new Student(answers, score,
                    nrOfCorrectAnswersLeftHalf, nrOfIncorrectAnswersLeftHalf);
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

    private static List<BinaryString> bruteForceLeftHalf(Student[] students, int cut, int nrOfQuestions) {
        List<BinaryString> bruteForceAnswers = new ArrayList<>();

        Student student = students[0];
        BinaryString answers = student.getAnswers().copyOf(cut);

        Iterator<BinaryString> it = null;
        if(student.getNrOfCorrectAnswersLeftHalf() != 0) {
            it = new BinaryStringMaxKDifferencesIterator(answers,
            cut - student.getNrOfCorrectAnswersLeftHalf());
        } else if(student.getNrOfIncorrectAnswersLeftHalf() != 0) {
            it = new BinaryStringMaxKDifferencesIterator(answers.opposite(),
            cut - student.getNrOfIncorrectAnswersLeftHalf());
        } else {
            it = new BinaryStringMaxKDifferencesIterator(answers, cut);
        }

        while(it.hasNext()) {
            BinaryString next = it.next();

            for(int i = 1; i < students.length; i++) {
                student = students[i];
                answers = student.getAnswers().copyOf(cut);

                if(student.getNrOfCorrectAnswersLeftHalf() != 0) {
                    if(answers.getNrOfDifferencesBetween(next)
                            <= cut - student.getNrOfCorrectAnswersLeftHalf()) {
                    } else {
                        break;
                    }
                } else if(student.getNrOfIncorrectAnswersLeftHalf() != 0) {
                    if(answers.opposite().getNrOfDifferencesBetween(next)
                            <= cut - student.getNrOfIncorrectAnswersLeftHalf()) {
                    } else {
                        break;
                    }
                } else {
                }

                if(i == students.length - 1) {
                    bruteForceAnswers.add(next);
                }
            }
        }

        return bruteForceAnswers;
    }   
}
