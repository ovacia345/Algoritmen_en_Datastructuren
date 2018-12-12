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
        
        List<Answers> bruteForceAnswers = bruteForceLeftHalf(students, cut,
                nrOfStudents);

        System.out.println("Accepted binary strings left half:");
        for(Answers answers : bruteForceAnswers) {
            System.out.println(answers);
        }
    }
    
    private static Student[] readStudents(int nrOfStudents, int nrOfQuestions,
            int cut) {
        Student[] students = new Student[nrOfStudents];
        int leftHalfWidth = cut;
        int rightHalfWidth = nrOfQuestions - cut;
        
        for(int i = 0; i < nrOfStudents; i++) {
            Answers answers = new Answers(IOHandler.readLong(), nrOfQuestions);
            int score = IOHandler.readInteger();            

            int maxErrorsLeftHalf = (score - rightHalfWidth) <= 0 ? leftHalfWidth :
                    leftHalfWidth - (score - rightHalfWidth);
            int maxErrorsComplimentLeftHalf = Math.min(leftHalfWidth, score);

            students[i] = new Student(answers, score,
                    maxErrorsLeftHalf, maxErrorsComplimentLeftHalf);
        }
        
        return students;
    }

    private static List<Answers> bruteForceLeftHalf(Student[] students,
            int cut, int nrOfStudents) {
        List<Answers> bruteForceAnswers = new ArrayList<>();

        Student student = students[0];
        Answers answers = student.getAnswers();

        Iterator<Answers> it = null;
        if(student.getMaxErrorsLeftHalf() != cut) {
            Answers leftHalfAnswers = answers.get(cut);
            it = new MaxKDifferencesAnswersIterator(leftHalfAnswers,
                    student.getMaxErrorsLeftHalf());
        } else if(student.getMaxErrorsComplimentLeftHalf() != cut) {
            Answers complimentLeftHalfAnswers = answers.getCompliment(cut);
            it = new MaxKDifferencesAnswersIterator(complimentLeftHalfAnswers,
                    student.getMaxErrorsComplimentLeftHalf());
        } else {
            Answers leftHalf = answers.get(cut);
            it = new MaxKDifferencesAnswersIterator(leftHalf, cut);
        }

        while(it.hasNext()) {
            Answers leftHalfAnswers = it.next();

            for(int i = 1; i < nrOfStudents; i++) {
                student = students[i];
                Answers studentAnswers = student.getAnswers();

                if(student.getMaxErrorsLeftHalf() != cut) {
                    Answers studentLeftHalfAnswers = studentAnswers.get(cut);
                    if(studentLeftHalfAnswers.getNrOfDifferencesWith(leftHalfAnswers)
                            > student.getMaxErrorsLeftHalf()) {
                        break;
                    }
                } else if(student.getMaxErrorsComplimentLeftHalf() != cut) {
                    Answers complimentStudentLeftHalfAnswers =
                            studentAnswers.getCompliment(cut);
                    if(complimentStudentLeftHalfAnswers.getNrOfDifferencesWith(leftHalfAnswers)
                            > student.getMaxErrorsComplimentLeftHalf()) {
                        break;
                    }
                }

                if(i == nrOfStudents - 1) {
                    bruteForceAnswers.add(leftHalfAnswers);
                }
            }
        }

        return bruteForceAnswers;
    }   
}
