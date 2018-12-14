package assignment2;

/**
 *
 * @author chihab
 * @author N.C.M. van Nistelrooij
 */
public class Student {
    private final Answers answers;
    private final int score;

    private StudentCutInfo cutInfo;
    
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

    public void setStudentCutInfo(int cutFromIndex, int cutToIndex) {
        cutInfo = new StudentCutInfo(this, cutFromIndex, cutToIndex);
    }

    public int getMaxScoreCut() {
        return cutInfo.getMaxScoreCut();
    }

    public int getMaxNrOfErrorsCut() {
        return cutInfo.getMaxNrOfErrorsCut();
    }           
}
