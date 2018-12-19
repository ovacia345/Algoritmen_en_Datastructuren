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

        if(nrOfQuestions == 1) {
            String solution = getOneQuestionSolution(nrOfStudents, nrOfQuestions,
                    students);
            IOHandler.write(solution);
        } else {
            int cutIndex = nrOfQuestions / 2; // index start of right cut

            SortedMap<int[], Answers> leftCutResults =
                    bruteForceCut(nrOfQuestions, students, 0, Math.max(1, cutIndex));

            SortedMap<int[], Answers> rightCutResults =
                    bruteForceCut(nrOfQuestions, students, cutIndex, nrOfQuestions);

            String solution = combineResults(leftCutResults, rightCutResults,
                    nrOfStudents, nrOfQuestions, cutIndex);
            IOHandler.write(solution);
        }
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

    private static String getOneQuestionSolution(int nrOfStudents, int nrOfQuestions,
            Student[] students) {
        int answer = (students[0].getScore() +
                students[0].getAnswers().getBitSet().length()) % 2;
        for(Student student : students) {
            if(answer != (student.getScore() +
                    student.getAnswers().getBitSet().length()) % 2) {
                return "0 solutions";
            }
        }

        return Integer.toString(1 - answer);
    }

    private static SortedMap<int[], Answers> bruteForceCut(int nrOfQuestions,
            Student[] students, int cutFromIndex, int cutToIndex) {
        for(Student student : students) {
            student.setCutInfo(cutFromIndex, cutToIndex);
        }
        Arrays.sort(students);

        Student student = students[0];
        Answers[] possibleCutAnswersArray = getPossibleCutAnswersArray(
                student, cutFromIndex, cutToIndex);

        SortedMap<int[], Answers> bruteForceCutResults = new TreeMap<>(
                (a1, a2) -> compareIntegerArrays(a1, a2));
        for(Answers possibleCutAnswers : possibleCutAnswersArray) {
            int[] studentScoresLeftCut = getStudentScoresLeftCut(students,
                    cutFromIndex, cutToIndex, possibleCutAnswers);
            studentScoresLeftCut[students.length] = counter++;
            bruteForceCutResults.put(studentScoresLeftCut, possibleCutAnswers);
        }

        return bruteForceCutResults;
    }

    private static Answers[] getPossibleCutAnswersArray(Student student,
            int cutFromIndex, int cutToIndex) {
        Answers studentCutAnswers = student.getAnswers().get(cutFromIndex, cutToIndex);

        int nrOfQuestionsCut = cutToIndex - cutFromIndex;
        int studentMinNrOfErrorsCut = nrOfQuestionsCut - student.getMaxScoreCut();
        int studentMaxNrOfErrorsCut = student.getMaxNrOfErrorsCut();

        if(student.getMaxNrOfErrorsCut() > student.getMaxScoreCut()) {
            studentCutAnswers = studentCutAnswers.getComplement();
            studentMinNrOfErrorsCut = nrOfQuestionsCut - student.getMaxNrOfErrorsCut();
            studentMaxNrOfErrorsCut = student.getMaxScoreCut();
        }

        int startNumber = sumBinomial(nrOfQuestionsCut, studentMinNrOfErrorsCut - 1);
        int endNumber = sumBinomial(nrOfQuestionsCut, studentMaxNrOfErrorsCut);

        int nrOfPossibleCutAnswers = endNumber - startNumber;
        Answers[] possibleCutAnswersArray = new Answers[nrOfPossibleCutAnswers];

        Queue<Answers> queue = new LinkedList<>();
        queue.offer(studentCutAnswers);

        for(int i = 0; i < endNumber; i++) {
            Answers possibleCutAnswers = queue.poll();

            for(int j = possibleCutAnswers.getLastChangedAnswerIndex() + 1;
                    j < nrOfQuestionsCut; j++) {
                Answers changedAnswers = possibleCutAnswers.changeAnswer(j);
                queue.offer(changedAnswers);
            }

            if(i >= startNumber) {
                possibleCutAnswersArray[i - startNumber] = possibleCutAnswers;
            }    
        }

        return possibleCutAnswersArray;
    }

    private static int[] getStudentScoresLeftCut(Student[] students,
            int cutFromIndex, int cutToIndex, Answers possibleCutAnswers) {
        int[] studentScoresLeftCut = new int[students.length + 1];

        for(int i = 0; i < students.length; i++) {
            Student student = students[i];
            Answers studentCutAnswers = student.getAnswers()
                    .get(cutFromIndex, cutToIndex);

            studentScoresLeftCut[i] = getStudentScoreLeftCut(possibleCutAnswers,
                    student, studentCutAnswers, cutFromIndex);
        }

        return studentScoresLeftCut;
    }

    private static int getStudentScoreLeftCut(Answers possibleCutAnswers,
            Student student, Answers studentCutAnswers, int cutFromIndex) {
        int studentScoreCut = studentCutAnswers.getNrOfEqualAnswersWith(
                        possibleCutAnswers);
        if(cutFromIndex == 0) { // Left cut
            return studentScoreCut;
        } else { // Right cut
            return student.getScore() - studentScoreCut;
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

        Answers correctAnswers = null;
        long totalNrOfSolutions = 0;
        while(leftCutResultsIterator.hasNext() || rightCutResultsIterator.hasNext()) {
            int[] bruteForceLeftCutStudentScoresLeftCut = cutResults[0].getKey();
            int[] bruteForceRightCutStudentScoresLeftCut = cutResults[1].getKey();
            int comparison = compareIntegerArrays(bruteForceLeftCutStudentScoresLeftCut,
                    bruteForceRightCutStudentScoresLeftCut, nrOfStudents);
            if(comparison < 0 && leftCutResultsIterator.hasNext()) {
                cutResults[0] = leftCutResultsIterator.next();
            } else if(comparison > 0 && rightCutResultsIterator.hasNext()) {
                cutResults[1] = rightCutResultsIterator.next();
            } else {
                correctAnswers = cutResults[0].getValue();
                if(nrOfQuestions > 1) {
                    correctAnswers = correctAnswers.concatenate(cutResults[1].getValue());
                }

                int nrOfEqualLeftCutResultsKeys = getNrOfEqualCutResultsKeys(nrOfStudents,
                        leftCutResultsIterator, cutResults, 0);
                int nrOfEqualRightCutResultsKeys = getNrOfEqualCutResultsKeys(nrOfStudents,
                        rightCutResultsIterator, cutResults, 1);

                totalNrOfSolutions += (long)nrOfEqualLeftCutResultsKeys *
                        (long)nrOfEqualRightCutResultsKeys;
            }
        }
        
        if(totalNrOfSolutions == 1) {
            return correctAnswers.toString();
        } else {
            return totalNrOfSolutions + " solutions";
        }
    }
    
    private static int getNrOfEqualCutResultsKeys(int nrOfStudents,
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
        if(k == -1) {
            return 0;
        }

        int binomial = 1;
        int sum = 1;
        for(int i = 1, m = n; i <= k; i++, m--) {
            binomial = binomial * m / i;
            sum += binomial;
        }

        return sum;
    }

    private static int compareIntegerArrays(int[] a1, int[] a2, int toIndex) {
        for(int i = 0; i < toIndex; i++) {
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
