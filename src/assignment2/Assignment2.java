package assignment2;

import java.util.Arrays;
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

        SortedMap<int[], Answers> leftCutResults =
                bruteForceCut(students, 0, cutIndex, true);
        SortedMap<int[], Answers> rightCutResults =
                bruteForceCut(students, cutIndex, nrOfQuestions, false);

        String solution = combineResults(leftCutResults, rightCutResults,
                nrOfStudents, nrOfQuestions, cutIndex);

        IOHandler.write(solution);
    }
    
    private static Student[] readStudents(int nrOfStudents, int nrOfQuestions,
            int cutIndex) {
        Student[] students = new Student[nrOfStudents];
        
        for(int i = 0; i < nrOfStudents; i++) {
            Answers answers = new Answers(IOHandler.readLong(), nrOfQuestions);
            int score = IOHandler.readInteger();            

            students[i] = new Student(answers, score, cutIndex);
        }
        
        return students;
    }

    private static SortedMap<int[], Answers> bruteForceCut(Student[] students,
            int cutFromIndex, int cutToIndex, boolean isLeftCut) {
        Student minMaxErrorsStudent = getMinMaxErrorsStudent(students);
        Answers[] possibleAnswersArray = getPossibleAnswersArray(
                minMaxErrorsStudent, cutFromIndex, cutToIndex);
        return getBruteForceCutResults(students, possibleAnswersArray,
                cutFromIndex, cutToIndex, isLeftCut);
    }

    private static Student getMinMaxErrorsStudent(Student[] students) {
        Student minMaxErrorsStudent = null;
        int minMaxErrors = Integer.MAX_VALUE;

        for(int i = 0; i < students.length; i++) {
            Student student = students[i];
            int studentMinMaxErrors = Math.min(student.getMaxErrorsLeftCut(),
                    student.getMaxErrorsComplimentLeftCut());
            if(studentMinMaxErrors < minMaxErrors) {
                minMaxErrorsStudent = student;
                minMaxErrors = studentMinMaxErrors;
            }
        }

        return minMaxErrorsStudent;
    }

    private static Answers[] getPossibleAnswersArray(Student student,
            int cutFromIndex, int cutToIndex) {
        Answers answers = student.getAnswers().get(cutFromIndex, cutToIndex);
        int maxErrorsAnswers = student.getMaxErrorsRightCut();

        if(maxErrorsAnswers > student.getMaxErrorsComplimentRightCut()) {
            answers = student.getAnswers().getCompliment(cutFromIndex, cutToIndex);
            maxErrorsAnswers = student.getMaxErrorsComplimentRightCut();
        }

        int nrOfPossibleAnswers = sumBinomial(
                answers.getNrOfQuestions(), maxErrorsAnswers);
        Answers[] possibleAnswersArray = new Answers[nrOfPossibleAnswers];
        Queue<Answers> queue = new LinkedList<>();
        queue.offer(answers);
        for(int i = 0; i < nrOfPossibleAnswers; i++) {
            answers = queue.poll();

            for(int j = answers.getLastChangedAnswerIndex() + 1;
                    j < answers.getNrOfQuestions(); j++) {
                queue.offer(answers.changeAnswer(j));
            }

            possibleAnswersArray[i] = answers;
        }

        return possibleAnswersArray;
    }

    private static SortedMap<int[], Answers> getBruteForceCutResults(
            Student[] students, Answers[] possibleAnswersArray,
            int cutFromIndex, int cutToIndex, boolean isLeftCut) {
        SortedMap<int[], Answers> bruteForceCutResults = new TreeMap<>(
                (a1, a2) -> compareIntegerArrays(a1, a2));

        for(Answers possibleCutAnswers : possibleAnswersArray) {
            int[] nrOfErrorsLeftCutArray = new int[students.length + 1];

            for(int i = 0; i < students.length; i++) {
                Student student = students[i];

                if(!areAcceptedAnswers(possibleCutAnswers, student, cutFromIndex,
                        cutToIndex, isLeftCut, nrOfErrorsLeftCutArray, i)) {
                    break;
                } else if(i == students.length - 1) {
                    nrOfErrorsLeftCutArray[students.length] = counter++;
                    bruteForceCutResults.put(nrOfErrorsLeftCutArray,
                            possibleCutAnswers);
                }
            }
        }

        return bruteForceCutResults;
    }

    private static boolean areAcceptedAnswers(Answers cutAnswers, Student student,
            int cutFromIndex, int cutToIndex, boolean isLeftCut,
            int[] nrOfErrorsLeftCutArray, int studentIndex) {
        if(student.getMaxErrorsComplimentRightCut() < student.getMaxErrorsRightCut()) {
            Answers studentComplimentCutAnswers = student.getAnswers()
                    .getCompliment(cutFromIndex, cutToIndex);
            int studentNrOfErrorsComplimentCut =
                    studentComplimentCutAnswers.getNrOfDifferencesWith(cutAnswers);

            if(studentNrOfErrorsComplimentCut
                    > student.getMaxErrorsComplimentRightCut()) {
                return false;
            } else {
                int studentNrOfErrorsCut = cutToIndex - cutFromIndex -
                        studentNrOfErrorsComplimentCut;
                int nrOfErrorsLeftCut = getNrOfErrorsLeftCut(student,
                        cutFromIndex, cutToIndex, isLeftCut,
                        studentNrOfErrorsCut);
                nrOfErrorsLeftCutArray[studentIndex] = nrOfErrorsLeftCut;
                return true;
            }
        } else {
            Answers studentCutAnswers = student.getAnswers()
                    .get(cutFromIndex, cutToIndex);
            int studentNrOfErrorsCut =
                    studentCutAnswers.getNrOfDifferencesWith(cutAnswers);

            if(studentNrOfErrorsCut > student.getMaxErrorsRightCut()) {
                return false;
            } else {
                int nrOfErrorsLeftCut = getNrOfErrorsLeftCut(student,
                        cutFromIndex, cutToIndex, isLeftCut,
                        studentNrOfErrorsCut);
                nrOfErrorsLeftCutArray[studentIndex] = nrOfErrorsLeftCut;
                return true;
            }
        }
    }

    private static int getNrOfErrorsLeftCut(Student student, int cutFromIndex,
            int cutToIndex, boolean isLeftCut, int studentNrOfErrorsCut) {
        if(isLeftCut) {
            return studentNrOfErrorsCut;
        } else {
            return cutToIndex - student.getScore() - studentNrOfErrorsCut;
        }
    }

    private static String combineResults(
            SortedMap<int[], Answers> leftCutResults,
            SortedMap<int[], Answers> rightCutResults,
            int nrOfStudents, int nrOfQuestions, int cutIndex) {
        int[] sentinel = new int[nrOfStudents + 1];
        Arrays.fill(sentinel, Integer.MAX_VALUE);

        leftCutResults.put(sentinel, new Answers(0, cutIndex));
        Iterator<Entry<int[], Answers>> leftCutResultsIterator =
                leftCutResults.entrySet().iterator();
        Entry<int[], Answers> leftCutResult =
                leftCutResultsIterator.next();

        rightCutResults.put(sentinel, new Answers(0,
                nrOfQuestions - cutIndex));
        Iterator<Entry<int[], Answers>> rightCutResultsIterator =
                rightCutResults.entrySet().iterator();
        Entry<int[], Answers> rightCutResult = rightCutResultsIterator.next();

        Answers correctAnswers = null;
        long nrOfSolutions = 0;
        while(leftCutResultsIterator.hasNext() || rightCutResultsIterator.hasNext()) {
            int comparison = compareIntegerArrays(leftCutResult.getKey(),
                    rightCutResult.getKey(), nrOfStudents);
            if(comparison < 0) {
                if(leftCutResultsIterator.hasNext()) {
                    leftCutResult = leftCutResultsIterator.next();
                }
            } else if(comparison > 0) {
                if(rightCutResultsIterator.hasNext()) {
                    rightCutResult = rightCutResultsIterator.next();
                }
            } else {
                correctAnswers = leftCutResult.getValue().concatenate(
                        rightCutResult.getValue());

                int[] leftCutResultKey = leftCutResult.getKey();
                int leftCounter = 0;
                while(compareIntegerArrays(leftCutResultKey,
                        leftCutResult.getKey(), nrOfStudents) == 0) {
                    leftCounter++;
                    if(leftCutResultsIterator.hasNext()) {
                        leftCutResult = leftCutResultsIterator.next();
                    } else {
                        break;
                    }
                }

                int[] rightCutResultKey = rightCutResult.getKey();
                int rightCounter = 0;
                while(compareIntegerArrays(rightCutResultKey,
                        rightCutResult.getKey(), nrOfStudents) == 0) {
                    rightCounter++;
                    if(rightCutResultsIterator.hasNext()) {
                        rightCutResult = rightCutResultsIterator.next();
                    } else {
                        break;
                    }
                }

                nrOfSolutions += (long)leftCounter * (long)rightCounter;                
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

    private static int compareIntegerArrays(int[] a1, int[] a2,
            int fromIndex, int toIndex) {
        if(fromIndex < 0 || fromIndex >= a1.length || fromIndex >= a2.length ||
                toIndex < 0 || toIndex > a1.length || toIndex > a2.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        for(int i = fromIndex; i < toIndex; i++) {
            if(a1[i] != a2[i]) {
                return Integer.compare(a1[i], a2[i]);
            }
        }

        return 0;
    }

    private static int compareIntegerArrays(int[] a1, int[] a2, int toIndex) {
        return compareIntegerArrays(a1, a2, 0, toIndex);
    }

    private static int compareIntegerArrays(int[] a1, int[] a2) {
        int toIndex = Math.min(a1.length, a2.length);
        return compareIntegerArrays(a1, a2, toIndex);
    }
}
