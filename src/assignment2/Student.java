package assignment2;

/**
 * Class that stores the answers and score of a student. It also stores
 * the maximum score and the maximum number of errors for a given cut size.
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class Student implements Comparable<Student> {
    private final Answers answers;
    private final int score;
    private int maxScoreCut;
    private int maxNrOfErrorsCut;

    /**
     * The answers and score are initialized.
     * @param answers the answers the student gave.
     * @param score the score the student got.
     */
    public Student(Answers answers, int score){
        this.answers = answers;
        this.score = score;
    }
    
    public Answers getAnswers() {
        return answers;
    }

    public int getScore() {
        return score;
    }

    /**
     * This method sets the maximum score and the maximum number of errors
     * for the given cut indices.
     * @param cutFromIndex Inclusive index for the start of the cut.
     * @param cutToIndex Exclusive index for the end of the cut.
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
     * Students are compared based on their smallest cut info member.
     * @param otherStudent The student this student is compared to.
     * @return -1 if this student's smallest cut info member is smaller than the
     * other student's smallest cut info member, 0 if they are equal and 1 otherwise.
     */
    @Override
    public int compareTo(Student otherStudent) {
        int minCutInfoMember = Math.min(maxScoreCut, maxNrOfErrorsCut);
        int otherStudentMinCutInfoMember = Math.min(otherStudent.getMaxScoreCut(),
                otherStudent.getMaxNrOfErrorsCut());

        return Integer.compare(minCutInfoMember, otherStudentMinCutInfoMember);
    }
}
