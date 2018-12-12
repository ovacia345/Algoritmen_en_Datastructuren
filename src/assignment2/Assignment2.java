package assignment2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

//        System.out.println("\nStudents after sorting:");
//        for(int i = 0; i < nrOfStudents; i++) {
//            System.out.println(i + ": " + students[i].getAnswers() + " " + students[i].getScore());
//        }
        
        Map<Answers, int[]> bruteForceLeftHalfAnswers = bruteForceLeftHalf(students, cut,
                nrOfStudents, nrOfQuestions);

//        System.out.println("\nAccepted answers left cut:");
//        for(Entry<Answers, int[]> entry : bruteForceLeftHalfAnswers.entrySet()) {
//            Answers leftHalfAnswers = entry.getKey();
//            int[] nrOfErrorsArray = entry.getValue();
//
//            System.out.println("Answers:\t\t" + leftHalfAnswers);
//            System.out.print("Nr. errors right half:\t");
//            for(int nrOfErrors : nrOfErrorsArray) {
//                System.out.print(" " + nrOfErrors);
//            }
//            System.out.println();
//        }

        List<Answers> correctAnswersList = new ArrayList<>();
        for(Answers leftHalfAnswers : bruteForceLeftHalfAnswers.keySet()) {
            List<Answers> answersList = bruteForceRightHalf(leftHalfAnswers, cut, nrOfQuestions,
                    nrOfStudents, students, bruteForceLeftHalfAnswers);
            correctAnswersList.addAll(answersList);
        }

