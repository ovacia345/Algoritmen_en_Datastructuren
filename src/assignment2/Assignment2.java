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

/**
 * Class that runs the practicum assignment 2 algorithm.
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class Assignment2 {
    /**
     * The counter used for domain separation in the {@code SortedMap}s.
     */
    private static int counter = 0;

    /**
     * The method that runs the algorithm. It reads the problem from standard
     * input and writes the solution to standard output.
     */
    public static void runAssignment() {
        int nrOfStudents = IOHandler.readInteger();
        int nrOfQuestions = IOHandler.readInteger();

        // The array of students is read from standard input.
        Student[] students = readStudents(nrOfStudents, nrOfQuestions);

        if(nrOfQuestions == 1) { // The algorithm does not work when this holds.
            String solution = getOneQuestionSolution(students);
            IOHandler.write(solution);
        } else {
            // Index of start of right cut.
            int cutIndex = nrOfQuestions / 2;

            // Brute force on the left cut. It returns a {@code SortedMap} where
            // the values are the left cut answers and the keys are the
            // corresponding student scores on the left cut.
            SortedMap<int[], Answers> leftCutResults =
                    bruteForceCut(students, 0, cutIndex);

            // Brute force on the right cut. It returns a {@code SortedMap} where
            // the values are the right cut answers and the keys are the
            // corresponding student scores on the left cut.
            SortedMap<int[], Answers> rightCutResults =
                    bruteForceCut(students, cutIndex, nrOfQuestions);

            // The brute force results are combined and the solution is returned.
            String solution = combineResults(leftCutResults, rightCutResults,
                    nrOfStudents, nrOfQuestions, cutIndex);

            // The solution is written to standard output.
            IOHandler.write(solution);
        }
    }

    /**
     * This method initializes an array of students, where
     * each student has answers and a score.
     * @param nrOfStudents Number of students.
     * @param nrOfQuestions Number of questions.
     * @return An array of {@code Student}s.
     */
    private static Student[] readStudents(int nrOfStudents, int nrOfQuestions) {
        Student[] students = new Student[nrOfStudents];
        
        for(int i = 0; i < nrOfStudents; i++) {
            Answers answers = new Answers(IOHandler.readLong(), nrOfQuestions);
            int score = IOHandler.readInteger();            

            students[i] = new Student(answers, score);
        }
        
        return students;
    }

    /**
     * Returns the solution if there is only one question.
     * @param students The array of students.
     * @return A {@code String} representation of the solution.
     */
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

    /**
     * Brute forces a cut.     *
     * @param students number of students
     * @param cutFromIndex Inclusive index of the start of the cut
     * @param cutToIndex Exclusive index of the end of the cut.
     * @return A {@code SortedMap} where
     * the values are the cut answers and the keys are the
     * corresponding student scores on the left cut.
     */
    private static SortedMap<int[], Answers> bruteForceCut(Student[] students,
            int cutFromIndex, int cutToIndex) {
        for(Student student : students) {
            student.setCutInfo(cutFromIndex, cutToIndex);
        }
        // Students are sorted based on the minimal cut info member.
        Arrays.sort(students);

        // Student with the smallest cut info member.
        Student student = students[0];
        // Get all the possible cut answers given the cut info and cut answers
        // of {@code student}.
        Answers[] possibleCutAnswersArray = getPossibleCutAnswersArray(
                student, cutFromIndex, cutToIndex);

        // A red-black tree is used as data-structure.
        SortedMap<int[], Answers> bruteForceCutResults = new TreeMap<>(
                (a1, a2) -> compareIntegerArrays(a1, a2));
        for(Answers possibleCutAnswers : possibleCutAnswersArray) {
            // Array of (needed) student scores on the left cut given
            // {@code possibleCutAnswers} as left/right cut answers.
            int[] studentScoresLeftCut = getStudentScoresLeftCut(students,
                    cutFromIndex, cutToIndex, possibleCutAnswers);
            bruteForceCutResults.put(studentScoresLeftCut, possibleCutAnswers);
        }

        return bruteForceCutResults;
    }

    /**
     * Generates all the possible cut answers given the cut info and cut
     * answers of {@code student}.
     * @param student A student.
     * @param cutFromIndex Inclusive index of the start of the cut
     * @param cutToIndex Exclusive index of the end of the cut.
     * @return list of possible answer sets given a student and cut.
     */
    private static Answers[] getPossibleCutAnswersArray(Student student,
            int cutFromIndex, int cutToIndex) {
        // The answers of a given student in a certain cut.
        Answers studentCutAnswers = student.getAnswers().get(cutFromIndex, cutToIndex);        
       
        int nrOfQuestionsCut = cutToIndex - cutFromIndex;
        int studentMinNrOfErrorsCut = nrOfQuestionsCut - student.getMaxScoreCut();
        int studentMaxNrOfErrorsCut = student.getMaxNrOfErrorsCut();

        // If the score of the student is very low, possible cut answers are
        // derived from the complement of the student's cut answers.
        if(student.getMaxNrOfErrorsCut() > student.getMaxScoreCut()) {
            studentCutAnswers = studentCutAnswers.getComplement();
            studentMinNrOfErrorsCut = nrOfQuestionsCut - student.getMaxNrOfErrorsCut();
            studentMaxNrOfErrorsCut = student.getMaxScoreCut();
        }

        // The number of cut answers that have too few errors.
        int startNumber = sumBinomial(nrOfQuestionsCut, studentMinNrOfErrorsCut - 1);
        // The number of cut answers that have maximally
        // {@code studentMaxNrOfErrorsCut} errors.
        int endNumber = sumBinomial(nrOfQuestionsCut, studentMaxNrOfErrorsCut);
        // The number of possible cut answers is the difference between the two.
        int nrOfPossibleCutAnswers = endNumber - startNumber;
        Answers[] possibleCutAnswersArray = new Answers[nrOfPossibleCutAnswers];

        Queue<Answers> queue = new LinkedList<>();
        queue.offer(studentCutAnswers);
        
        // The following for loops adds all the possible cut answers to the array.
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

    /**
     * Calculates (needed) student scores on the left cut given possible
     * left/right cut answers.
     * @param students Array of students.
     * @param cutFromIndex Inclusive index of the start of the cut.
     * @param cutToIndex Exclusive index of the end of the cut.
     * @param possibleCutAnswers Possible left/right cut answers.
     * @return Array of (needed) student scores on the left cut.
     */
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

        // Domain seperation.
        studentScoresLeftCut[students.length] = counter++;

        return studentScoresLeftCut;
    }

    /**
     * Gets (needed) students score on the left cut given possible left/right
     * cut answers, the student and the student left/right cut answers.
     * @param possibleCutAnswers Possible left/right cut answers.
     * @param student Array of students.
     * @param studentCutAnswers The student's answers in the left/right cut.
     * @param cutFromIndex Inclusive index of the start of the cut.
     * @return the student's (needed) score in the left cut.
     */ 
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

    /**
     * Combines the brute force results to return the solution.
     * @param leftCutResults The results of the brute force on the left cut.
     * @param rightCutResults The results of the brute force on the right cut.
     * @param nrOfStudents The number of students.
     * @param nrOfQuestions The number of questions.
     * @param cutIndex Index of start of right cut.
     * @return A {@code String} representation of the solution.
     */
    private static String combineResults(
            SortedMap<int[], Answers> leftCutResults,
            SortedMap<int[], Answers> rightCutResults,
            int nrOfStudents, int nrOfQuestions, int cutIndex) {
        // An extra element is put at the back of {@code leftCutResults} and
        // {@code rightCutResults}.
        int[] sentinel = new int[nrOfStudents + 1];
        Arrays.fill(sentinel, Integer.MAX_VALUE);
        leftCutResults.put(sentinel, new Answers(0, cutIndex));
        rightCutResults.put(sentinel, new Answers(0, nrOfQuestions - cutIndex));

        // Create iterators for both cut results maps entries.
        Iterator<Entry<int[], Answers>> leftCutResultsIterator =
                leftCutResults.entrySet().iterator();
        Iterator<Entry<int[], Answers>> rightCutResultsIterator =
                rightCutResults.entrySet().iterator();
        
        // Create a cut results list and put first left and right cut results in
        // the list.
        List<Entry<int[], Answers>> cutResults = new ArrayList<>(2);
        cutResults.add(leftCutResultsIterator.next());
        cutResults.add(rightCutResultsIterator.next());

        Answers correctAnswers = null;
        long totalNrOfSolutions = 0;
        while(leftCutResultsIterator.hasNext() || rightCutResultsIterator.hasNext()) {
            // The (needed) left cut student scores are retreived from the cut results.
            int[] leftCutResultsStudentScores = cutResults.get(0).getKey();
            int[] rightCutResultsStudentScores = cutResults.get(1).getKey();

            int comparison = compareIntegerArrays(leftCutResultsStudentScores,
                    rightCutResultsStudentScores, nrOfStudents);
            if(comparison < 0 && leftCutResultsIterator.hasNext()) {
                // The left cut result is smaller than the right cut result,
                // so the next left cut result is put in {@code cutResults}.
                cutResults.set(0, leftCutResultsIterator.next());
            } else if(comparison > 0 && rightCutResultsIterator.hasNext()) {
                // The right cut result is smaller than the left cut result,
                // so the next right cut result is put in {@code cutResults}.
                cutResults.set(1, rightCutResultsIterator.next());
            } else { // A solution has been found.
                // The correct full answers are the answers of the left cut
                // concatenated with the answers of the right cut.
                if(correctAnswers ==  null) {
                    correctAnswers = cutResults.get(0).getValue()
                            .concatenate(cutResults.get(1).getValue());
                }

                // The number of equal (needed) left cut student scores for
                // each cut results iterator is calculated.
                int nrOfEqualLeftCutResultsKeys = getNrOfEqualCutResultsKeys(nrOfStudents,
                        leftCutResultsIterator, cutResults, 0);
                int nrOfEqualRightCutResultsKeys = getNrOfEqualCutResultsKeys(nrOfStudents,
                        rightCutResultsIterator, cutResults, 1);

                // The number of solutions with the current (needed) left cut
                // student scores is added to {@code totalNrOfSolutions}.
                totalNrOfSolutions += (long)nrOfEqualLeftCutResultsKeys *
                        (long)nrOfEqualRightCutResultsKeys;
            }
        }

        // The proper solution {@code String} representation is returned.
        if(totalNrOfSolutions == 1) {
            return correctAnswers.toString();
        } else {
            return totalNrOfSolutions + " solutions";
        }
    }

    /**
     * Gets the number of equal (needed) left cut student scores for the given
     * left/right cut results iterator.
     * @param nrOfStudents The number of students.
     * @param cutResultsIterator Iterator over the left/right cut results map entries.
     * @param cutResults The current cut results map entries. {@code cutResults[0]}
     * is the left cut results map entry and {@code cutResults[1]} is the right one.
     * @param cutResultIndex Index representing the cut. 0 = left, and 1 = right.
     * @return number of equal (needed) left cut student scores for the given
     * left/right cut results iterator.
     */
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
    
    /**
     * Computes the sum of binomial coefficients like (n 0) + (n 1) + ... +
     * (n k-1) + (n k).
     * @param n The top number in the binomial coefficient.
     * @param k The maximum bottom number in the binomial coefficient.
     * @return Sum of binomial coefficients.
     */
    private static int sumBinomial(int n, int k) {
        // Mathematically not sound, but it is needed for the assignment.
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

    /**
     * Compare two integer arrays up to a certain index.
     * @param a1 First integer array.
     * @param a2 Second integer array.
     * @param toIndex The exclusive index up to which the values of the arrays
     * should be compared.
     * @return -1 if {@code a1} is smaller than {@code a2}, 0 if they are
     * equal and 1 otherwise up to {@code toIndex}.
     */
    private static int compareIntegerArrays(int[] a1, int[] a2, int toIndex) {
        for(int i = 0; i < toIndex; i++) {
            if(a1[i] != a2[i]) {
                return Integer.compare(a1[i], a2[i]);
            }
        }
        return 0;
    }

    /**
     * Compare two integer arrays
     * @param a1 First integer array.
     * @param a2 Second integer array.
     * @return -1 if {@code a1} is smaller than {@code a2}, 0 if they are
     * equal and 1 otherwise.
     */
    private static int compareIntegerArrays(int[] a1, int[] a2) {
        int toIndex = Math.min(a1.length, a2.length);
        return compareIntegerArrays(a1, a2, toIndex);
    }
}
