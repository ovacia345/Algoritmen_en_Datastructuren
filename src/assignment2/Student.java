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
    }
    
    public Answers getAnswers() {
        return answers;
    }

    public int getScore() {
        return score;
    }

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

    @Override
    public int compareTo(Student otherStudent) {
        int minCutInfoMember = Math.min(maxScoreCut, maxNrOfErrorsCut);
        int otherStudentMinCutInfoMember = Math.min(otherStudent.getMaxScoreCut(),
                otherStudent.getMaxNrOfErrorsCut());

        return Integer.compare(minCutInfoMember, otherStudentMinCutInfoMember);
    }
}
