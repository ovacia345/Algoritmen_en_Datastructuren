package assignment2;

/**
 *
 * @author N.C.M. van Nistelrooij
 */
public class StudentCutInfo {
    private final int maxScoreCut, maxNrOfErrorsCut;

    public StudentCutInfo(Student student, int cutFromIndex, int cutToIndex) {
        int score = student.getScore();
        int nrOfErrors = student.getAnswers().getNrOfQuestions() - score;

        int nrOfQuestionsCut = cutToIndex - cutFromIndex;

        maxScoreCut = Math.min(nrOfQuestionsCut, score);
        maxNrOfErrorsCut = Math.min(nrOfQuestionsCut, nrOfErrors);
    }

    public int getMaxScoreCut() {
        return maxScoreCut;
    }

    public int getMaxNrOfErrorsCut() {
        return maxNrOfErrorsCut;
    }
}
