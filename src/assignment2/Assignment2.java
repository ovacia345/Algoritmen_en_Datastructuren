package assignment2;

import java.lang.reflect.Array;
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
        
        Student[] students = readStudents(nrOfStudents, nrOfQuestions);

        int cutIndex = nrOfQuestions; // index start of right cut
        SortedMap<int[], Answers> leftCutResults =
                bruteForceCut(nrOfQuestions, students, 3, cutIndex);
        SortedMap<int[], Answers> rightCutResults =
                bruteForceCut(nrOfQuestions, students, cutIndex, nrOfQuestions);

        String solution = combineResults(leftCutResults, rightCutResults,
                nrOfStudents, nrOfQuestions, cutIndex);

        IOHandler.write(solution);
    }
    
    private static Student[] readStudents(int nrOfStudents, int nrOfQuestions) {
        Student[] students = new Student[nrOfStudents];
        
        for(int i = 0; i < nrOfStudents; i++) {
            Answers answers = new Answers(IOHandler.readLong(), nrOfQuestions);
            int score = IOHandler.readInteger();            

            students[i] = new Student(answers, score);
        }
        
        return students;
    }

    private static SortedMap<int[], Answers> bruteForceCut(int nrOfQuestions,
            Student[] students, int cutFromIndex, int cutToIndex) {
        for(Student student : students) {
            student.setStudentCutInfo(cutFromIndex, cutToIndex);
        }

        Student student = getStudentWithLeastPossibleCutAnswers(students);

        Answers[] possibleCutAnswersArray = getPossibleCutAnswersArray(
                student, cutFromIndex, cutToIndex);

        SortedMap<int[], Answers> bruteForceCutResults = new TreeMap<>(
                (a1, a2) -> compareIntegerArrays(a1, a2));
        for(Answers possibleCutAnswers : possibleCutAnswersArray) {
            checkPossibleCutAnswers(students, cutFromIndex, cutToIndex,
                    bruteForceCutResults, possibleCutAnswers);
        }

        return bruteForceCutResults;
    }

    private static Student getStudentWithLeastPossibleCutAnswers(Student[] students) {
        Student studentWithLeastPossibleCutAnswers = students[0];
        int min = Math.min(studentWithLeastPossibleCutAnswers.getMaxScoreCut(),
                studentWithLeastPossibleCutAnswers.getMaxNrOfErrorsCut());

        for(int i = 1; i < students.length; i++) {
            Student student = students[i];
            int studentMin = Math.min(student.getMaxScoreCut(),
                    student.getMaxNrOfErrorsCut());
            if(studentMin < min) {
                studentWithLeastPossibleCutAnswers = student;
                min = studentMin;
            }
        }

        return studentWithLeastPossibleCutAnswers;
    }

    private static Answers[] getPossibleCutAnswersArray(Student student,
            int cutFromIndex, int cutToIndex) {
        Answers studentCutAnswers = student.getAnswers().get(cutFromIndex, cutToIndex);
        int studentMaxNrOfErrorsCut = student.getMaxNrOfErrorsCut();

        if(studentMaxNrOfErrorsCut > student.getMaxScoreCut()) {
            studentCutAnswers = student.getAnswers().getComplement(cutFromIndex, cutToIndex);
            studentMaxNrOfErrorsCut = student.getMaxScoreCut();
        }

        int nrOfPossibleCutAnswers = sumBinomial(
                studentCutAnswers.getNrOfQuestions(), studentMaxNrOfErrorsCut);
        Answers[] possibleCutAnswersArray = new Answers[nrOfPossibleCutAnswers];
        Queue<Answers> queue = new LinkedList<>();
        queue.offer(studentCutAnswers);
        for(int i = 0; i < nrOfPossibleCutAnswers; i++) {
            Answers possibleCutAnswers = queue.poll();

            for(int j = possibleCutAnswers.getLastChangedAnswerIndex() + 1;
                    j < possibleCutAnswers.getNrOfQuestions(); j++) {
                queue.offer(possibleCutAnswers.changeAnswer(j));
            }

            possibleCutAnswersArray[i] = possibleCutAnswers;
        }

        return possibleCutAnswersArray;
    }

    private static void checkPossibleCutAnswers(Student[] students,
            int cutFromIndex, int cutToIndex,
            SortedMap<int[], Answers> bruteForceCutResults,
            Answers possibleCutAnswers) {
        int[] nrOfErrorsLeftCutArray = new int[students.length + 1];

        for(int i = 0; i < students.length; i++) {
            Student student = students[i];

            if(!areAcceptedAnswers(possibleCutAnswers, student, cutFromIndex,
                    cutToIndex, nrOfErrorsLeftCutArray, i)) {
                break;
            } else if(i == students.length - 1) {
                nrOfErrorsLeftCutArray[students.length] = counter++;
                bruteForceCutResults.put(nrOfErrorsLeftCutArray,
                        possibleCutAnswers);
            }
        }
    }

    private static boolean areAcceptedAnswers(Answers possibleCutAnswers, Student student,
            int cutFromIndex, int cutToIndex, int[] nrOfErrorsLeftCutArray,
            int studentIndex) {
        if(student.getMaxScoreCut() < student.getMaxNrOfErrorsCut()) {
            Answers studentCutAnswersComplement = student.getAnswers()
                    .getComplement(cutFromIndex, cutToIndex);
            int studentScoreCut =
                    studentCutAnswersComplement.getNrOfDifferencesWith(possibleCutAnswers);

            return isNrOfErrorsCutWithinMaxNrOfErrorsCut(student, cutFromIndex,
                    cutToIndex, nrOfErrorsLeftCutArray, studentIndex,
                    studentScoreCut);
        } else {
            Answers studentCutAnswers = student.getAnswers()
                    .get(cutFromIndex, cutToIndex);
            int studentNrOfErrorsCut =
                    studentCutAnswers.getNrOfDifferencesWith(possibleCutAnswers);

            return isNrOfErrorsCutWithinMaxNrOfErrorsCut(student, cutFromIndex,
                    cutToIndex, nrOfErrorsLeftCutArray, studentIndex,
                    studentNrOfErrorsCut);
        }
    }

    private static boolean isNrOfErrorsCutWithinMaxNrOfErrorsCut(Student student,
            int cutFromIndex, int cutToIndex, int[] nrOfErrorsLeftCutArray,
            int studentIndex, int studentNrOfErrorsCut) {
        int maxNrOfErrorsCut = Math.min(student.getMaxScoreCut(),
                student.getMaxNrOfErrorsCut());
        if(studentNrOfErrorsCut > maxNrOfErrorsCut) {
            return false;
        } else {
            int nrOfErrorsLeftCut = getNrOfErrorsLeftCut(student, cutFromIndex,
                    cutToIndex, studentNrOfErrorsCut);
            nrOfErrorsLeftCutArray[studentIndex] = nrOfErrorsLeftCut;
            return true;
        }
    }

    private static int getNrOfErrorsLeftCut(Student student, 
            int cutFromIndex, int cutToIndex, int studentNrOfErrorsCut) {
        if(cutFromIndex == 0) {
            if(student.getMaxScoreCut() < student.getMaxNrOfErrorsCut()) {
                return cutToIndex - studentNrOfErrorsCut;
            } else {
                return studentNrOfErrorsCut;
            }
        } else {
            if(student.getMaxScoreCut() < student.getMaxNrOfErrorsCut()) {
                return cutFromIndex - (student.getScore() - studentNrOfErrorsCut);
            } else {
                return cutToIndex - student.getScore() - studentNrOfErrorsCut;
            }
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
        Entry<int[], Answers> leftCutResult = leftCutResultsIterator.next();
        Entry<int[], Answers>[] cutResults = (Entry<int[], Answers>[])
                Array.newInstance(leftCutResult.getClass(), 2);
        cutResults[0] = leftCutResult;

        rightCutResults.put(sentinel, new Answers(0,
                nrOfQuestions - cutIndex));
        Iterator<Entry<int[], Answers>> rightCutResultsIterator =
                rightCutResults.entrySet().iterator();
        cutResults[1] = rightCutResultsIterator.next();

        Answers correctAnswers = cutResults[0].getValue();
        long totalNrOfSolutions = 0;
        while(leftCutResultsIterator.hasNext() || rightCutResultsIterator.hasNext()) {
            if(correctAnswers.getNrOfQuestions() != nrOfQuestions) {
                correctAnswers = cutResults[0].getValue();
            }
            totalNrOfSolutions += getNrOfSolutions(nrOfStudents,
                    leftCutResultsIterator, rightCutResultsIterator,
                    cutResults, correctAnswers);
        }
        
        if(totalNrOfSolutions == 1) {
            return correctAnswers.toString();
        } else {
            return totalNrOfSolutions + " solutions";
        }
    }

    private static long getNrOfSolutions(int nrOfStudents,
            Iterator<Entry<int[], Answers>> leftCutResultsIterator,
            Iterator<Entry<int[], Answers>> rightCutResultsIterator,
            Entry<int[], Answers>[] cutResults, Answers correctAnswers) {
        int comparison = compareIntegerArrays(cutResults[0].getKey(),
                    cutResults[1].getKey(), nrOfStudents);
        if(comparison < 0 && leftCutResultsIterator.hasNext()) {
            cutResults[0] = leftCutResultsIterator.next();
            return 0;
        } else if(comparison > 0 && rightCutResultsIterator.hasNext()) {
            cutResults[1] = rightCutResultsIterator.next();
            return 0;
        } else {
            correctAnswers.concatenate(cutResults[1].getValue());
            
            int nrOfEqualLeftCutResultsKeys = getNrOfEqualCutResultKeys(nrOfStudents,
                    leftCutResultsIterator, cutResults, 0);
            int nrOfEqualRightCutResultsKeys = getNrOfEqualCutResultKeys(nrOfStudents,
                    rightCutResultsIterator, cutResults, 1);

            return (long)nrOfEqualLeftCutResultsKeys *(long)nrOfEqualRightCutResultsKeys;
        }
    }
    
    private static int getNrOfEqualCutResultKeys(int nrOfStudents,
            Iterator<Entry<int[], Answers>> cutResultsIterator,
            Entry<int[], Answers>[] cutResults, int cutResultIndex) {
        int nrOfEqualCutResultsKeys = 0;
        int[] initialCutResultKey = cutResults[cutResultIndex].getKey();
        while(compareIntegerArrays(initialCutResultKey,
                cutResults[cutResultIndex].getKey(), nrOfStudents) == 0) {
            nrOfEqualCutResultsKeys++;
            if(!cutResultsIterator.hasNext()) {
                return nrOfEqualCutResultsKeys;
            } else {
                cutResults[cutResultIndex] = cutResultsIterator.next();
            }
        }

        return nrOfEqualCutResultsKeys;
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
//        if(fromIndex < 0 || fromIndex >= a1.length || fromIndex >= a2.length ||
//                toIndex < 0 || toIndex > a1.length || toIndex > a2.length) {
//            throw new ArrayIndexOutOfBoundsException();
//        }
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
