package assignment2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 *
 * @author N.C.M. van Nistelrooij
 */
public class Assignment2 {
    public static void runAssignment() {
        int nrOfStudents = IOHandler.readInteger();
        int nrOfQuestions = IOHandler.readInteger();
        int cutIndex = nrOfQuestions / 2; // index start of right cut
        
        Student[] students = readStudents(nrOfStudents, nrOfQuestions, cutIndex);

        Map<Answers, int[]> bruteForceLeftCutAnswers = bruteForceLeftCut(students,
                cutIndex);        
        Map<Answers, int[]> bruteForceRightCutAnswers = bruteForceRightCut(students,
                cutIndex);

        Collection<int[]> nrOfErrorsLeftCutArray = bruteForceLeftCutAnswers.values();
        Collection<int[]> nrOfNeededErrorsLeftCutArray = bruteForceRightCutAnswers.values();

        List<int[]> correctAnswersArray = new ArrayList<>(nrOfErrorsLeftCutArray);
        correctAnswersArray.retainAll(nrOfNeededErrorsLeftCutArray);

//        System.out.println();
//        for(Answers correctAnswers : correctAnswersList) {
//            System.out.println(correctAnswers);
//        }

        if(correctAnswersArray.size() == 1) {
            int[] correctAnswers = correctAnswersArray.get(0);
            IOHandler.write("\n" + correctAnswers.toString());
        } else {
            IOHandler.write("\n" + correctAnswersArray.size() + " solutions");
        }
    }
    
    private static Student[] readStudents(int nrOfStudents, int nrOfQuestions,
            int cut) {
        Student[] students = new Student[nrOfStudents];
        
        for(int i = 0; i < nrOfStudents; i++) {
            Answers answers = new Answers(IOHandler.readLong(), nrOfQuestions);
            int score = IOHandler.readInteger();            

            students[i] = new Student(answers, score, cut);
        }
        
        return students;
    }

    /**
     *
     * @param students
     * @param cutIndex
     * @return A map where the keys are the possible left cut answers and the
     * values are arrays of number of errors of every student's left cut answers
     * with respect to the key.
     */
    private static Map<Answers, int[]> bruteForceLeftCut(Student[] students, int cutIndex) {
        Map<Answers, int[]> bruteForceLeftCutAnswers = new HashMap<>();

        int bestLeftCutStudentIndex = getBestLeftCutStudentIndex(students);
        Student student = students[bestLeftCutStudentIndex];

        Answers[] possibleLeftCutAnswersArray = getPossibleLeftCutAnswersArray(student,
                cutIndex);
        for(Answers possibleLeftCutAnswers : possibleLeftCutAnswersArray) {
            int[] nrOfErrorsLeftCutArray = new int[students.length];

            for(int i = 0; i < students.length; i++) {
                student = students[i];
                Answers studentAnswers = student.getAnswers();

                if(student.getMaxErrorsComplimentLeftCut() < cutIndex) {
                    Answers studentComplimentLeftCutAnswers = studentAnswers.getCompliment(cutIndex);
                    int studentNrOfErrorsComplimentLeftCut =
                            studentComplimentLeftCutAnswers.getNrOfDifferencesWith(possibleLeftCutAnswers);
                    if(studentNrOfErrorsComplimentLeftCut
                            > student.getMaxErrorsComplimentLeftCut()) {
                        break;
                    } else {
                        nrOfErrorsLeftCutArray[i] = cutIndex - studentNrOfErrorsComplimentLeftCut;
                    }
                } else {
                   Answers studentLeftCutAnswers = studentAnswers.get(cutIndex);
                    int studentNrOfErrorsLeftCut =
                            studentLeftCutAnswers.getNrOfDifferencesWith(possibleLeftCutAnswers);
                    if(studentNrOfErrorsLeftCut > student.getMaxErrorsLeftCut()) {
                        break;
                    } else {
                        nrOfErrorsLeftCutArray[i] = studentNrOfErrorsLeftCut;
                    }
                }

                if(i == students.length - 1) {
                    bruteForceLeftCutAnswers.put(possibleLeftCutAnswers,
                            nrOfErrorsLeftCutArray);
                }
            }
        }

        return bruteForceLeftCutAnswers;
    }

    private static int getBestLeftCutStudentIndex(Student[] students) {
        int bestLeftCutStudentIndex = -1;
        int minMaxErrorsLeftCut = Integer.MAX_VALUE;

        for(int i = 0; i < students.length; i++) {
            Student student = students[i];
            int minMaxErrorsStudentLeftCut = Math.min(student.getMaxErrorsLeftCut(),
                    student.getMaxErrorsComplimentLeftCut());
            if(minMaxErrorsStudentLeftCut < minMaxErrorsLeftCut) {
                bestLeftCutStudentIndex = i;
                minMaxErrorsLeftCut = minMaxErrorsStudentLeftCut;
            }
        }

        return bestLeftCutStudentIndex;
    }

    private static Answers[] getPossibleLeftCutAnswersArray(Student student, int cutIndex) {
        Answers leftCutAnswers = student.getAnswers().get(cutIndex);
        int maxErrorsLeftCutAnswers = student.getMaxErrorsLeftCut();

        if(student.getMaxErrorsComplimentLeftCut() < cutIndex) {
            leftCutAnswers = student.getAnswers().getCompliment(cutIndex);
            maxErrorsLeftCutAnswers = student.getMaxErrorsComplimentLeftCut();
        }

        int nrOfPossibleLeftCutAnswers = sumBinomial(
                leftCutAnswers.getNrOfQuestions(), maxErrorsLeftCutAnswers);
        Answers[] possibleLeftCutAnswersArray = new Answers[nrOfPossibleLeftCutAnswers];
        Queue<Answers> queue = new LinkedList<>();
        queue.offer(leftCutAnswers);
        for(int i = 0; i < nrOfPossibleLeftCutAnswers; i++) {
            Answers possibleLeftCutAnswers = queue.poll();

            for(int j = possibleLeftCutAnswers.getLastFlippedIndex() + 1;
                    j < possibleLeftCutAnswers.getNrOfQuestions(); j++) {
                queue.offer(possibleLeftCutAnswers.changeAnswer(j));
            }

            possibleLeftCutAnswersArray[i] = possibleLeftCutAnswers;
        }

        return possibleLeftCutAnswersArray;
    }

