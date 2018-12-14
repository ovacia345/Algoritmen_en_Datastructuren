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
        int cutIndex = nrOfQuestions / 2; // index start of right cut
        
        Student[] students = readStudents(nrOfStudents, nrOfQuestions, cutIndex);

        SortedMap<int[], Answers> leftCutResults =
                bruteForceCut(students, 0, cutIndex);
        SortedMap<int[], Answers> rightCutResults =
                bruteForceCut(students, cutIndex, nrOfQuestions);

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
            int cutFromIndex, int cutToIndex) {
        Student minMaxErrorsStudent = getMinMaxErrorsStudent(students);

        Answers[] possibleCutAnswersArray = getPossibleCutAnswersArray(
                minMaxErrorsStudent, cutFromIndex, cutToIndex);

        SortedMap<int[], Answers> bruteForceCutResults = new TreeMap<>(
                (a1, a2) -> compareIntegerArrays(a1, a2));
        for(Answers possibleCutAnswers : possibleCutAnswersArray) {
            checkPossibleCutAnswers(students, cutFromIndex, cutToIndex,
                    bruteForceCutResults, possibleCutAnswers);
        }

        return bruteForceCutResults;
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

    private static Answers[] getPossibleCutAnswersArray(Student student,
            int cutFromIndex, int cutToIndex) {
        Answers cutAnswers = student.getAnswers().get(cutFromIndex, cutToIndex);
        int maxErrorsAnswers = student.getMaxErrorsRightCut();

        if(maxErrorsAnswers > student.getMaxErrorsComplimentRightCut()) {
            cutAnswers = student.getAnswers().getCompliment(cutFromIndex, cutToIndex);
            maxErrorsAnswers = student.getMaxErrorsComplimentRightCut();
        }

        int nrOfPossibleAnswers = sumBinomial(
                cutAnswers.getNrOfQuestions(), maxErrorsAnswers);
        Answers[] possibleAnswersArray = new Answers[nrOfPossibleAnswers];
        Queue<Answers> queue = new LinkedList<>();
        queue.offer(cutAnswers);
        for(int i = 0; i < nrOfPossibleAnswers; i++) {
            cutAnswers = queue.poll();

            for(int j = cutAnswers.getLastChangedAnswerIndex() + 1;
                    j < cutAnswers.getNrOfQuestions(); j++) {
                queue.offer(cutAnswers.changeAnswer(j));
            }

            possibleAnswersArray[i] = cutAnswers;
        }

        return possibleAnswersArray;
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

    private static boolean areAcceptedAnswers(Answers cutAnswers, Student student,
            int cutFromIndex, int cutToIndex, int[] nrOfErrorsLeftCutArray,
            int studentIndex) {
        if(student.getMaxErrorsComplimentRightCut() < student.getMaxErrorsRightCut()) {
            Answers studentComplimentCutAnswers = student.getAnswers()
                    .getCompliment(cutFromIndex, cutToIndex);
            int studentNrOfErrorsComplimentCut =
                    studentComplimentCutAnswers.getNrOfDifferencesWith(cutAnswers);

            return isNrOfErrorsCutWithinMaxErrorsCut(student, cutFromIndex,
                    cutToIndex, nrOfErrorsLeftCutArray, studentIndex,
                    studentNrOfErrorsComplimentCut);
        } else {
            Answers studentCutAnswers = student.getAnswers()
                    .get(cutFromIndex, cutToIndex);
            int studentNrOfErrorsCut =
                    studentCutAnswers.getNrOfDifferencesWith(cutAnswers);

            return isNrOfErrorsCutWithinMaxErrorsCut(student, cutFromIndex,
                    cutToIndex, nrOfErrorsLeftCutArray, studentIndex,
                    studentNrOfErrorsCut);
        }
    }

    private static boolean isNrOfErrorsCutWithinMaxErrorsCut(Student student,
            int cutFromIndex, int cutToIndex, int[] nrOfErrorsLeftCutArray,
            int studentIndex, int studentNrOfErrorsCut) {
        int maxErrorsCut = Math.min(student.getMaxErrorsRightCut(),
                student.getMaxErrorsComplimentRightCut());
        if(studentNrOfErrorsCut > maxErrorsCut) {
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
            if(student.getMaxErrorsComplimentLeftCut() < cutToIndex) {
                return cutToIndex - studentNrOfErrorsCut;
            } else {
                return studentNrOfErrorsCut;
            }
        } else {
            if(student.getMaxErrorsComplimentRightCut() < cutToIndex - cutFromIndex) {
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
        Iterator<Entry<int[], Answers>> leftCutResultIterator =
                leftCutResults.entrySet().iterator();
        Entry<int[], Answers> leftCutResult = leftCutResultIterator.next();
        Entry<int[], Answers>[] cutResults = (Entry<int[], Answers>[])
                Array.newInstance(leftCutResult.getClass(), 2);
        cutResults[0] = leftCutResult;

        rightCutResults.put(sentinel, new Answers(0,
                nrOfQuestions - cutIndex));
        Iterator<Entry<int[], Answers>> rightCutResultIterator =
                rightCutResults.entrySet().iterator();
        cutResults[1] = rightCutResultIterator.next();

        Answers correctAnswers = null;
        long totalNrOfSolutions = 0;
        while(leftCutResultIterator.hasNext() || rightCutResultIterator.hasNext()) {
            correctAnswers = cutResults[0].getValue();
            totalNrOfSolutions += getNrOfSolutions(nrOfStudents,
                    leftCutResultIterator, rightCutResultIterator,
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
            
            int nrOfEqualLeftCutResultKeys = getNrOfEqualCutResultKeys(nrOfStudents,
                    leftCutResultsIterator, cutResults, 0);
            int nrOfEqualRightCutResultKeys = getNrOfEqualCutResultKeys(nrOfStudents,
                    rightCutResultsIterator, cutResults, 1);

            return (long)nrOfEqualLeftCutResultKeys *(long)nrOfEqualRightCutResultKeys;
        }
    }
    
    private static int getNrOfEqualCutResultKeys(int nrOfStudents,
            Iterator<Entry<int[], Answers>> cutResultsIterator,
            Entry<int[], Answers>[] cutResults, int cutResultIndex) {
        int nrOfEqualCutResultKeys = 0;
        int[] initialCutResultKey = cutResults[cutResultIndex].getKey();
        while(compareIntegerArrays(initialCutResultKey,
                cutResults[cutResultIndex].getKey(), nrOfStudents) == 0) {
            nrOfEqualCutResultKeys++;
            if(!cutResultsIterator.hasNext()) {
                break;
            } else {
                cutResults[cutResultIndex] = cutResultsIterator.next();
            }
        }

        return nrOfEqualCutResultKeys;
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
