package assignment2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author N.C.M. van Nistelrooij
 */
public class Assignment2 {
    private static int counter = 0;

    public static void runAssignment() {
        int nrOfStudents = IOHandler.readInteger();
        int nrOfQuestions = IOHandler.readInteger();
        int cutIndex = nrOfQuestions / 2; // index start of right cut
        
        Student[] students = readStudents(nrOfStudents, nrOfQuestions, cutIndex);

        SortedMap<int[], Answers> bruteForceLeftCutAnswers =
                bruteForceLeftCut(students, cutIndex);
        SortedMap<int[], Answers> bruteForceRightCutAnswers =
                bruteForceRightCut(students, cutIndex);

        String solution = combineBruteForceAnswers(bruteForceLeftCutAnswers,
                bruteForceRightCutAnswers);

        IOHandler.write(solution);
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
    private static SortedMap<int[], Answers> bruteForceLeftCut(
            Student[] students, int cutIndex) {
        SortedMap<int[], Answers> bruteForceLeftCutAnswers = new TreeMap<>(
                (a1, a2) -> compareIntegerArrays(a1, a2));

        int bestLeftCutStudentIndex = getBestLeftCutStudentIndex(students);
        Student student = students[bestLeftCutStudentIndex];

        Answers[] possibleLeftCutAnswersArray = getPossibleLeftCutAnswersArray(student,
                cutIndex);
        for(Answers possibleLeftCutAnswers : possibleLeftCutAnswersArray) {
            int[] nrOfErrorsLeftCutArray = new int[students.length + 1];

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
                    nrOfErrorsLeftCutArray[students.length] = counter++;
                    bruteForceLeftCutAnswers.put(nrOfErrorsLeftCutArray,
                            possibleLeftCutAnswers);
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
            int studentMinMaxErrorsLeftCut = Math.min(student.getMaxErrorsLeftCut(),
                    student.getMaxErrorsComplimentLeftCut());
            if(studentMinMaxErrorsLeftCut < minMaxErrorsLeftCut) {
                bestLeftCutStudentIndex = i;
                minMaxErrorsLeftCut = studentMinMaxErrorsLeftCut;
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

            for(int j = possibleLeftCutAnswers.getLastChangedAnswerIndex() + 1;
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
    private static SortedMap<int[], Answers> bruteForceRightCut(
            Student[] students, int cutIndex) {
        SortedMap<int[], Answers> bruteForceRightCutAnswers = new TreeMap<>(
                (a1, a2) -> compareIntegerArrays(a1, a2));

        int bestRightCutStudentIndex = getBestRightCutStudentIndex(students);
        Student student = students[bestRightCutStudentIndex];
        int nrOfQuestions = student.getAnswers().getNrOfQuestions();

        Answers[] possibleRightCutAnswersArray = getPossibleRightCutAnswersArray(student,
                cutIndex);
        for(Answers possibleRightCutAnswers : possibleRightCutAnswersArray) {
            int[] nrOfNeededErrorsLeftCutArray = new int[students.length + 1];

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
                    nrOfNeededErrorsLeftCutArray[students.length] = counter++;
                    bruteForceRightCutAnswers.put(nrOfNeededErrorsLeftCutArray,
                            possibleRightCutAnswers);
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
            int studentMinMaxErrorsRightCut = Math.min(student.getMaxErrorsRightCut(),
                    student.getMaxErrorsComplimentRightCut());
            if(studentMinMaxErrorsRightCut < minMaxErrorsRightCut) {
                bestRightCutStudentIndex = i;
                minMaxErrorsRightCut = studentMinMaxErrorsRightCut;
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

            for(int j = possibleRightCutAnswers.getLastChangedAnswerIndex() + 1;
                    j < possibleRightCutAnswers.getNrOfQuestions(); j++) {
                queue.offer(possibleRightCutAnswers.changeAnswer(j));
            }

            possibleRightCutAnswersArray[i] = possibleRightCutAnswers;
        }

        return possibleRightCutAnswersArray;
    }

    private static String combineBruteForceAnswers(
            SortedMap<int[], Answers> bruteForceLeftCutAnswers,
            SortedMap<int[], Answers> bruteForceRightCutAnswers) {
        Answers correctAnswers = null;
        long nrOfSolutions = 0;

        Iterator<Entry<int[], Answers>> leftCutAnswersIterator =
                bruteForceLeftCutAnswers.entrySet().iterator();
        Iterator<Entry<int[], Answers>> rightCutAnswersIterator =
                bruteForceRightCutAnswers.entrySet().iterator();

        Entry<int[], Answers> leftCutAnswers = null;
        Entry<int[], Answers> rightCutAnswers = null;

        if(leftCutAnswersIterator.hasNext()) {
            leftCutAnswers = leftCutAnswersIterator.next();
        }
        if(rightCutAnswersIterator.hasNext()) {
            rightCutAnswers = rightCutAnswersIterator.next();
        }

        while(leftCutAnswers != null && rightCutAnswers != null) {
            int comparison = compareIntegerArrays(leftCutAnswers.getKey(),
                    rightCutAnswers.getKey(),
                    leftCutAnswers.getKey().length - 1);
            if(comparison < 0) {
                if(leftCutAnswersIterator != null &&
                        leftCutAnswersIterator.hasNext()) {
                    leftCutAnswers = leftCutAnswersIterator.next();
                } else {
                    leftCutAnswers = null;
                }
            } else if(comparison > 0) {
                if(rightCutAnswersIterator != null &&
                        rightCutAnswersIterator.hasNext()) {
                    rightCutAnswers = rightCutAnswersIterator.next();
                } else {
                    rightCutAnswers = null;
                }
            } else {
                correctAnswers = leftCutAnswers.getValue().concatenate(
                        rightCutAnswers.getValue());

                int[] leftCutAnswersCopy = leftCutAnswers.getKey();
                int leftCounter = 0;
                while(compareIntegerArrays(leftCutAnswersCopy,
                        leftCutAnswers.getKey(),
                        leftCutAnswersCopy.length - 1) == 0) {
                    leftCounter++;
                    if(leftCutAnswersIterator != null &&
                            leftCutAnswersIterator.hasNext()) {
                        leftCutAnswers = leftCutAnswersIterator.next();
                    } else {
                        break;
                    }
                }

                int[] rightCutAnswersCopy = rightCutAnswers.getKey();
                int rightCounter = 0;
                while(compareIntegerArrays(rightCutAnswersCopy,
                        rightCutAnswers.getKey(),
                        rightCutAnswersCopy.length - 1) == 0) {
                    rightCounter++;
                    if(rightCutAnswersIterator != null &&
                            rightCutAnswersIterator.hasNext()) {
                        rightCutAnswers = rightCutAnswersIterator.next();
                    } else {
                        break;
                    }
                }

                nrOfSolutions += (long)leftCounter * (long)rightCounter;                
            }

            if(leftCutAnswersIterator == null || rightCutAnswersIterator == null) {
                leftCutAnswers = null;
                rightCutAnswers = null;
            } else if(!leftCutAnswersIterator.hasNext() && !rightCutAnswersIterator.hasNext()) {
                leftCutAnswersIterator = null;
                rightCutAnswersIterator = null;
            }
        }

        if(nrOfSolutions == 1) {
            return correctAnswers.toString();
        } else {
            return nrOfSolutions + " solutions";
        }
    }

    private static int sumBinomial(int n, int k) {
        int binomial = 1;
        int sum = 1;
        for(int i = 1, m = n; i <= k; i++, m--) {
            binomial = binomial * m / i;
            sum += binomial;
        }

        return sum;
    }

    private static int compareIntegerArrays(int[] a1, int[] a2, int toIndex) {
        for(int i = 0; i < toIndex && i < a1.length && i < a2.length; i++) {
            if(a1[i] != a2[i]) {
                return Integer.compare(a1[i], a2[i]);
            }
        }

        return 0;
    }

    private static int compareIntegerArrays(int[] a1, int[] a2) {
        int toIndex = Math.min(a1.length, a2.length);
        return compareIntegerArrays(a1, a2, toIndex);
    }
}
