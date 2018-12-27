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
    /**
     * 
     */
    public static void runAssignment() {
        //reading nr of students  
        int nrOfStudents = IOHandler.readInteger();
        //reaing nr of questions
        int nrOfQuestions = IOHandler.readInteger();
        //list of students
        Student[] students = readStudents(nrOfStudents, nrOfQuestions);

        if(nrOfQuestions == 1) {
            String solution = getOneQuestionSolution(students);
            IOHandler.write(solution);
        } else {
            int cutIndex = nrOfQuestions / 2; // index start of right cut
            // two maps are created, each map represents the bruteforce results of a cut.
            // The maps contain a list of integers and answersets, 
            // the integer lists represent the scores
            // the answersets represent the possible answers in a cut.
            
            SortedMap<int[], Answers> leftCutResults =
                    bruteForceCut(nrOfQuestions, students, 0, Math.max(1, cutIndex));

            SortedMap<int[], Answers> rightCutResults =
                    bruteForceCut(nrOfQuestions, students, cutIndex, nrOfQuestions);

            String solution = combineResults(leftCutResults, rightCutResults,
                    nrOfStudents, nrOfQuestions, cutIndex);
            IOHandler.write(solution);
        }
    }
    /**
     * This method creates an list of students,
     * each student has a score and set of answers.
     * @param nrOfStudents number of students
     * @param nrOfQuestions number of questions
     * @return a list of students
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
     * This method returns the solution of there is only one question
     * @param students the number of students
     * @return A solution if there exists one, else 0 solutions
     */
    private static String getOneQuestionSolution(Student[] students) {
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

    /**
     * This method is responsible for bruteforce in a cut
     * 
     * @param nrOfQuestions number of questions 
     * @param students number of students
     * @param cutFromIndex starting index of a cut
     * @param cutToIndex ending index of a cut
     * @return possible answers for questions in the cut
     */
    private static SortedMap<int[], Answers> bruteForceCut(int nrOfQuestions,
            Student[] students, int cutFromIndex, int cutToIndex) {
        //create cuts for each student
        for(Student student : students) {
            student.setCutInfo(cutFromIndex, cutToIndex);
        }
        //sort the students, students are compared based on max or minimal score in a cut
        Arrays.sort(students);
        
        Student student = students[0];
        // Get all the possible answer sets.
        Answers[] possibleCutAnswersArray = getPossibleCutAnswersArray(
                student, cutFromIndex, cutToIndex);
        // Bruteforce the possibilities to find 
        SortedMap<int[], Answers> bruteForceCutResults = new TreeMap<>(
                (a1, a2) -> compareIntegerArrays(a1, a2));
        for(Answers possibleCutAnswers : possibleCutAnswersArray) {
            //List of scores corresponding to each possible answer set, and a counter to make sure no duplicate entries exist in this list
            int[] studentScoresLeftCut = getStudentScoresLeftCut(students,
                    cutFromIndex, cutToIndex, possibleCutAnswers);
            studentScoresLeftCut[students.length] = counter++;
            bruteForceCutResults.put(studentScoresLeftCut, possibleCutAnswers);
        }

        return bruteForceCutResults;
    }

    /**
     * This method generates the possible answer sets of a certain cut
     * @param student a student
     * @param cutFromIndex the starting index of a cut
     * @param cutToIndex the ending index of a cut
     * @return list of possible answer sets given a student and cut.
     */
    private static Answers[] getPossibleCutAnswersArray(Student student,
            int cutFromIndex, int cutToIndex) {
        //The answers of a given student in a certain cut
        Answers studentCutAnswers = student.getAnswers().get(cutFromIndex, cutToIndex);
        
       
        int nrOfQuestionsCut = cutToIndex - cutFromIndex;
        //max score cut is min(nrofquestionsincut, score)
        // the minimal number of errors in a cut is the nr of questions in that cut minus the maximal score in that cut
        int studentMinNrOfErrorsCut = nrOfQuestionsCut - student.getMaxScoreCut();
        // the maximal number of errors in a cut is the smallest value in the nr of errors or the number of questions in a cut 
        int studentMaxNrOfErrorsCut = student.getMaxNrOfErrorsCut();
        
        // if the max nr of errors in a cut is higher than the max score in a cut 
        // then the score is very low and the minimal amount or errors in a cut is nr of questions in that cut minus maximal errors in the cut
        // the maximum nr of errors in the cut will be maximum score of a cut. Because in this situation the maxnroferrorscut will be the total nr of errors 
        // and the maxscorecut will be the nr of questions in the cut
        if(student.getMaxNrOfErrorsCut() > student.getMaxScoreCut()) {
            studentCutAnswers = studentCutAnswers.getComplement();
            studentMinNrOfErrorsCut = nrOfQuestionsCut - student.getMaxNrOfErrorsCut();
            studentMaxNrOfErrorsCut = student.getMaxScoreCut();
        }

        
        int startNumber = sumBinomial(nrOfQuestionsCut, studentMinNrOfErrorsCut - 1);
        int endNumber = sumBinomial(nrOfQuestionsCut, studentMaxNrOfErrorsCut);
        // the startNumber and endNumber variables represent the number of possible answer sets given the min or max number of errors in a cut
        // the actual nr of possible answers in a cut is the difference between the two.
        int nrOfPossibleCutAnswers = endNumber - startNumber;
        Answers[] possibleCutAnswersArray = new Answers[nrOfPossibleCutAnswers];

        Queue<Answers> queue = new LinkedList<>();
        queue.offer(studentCutAnswers);
        
        // The following for loops adds all the possible answersets to the queue.
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
    /**
     * This method calculates scores corresponding to possible answers in a cut 
     * @param students list of students
     * @param cutFromIndex starting index of a cut
     * @param cutToIndex ending index of a cut
     * @param possibleCutAnswers sets of possible answers of a cut
     * @return a list of integers representing scores of possible answers sets in a cut.
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

        return studentScoresLeftCut;
    }
    /**
     * This method returns the score of a certain cut.
     * @param possibleCutAnswers set of possible answers in a certain cut
     * @param student a student
     * @param studentCutAnswers the students answers in certain cut
     * @param cutFromIndex the starting index of a certain cut
     * @return the students' score in a certain cut
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

    
    private static String combineResults(
            SortedMap<int[], Answers> leftCutResults,
            SortedMap<int[], Answers> rightCutResults,
            int nrOfStudents, int nrOfQuestions, int cutIndex) {
        // sentinel is used to remember the last "visited" index
        int[] sentinel = new int[nrOfStudents + 1];
        Arrays.fill(sentinel, Integer.MAX_VALUE);

        //create iterators for both cuts and add empty answers lists with sentinel value to left and right cutresults list
        
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
        // loop through the left and right cut 
        // compare the left and right cut integer arrays to eachother 
        // if they are equal then we have found a possible solution, add solution and continue
        // else move on to nect left or right cut result depending on comparison output.
        while(leftCutResultsIterator.hasNext() || rightCutResultsIterator.hasNext()) {
            int[] bruteForceLeftCutStudentScores = cutResults[0].getKey();
            int[] bruteForceRightCutStudentScores = cutResults[1].getKey();
            int comparison = compareIntegerArrays(bruteForceLeftCutStudentScores,
                    bruteForceRightCutStudentScores, nrOfStudents);
            if(comparison < 0 && leftCutResultsIterator.hasNext()) {
                cutResults[0] = leftCutResultsIterator.next();
            } else if(comparison > 0 && rightCutResultsIterator.hasNext()) {
                cutResults[1] = rightCutResultsIterator.next();
            } else {
                correctAnswers = cutResults[0].getValue()
                        .concatenate(cutResults[1].getValue());

                int nrOfEqualLeftCutResultsKeys = getNrOfEqualCutResultsKeys(nrOfStudents,
                        leftCutResultsIterator, cutResults, 0);
                int nrOfEqualRightCutResultsKeys = getNrOfEqualCutResultsKeys(nrOfStudents,
                        rightCutResultsIterator, cutResults, 1);

                totalNrOfSolutions += (long)nrOfEqualLeftCutResultsKeys *
                        (long)nrOfEqualRightCutResultsKeys;
            }
        }
        // return solution(s)
        if(totalNrOfSolutions == 1) {
            return correctAnswers.toString();
        } else {
            return totalNrOfSolutions + " solutions";
        }
    }
    /**
     * This method returns the number of cuts with equal scores..
     * @param nrOfStudents number of students
     * @param cutResultsIterator iterator over a map of scores and answersets
     * @param cutResults a map containing the scores and answers of a cut
     * @param cutResultIndex index representing the cut, i.e. 0 = left, 1 = right 
     * @return integer representing the number of equal cuts, e.g. answer sets with equal scores
     */
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
    
    /**
     * This method returns the number of possible solution sets 
     * @param n number of question in the cut
     * @param k score/number of errors
     * @return number of possible solutions.
     */
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
    /**
     * Compare two arrays containing integers up to a certain index
     * @param a1 array of integers
     * @param a2 array of integers
     * @param toIndex the index up to which the values of the arrays should be compared
     * @return 0 if equal, -1 if smaller else 1
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
     * Compare two arrays containing integers 
     * @param a1
     * @param a2
     * @return 0 if equal, -1 if smaller else 1
     */
    private static int compareIntegerArrays(int[] a1, int[] a2) {
        int toIndex = Math.min(a1.length, a2.length);
        return compareIntegerArrays(a1, a2, toIndex);
    }
}
