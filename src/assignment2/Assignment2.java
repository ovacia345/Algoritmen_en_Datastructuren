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

//        long exhaustiveTime = System.currentTimeMillis();
//        SortedMap<int[], Answers> exhaustiveResults =
//                bruteForceCut(nrOfQuestions, students, 0, nrOfQuestions);
//
//        String exhaustiveSolution = exhaustiveSearch(students, exhaustiveResults,
//                nrOfStudents);
//        exhaustiveTime = System.currentTimeMillis() - exhaustiveTime;
//        IOHandler.write(exhaustiveSolution);
//        IOHandler.write(String.format("Exhaustive time: %dms", exhaustiveTime));


        long fastTime = System.currentTimeMillis();
        int cutIndex = nrOfQuestions / 2; // index start of right cut
        SortedMap<int[], Answers> leftCutResults =
                bruteForceCut(nrOfQuestions, students, 0, Math.max(1, cutIndex));
        SortedMap<int[], Answers> rightCutResults =
                bruteForceCut(nrOfQuestions, students, cutIndex, nrOfQuestions);

        String solution = combineResults(leftCutResults, rightCutResults,
                nrOfStudents, nrOfQuestions, cutIndex);
        fastTime = System.currentTimeMillis() - fastTime;
        IOHandler.write(solution);
//        IOHandler.write(String.format("Algorithm time: %dms", fastTime));
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
        if(cutFromIndex != 0 || cutToIndex != nrOfQuestions || cutToIndex == 1) {
            for(Student student : students) {
                student.setCutInfo(cutFromIndex, cutToIndex);
            }
            Arrays.sort(students);
        }

        Student student = students[0];
        Answers[] possibleCutAnswersArray = getPossibleCutAnswersArray(
                student, cutFromIndex, cutToIndex);

        SortedMap<int[], Answers> bruteForceCutResults = new TreeMap<>(
                (a1, a2) -> compareIntegerArrays(a1, a2));
        for(Answers possibleCutAnswers : possibleCutAnswersArray) {
            int[] studentScoresLeftCut = getStudentScoresLeftCut(students,
                    cutFromIndex, cutToIndex, possibleCutAnswers);
            if(studentScoresLeftCut != null) {
                studentScoresLeftCut[students.length] = counter++;
                bruteForceCutResults.put(studentScoresLeftCut, possibleCutAnswers);
            }
        }

        return bruteForceCutResults;
    }

    private static Answers[] getPossibleCutAnswersArray(Student student,
            int cutFromIndex, int cutToIndex) {
        Answers studentCutAnswers = student.getAnswers().get(cutFromIndex, cutToIndex);

        int nrOfQuestionsCut = cutToIndex - cutFromIndex;
        int studentMinNrOfErrorsCut = nrOfQuestionsCut - student.getMaxScoreCut();
        int studentMaxNrOfErrorsCut = student.getMaxNrOfErrorsCut();

        if(studentMaxNrOfErrorsCut > student.getMaxScoreCut()) {
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
            Answers studentCutAnswers = student.getAnswers().get(cutFromIndex, cutToIndex);

            if(!arePotentialStudentCutAnswers(possibleCutAnswers, student,
                    studentCutAnswers, cutFromIndex, cutToIndex)) {
                return null;
            }

            studentScoresLeftCut[i] = getStudentScoreLeftCut(possibleCutAnswers,
                    student, studentCutAnswers, cutFromIndex, cutToIndex);
        }

        return studentScoresLeftCut;
    }

    private static boolean arePotentialStudentCutAnswers(Answers possibleCutAnswers,
            Student student, Answers studentCutAnswers, int cutFromIndex, int cutToIndex) {
        int studentScoreCut = studentCutAnswers.getNrOfEqualAnswersWith(
                possibleCutAnswers);
        int studentNrOfErrorsCut = studentCutAnswers.getNrOfQuestions() - studentScoreCut;
        return studentScoreCut <= student.getMaxScoreCut() &&
                studentNrOfErrorsCut <= student.getMaxNrOfErrorsCut();
    }

    private static int getStudentScoreLeftCut(Answers potentialStudentCutAnswers,
            Student student, Answers studentCutAnswers, int cutFromIndex, int cutToIndex) {
        int studentScoreCut = studentCutAnswers.getNrOfEqualAnswersWith(
                        potentialStudentCutAnswers);
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
            int comparison = compareIntegerArrays(cutResults[0].getKey(),
                    cutResults[1].getKey(), nrOfStudents);
            if(comparison < 0 && leftCutResultsIterator.hasNext()) {
                cutResults[0] = leftCutResultsIterator.next();
            } else if(comparison > 0 && rightCutResultsIterator.hasNext()) {
                cutResults[1] = rightCutResultsIterator.next();
            } else {
                correctAnswers = cutResults[0].getValue();
                if(cutIndex != 0) {
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

    private static String exhaustiveSearch(Student[] students,
            SortedMap<int[], Answers> potentialAnswersMap,
            int nrOfStudents) {
        int[] studentScores = getStudentScores(students, nrOfStudents);
        int nrOfSolutions = 0;
        Answers correctAnswers = null;
        for(Entry<int[], Answers> potentialAnswers : potentialAnswersMap.entrySet()) {
            if(compareIntegerArrays(studentScores, potentialAnswers.getKey(),
                    nrOfStudents) == 0) {
                correctAnswers = potentialAnswers.getValue();
                nrOfSolutions++;
            }
        }

        if(nrOfSolutions == 1) {
            return correctAnswers.toString();
        } else {
            return nrOfSolutions + " solutions";
        }
    }

    private static int[] getStudentScores(Student[] students, int nrOfStudents) {
        int[] studentScores = new int[nrOfStudents];
        for(int i = 0; i < nrOfStudents; i++) {
            studentScores[i] = students[i].getScore();
        }

        return studentScores;
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
