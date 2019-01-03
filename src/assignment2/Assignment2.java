package assignment2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;

public class Assignment2 {
    private static int counter = 0;

    public static void runAssignment() {
        int nrOfStudents = IOHandler.readInteger();
        int nrOfQuestions = IOHandler.readInteger();

        Student[] students = readStudents(nrOfStudents, nrOfQuestions);

        if(nrOfQuestions == 1) {
            String solution = getOneQuestionSolution(students);
            IOHandler.write(solution);
        } else {
            int cutIndex = nrOfQuestions / 2;

            SortedMap<int[], Answers> leftCutResults =
                    bruteForceCut(students, 0, cutIndex);
            SortedMap<int[], Answers> rightCutResults =
                    bruteForceCut(students, cutIndex, nrOfQuestions);

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

    private static String getOneQuestionSolution(Student[] students) {
        int answerComplement = (students[0].getScore() +
                students[0].getAnswers().getBitSet().length()) % 2;
        for(Student student : students) {
            if(answerComplement != (student.getScore() +
                    student.getAnswers().getBitSet().length()) % 2) {
                return "0 solutions";
            }
        }

        return Integer.toString(1 - answerComplement);
    }

    private static SortedMap<int[], Answers> bruteForceCut(Student[] students,
            int cutFromIndex, int cutToIndex) {
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
            Answers cutAnswers = queue.poll();

            for(int j = cutAnswers.getLastChangedAnswerIndex() + 1;
                    j < nrOfQuestionsCut; j++) {
                Answers changedCutAnswers = cutAnswers.changeAnswer(j);
                queue.offer(changedCutAnswers);
            }

            if(i >= startNumber) {
                possibleCutAnswersArray[i - startNumber] = cutAnswers;
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

        studentScoresLeftCut[students.length] = counter++;

        return studentScoresLeftCut;
    }

    private static int getStudentScoreLeftCut(Answers possibleCutAnswers,
            Student student, Answers studentCutAnswers, int cutFromIndex) {
        int studentScoreCut = studentCutAnswers.getNrOfEqualAnswers(
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
        rightCutResults.put(sentinel, new Answers(0, nrOfQuestions - cutIndex));

        Iterator<Entry<int[], Answers>> leftCutResultsIterator =
                leftCutResults.entrySet().iterator();
        Iterator<Entry<int[], Answers>> rightCutResultsIterator =
                rightCutResults.entrySet().iterator();
        
        List<Entry<int[], Answers>> cutResults = new ArrayList<>(2);
        cutResults.add(leftCutResultsIterator.next());
        cutResults.add(rightCutResultsIterator.next());

        Answers correctAnswers = null;
        long totalNrOfSolutions = 0;
        while(leftCutResultsIterator.hasNext() || rightCutResultsIterator.hasNext()) {
            int[] leftCutResultsStudentScores = cutResults.get(0).getKey();
            int[] rightCutResultsStudentScores = cutResults.get(1).getKey();

            int comparison = compareIntegerArrays(leftCutResultsStudentScores,
                    rightCutResultsStudentScores, nrOfStudents);
            if(comparison < 0 && leftCutResultsIterator.hasNext()) {
                cutResults.set(0, leftCutResultsIterator.next());
            } else if(comparison > 0 && rightCutResultsIterator.hasNext()) {
                cutResults.set(1, rightCutResultsIterator.next());
            } else {
                if(correctAnswers == null) {
                    correctAnswers = cutResults.get(0).getValue()
                        .concatenate(cutResults.get(1).getValue());
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
            List<Entry<int[], Answers>> cutResults, int cutResultIndex) {
        int nrOfEqualCutResultsKeys = 0;
        int[] initialCutResultKey = cutResults.get(cutResultIndex).getKey();
        while(compareIntegerArrays(initialCutResultKey,
                cutResults.get(cutResultIndex).getKey(), nrOfStudents) == 0) {
            nrOfEqualCutResultsKeys++;
            if(!cutResultsIterator.hasNext()) {
                return nrOfEqualCutResultsKeys;
            } else {
                cutResults.set(cutResultIndex, cutResultsIterator.next());
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
