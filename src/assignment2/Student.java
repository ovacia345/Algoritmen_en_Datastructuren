package assignment2;

/**
 *
 * @author chihab
 * @author N.C.M. van Nistelrooij
 */
public class Student implements Comparable<Student> {
    private final Answers answers;
    private final int score;
    private int maxScoreCut;
    private int maxNrOfErrorsCut;
    
    public Student(Answers answers, int score){
        this.answers = answers;
        this.score = score;
        maxScoreCut = answers.getNrOfQuestions();
        maxNrOfErrorsCut = answers.getNrOfQuestions();
    }
    
    public Answers getAnswers() {
        return answers;
    }

    public int getScore() {
        return score;
    }

    /**
     * This method modifies variables dependent on the current cut,
     * e.g. the maximal score/errors in the current cut. 
     * @param cutFromIndex Index for the start of the cut
     * @param cutToIndex Index for the end of the cut
     */
    public void setCutInfo(int cutFromIndex, int cutToIndex) {
        int nrOfQuestionsCut = cutToIndex - cutFromIndex;

        maxScoreCut = Math.min(nrOfQuestionsCut, score);

        int nrOfErrors = answers.getNrOfQuestions() - score;
        maxNrOfErrorsCut = Math.min(nrOfQuestionsCut, nrOfErrors);
    }

    public int getMaxScoreCut() {
        return maxScoreCut;
    }

    public int getMaxNrOfErrorsCut() {
        return maxNrOfErrorsCut;
    }           

    /**
     * Students are compared based on either the maximal score or errors of a current cut.
     * @param otherStudent
     * @return -1 if smaller, 0 if equal else 1
     */
    @Override
    public int compareTo(Student otherStudent) {
        int minCutInfoMember = Math.min(maxScoreCut, maxNrOfErrorsCut);
        int otherStudentMinCutInfoMember = Math.min(otherStudent.getMaxScoreCut(),
                otherStudent.getMaxNrOfErrorsCut());

        return Integer.compare(minCutInfoMember, otherStudentMinCutInfoMember);
    }
}