//        System.out.println();
//        for(Answers correctAnswers : correctAnswersList) {
//            System.out.println(correctAnswers);
//        }

        if(correctAnswersList.size() == 1) {
            Answers correctAnswers = correctAnswersList.get(0);
            IOHandler.write("\n" + correctAnswers.toString());
        } else {
            IOHandler.write("\n" + correctAnswersList.size() + " solutions");
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

    private static Map<Answers, int[]> bruteForceLeftHalf(Student[] students,
            int cut, int nrOfStudents, int nrOfQuestions) {
        Map<Answers, int[]> bruteForceAnswers = new HashMap<>();

        Student student = students[0];
        Answers studentAnswers = student.getAnswers();

        Iterator<Answers> it = null;
        if(student.getMaxErrorsLeftHalf() != cut) {
            Answers leftHalfAnswers = studentAnswers.get(cut);
            it = new MaxKDifferencesAnswersIterator(leftHalfAnswers,
                    student.getMaxErrorsLeftHalf());
        } else if(student.getMaxErrorsComplimentLeftHalf() != cut) {
            Answers complimentLeftHalfAnswers = studentAnswers.getCompliment(cut);
            it = new MaxKDifferencesAnswersIterator(complimentLeftHalfAnswers,
                    student.getMaxErrorsComplimentLeftHalf());
        } else {
            Answers leftHalf = studentAnswers.get(cut);
            it = new MaxKDifferencesAnswersIterator(leftHalf, cut);
        }

        while(it.hasNext()) {
            Answers leftHalfAnswers = it.next();
            int[] nrOfErrorsArray = new int[nrOfStudents];

            for(int i = 0; i < nrOfStudents; i++) {
                student = students[i];
                studentAnswers = student.getAnswers();

                if(student.getMaxErrorsLeftHalf() != cut) {
                    Answers studentLeftHalfAnswers = studentAnswers.get(cut);
                    int nrOfErrors =
                            studentLeftHalfAnswers.getNrOfDifferencesWith(leftHalfAnswers);
                    if(nrOfErrors > student.getMaxErrorsLeftHalf()) {
                        break;
                    } else {
                        int nrOfErrorsRightHalf = nrOfQuestions - student.getScore() -
                                nrOfErrors;
                        nrOfErrorsArray[i] = nrOfErrorsRightHalf;
                    }
                } else if(student.getMaxErrorsComplimentLeftHalf() != cut) {
                    Answers complimentStudentLeftHalfAnswers = studentAnswers.getCompliment(cut);
                    int complimentNrOfErrors =
                            complimentStudentLeftHalfAnswers.getNrOfDifferencesWith(leftHalfAnswers);
                    if(complimentNrOfErrors
                            > student.getMaxErrorsComplimentLeftHalf()) {
                        break;
                    } else {
                        int nrOfErrorsRightHalf = nrOfQuestions - student.getScore() -
                                studentAnswers.get(cut).getNrOfDifferencesWith(leftHalfAnswers);
                        nrOfErrorsArray[i] = nrOfErrorsRightHalf;
                    }
                } else {
                    int nrOfErrorsRightHalf = nrOfQuestions - student.getScore() -
                            studentAnswers.get(cut).getNrOfDifferencesWith(leftHalfAnswers);
                    nrOfErrorsArray[i] = nrOfErrorsRightHalf;
                }

                if(i == nrOfStudents - 1) {
                    bruteForceAnswers.put(leftHalfAnswers, nrOfErrorsArray);
                }
            }
        }

        return bruteForceAnswers;
    }

    public static List<Answers> bruteForceRightHalf(Answers leftHalfAnswers, int cut,
            int nrOfQuestions, int nrOfStudents, Student[] students,
            Map<Answers, int[]> bruteForceLeftHalfAnswers) {
        List<Answers> bruteForceAnswers = new ArrayList<>();
        int[] nrOfErrorsArray = bruteForceLeftHalfAnswers.get(leftHalfAnswers);
        int[] bestValue = getIndexOfBestValue(nrOfErrorsArray, cut, nrOfQuestions);

        Student student = students[bestValue[0]];
        Answers studentAnswers = student.getAnswers();

        int nrOfErrorsRightHalf = bestValue[1];
        KDifferencesAnswersIterator it = null;
        if(nrOfErrorsRightHalf <= (nrOfQuestions - cut) / 2) {
            it = new KDifferencesAnswersIterator(
                        studentAnswers.get(cut, nrOfQuestions),
                        nrOfErrorsRightHalf);
        } else {
            it = new KDifferencesAnswersIterator(
                        studentAnswers.getCompliment(cut, nrOfQuestions),
                        nrOfQuestions - cut - nrOfErrorsRightHalf);
        }

        while(it.hasNext()) {
            Answers rightHalfAnswers = it.next();

            for(int i = 0; i < nrOfStudents; i++) {
                student = students[i];
                studentAnswers = student.getAnswers();
                Answers studentRightHalfAnswers = studentAnswers.get(cut, nrOfQuestions);
                nrOfErrorsRightHalf = nrOfErrorsArray[i];
                if(studentRightHalfAnswers.getNrOfDifferencesWith(rightHalfAnswers) !=
                        nrOfErrorsRightHalf) {
                    break;
                }

                if(i == nrOfStudents - 1) {
                    bruteForceAnswers.add(leftHalfAnswers.concatenate(
                            rightHalfAnswers, nrOfQuestions - cut));
                }
            }
        }

        return bruteForceAnswers;
    }

    private static int[] getIndexOfBestValue(int[] nrOfErrorsArray, int cut,
            int nrOfQuestions) {
        int[] max = {-1, Integer.MIN_VALUE};
        int[] min = {-1, Integer.MAX_VALUE};

        for(int i = 0; i < nrOfErrorsArray.length; i++) {
            if(nrOfErrorsArray[i] > max[1]) {
                max[0] = i;
                max[1] = nrOfErrorsArray[i];
            }
            if(nrOfErrorsArray[i] < min[1]) {
                min[0] = i;
                min[1] = nrOfErrorsArray[i];
            }
        }

        if(nrOfQuestions - cut - max[1] < min[1]) {
            return max;
        } else {
            return min;
        }
    }
}