    /**
     *
     * @param students
     * @param cutIndex
     * @return A map where the keys are the possible right cut answers and the
     * values are arrays of needed numbers of errors of every student's left cut
     * answers with respect to the key to get the student's score.
     */
    private static Map<Answers, int[]> bruteForceRightCut(Student[] students,
            int cutIndex) {
        Map<Answers, int[]> bruteForceRightCutAnswers = new HashMap<>();

        int bestRightCutStudentIndex = getBestRightCutStudentIndex(students);
        Student student = students[bestRightCutStudentIndex];
        int nrOfQuestions = student.getAnswers().getNrOfQuestions();

        Answers[] possibleRightCutAnswersArray = getPossibleRightCutAnswersArray(student,
                cutIndex);
        for(Answers possibleRightCutAnswers : possibleRightCutAnswersArray) {
            int[] nrOfNeededErrorsLeftCutArray = new int[students.length];

            for(int i = 0; i < students.length; i++) {
                student = students[i];
                Answers studentAnswers = student.getAnswers();

                if(student.getMaxErrorsComplimentRightCut() < nrOfQuestions - cutIndex) {
                    Answers studentComplimentRightCutAnswers = studentAnswers.getCompliment(
                                cutIndex, nrOfQuestions);
                    int studentNrOfErrorsComplimentRightCut =
                            studentComplimentRightCutAnswers.getNrOfDifferencesWith(possibleRightCutAnswers);
                    if(studentNrOfErrorsComplimentRightCut
                            > student.getMaxErrorsComplimentRightCut()) {
                        break;
                    } else {
                        nrOfNeededErrorsLeftCutArray[i] = cutIndex -
                            (student.getScore() - studentNrOfErrorsComplimentRightCut);
                    }
                } else {
                   Answers studentRightCutAnswers = studentAnswers.get(cutIndex, nrOfQuestions);
                    int studentNrOfErrorsRightCut =
                            studentRightCutAnswers.getNrOfDifferencesWith(possibleRightCutAnswers);
                    if(studentNrOfErrorsRightCut > student.getMaxErrorsRightCut()) {
                        break;
                    } else {
                        nrOfNeededErrorsLeftCutArray[i] = nrOfQuestions -
                                student.getScore() - studentNrOfErrorsRightCut;
                    }
                }

                if(i == students.length - 1) {
                    bruteForceRightCutAnswers.put(possibleRightCutAnswers,
                            nrOfNeededErrorsLeftCutArray);
                }
            }
        }

        return bruteForceRightCutAnswers;
    }
    
    private static int getBestRightCutStudentIndex(Student[] students) {
        int bestRightCutStudentIndex = -1;
        int minMaxErrorsRightCut = Integer.MAX_VALUE;

        for(int i = 0; i < students.length; i++) {
            Student student = students[i];
            int minMaxErrorsStudentRightCut = Math.min(student.getMaxErrorsRightCut(),
                    student.getMaxErrorsComplimentRightCut());
            if(minMaxErrorsStudentRightCut < minMaxErrorsRightCut) {
                bestRightCutStudentIndex = i;
                minMaxErrorsRightCut = minMaxErrorsStudentRightCut;
            }
        }

        return bestRightCutStudentIndex;
    }    
    
    private static Answers[] getPossibleRightCutAnswersArray(Student student, int cutIndex) {
        int nrOfQuestions = student.getAnswers().getNrOfQuestions();
        Answers rightCutAnswers = student.getAnswers().get(cutIndex, nrOfQuestions);
        int maxErrorsRightCutAnswers = student.getMaxErrorsRightCut();

        if(student.getMaxErrorsComplimentRightCut() < nrOfQuestions - cutIndex) {
            rightCutAnswers = student.getAnswers().getCompliment(cutIndex, nrOfQuestions);
            maxErrorsRightCutAnswers = student.getMaxErrorsComplimentRightCut();
        }

        int nrOfPossibleRightCutAnswers = sumBinomial(
                rightCutAnswers.getNrOfQuestions(), maxErrorsRightCutAnswers);
        Answers[] possibleRightCutAnswersArray = new Answers[nrOfPossibleRightCutAnswers];
        Queue<Answers> queue = new LinkedList<>();
        queue.offer(rightCutAnswers);
        for(int i = 0; i < nrOfPossibleRightCutAnswers; i++) {
            Answers possibleRightCutAnswers = queue.poll();

            for(int j = possibleRightCutAnswers.getLastFlippedIndex() + 1;
                    j < possibleRightCutAnswers.getNrOfQuestions(); j++) {
                queue.offer(possibleRightCutAnswers.changeAnswer(j));
            }

            possibleRightCutAnswersArray[i] = possibleRightCutAnswers;
        }

        return possibleRightCutAnswersArray;
    }

    private static int sumBinomial(int n, int k)
    {
        int sum = 1;
        int binomial = 1;
        for(int i = 1, m = n; i <= k; i++, m--) {
            binomial = binomial * m / i;
            sum += binomial;
        }

        return sum;
    }
}
