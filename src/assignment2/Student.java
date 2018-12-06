package assignment2;

/**
 *
 * @author chihab
 * @author N.C.M. van Nistelrooij
 */
public class Student implements Comparable<Student> {
    private final BinaryString answers;
    private final int score;
    private final int nrOfCorrectAnswersLeftHalf, nrOfIncorrectAnswersLeftHalf;
    private int nrOfCorrectAnswersRightHalf, nrOfIncorrectAnswersRightHalf;
    
    public Student(int[] answers, int score,
            int nrOfCorrectAnswersLeftHalf, int nrOfIncorrectAnswersLeftHalf){
        this.answers = new BinaryString(answers, -1, 0);
        this.score = score;
        this.nrOfCorrectAnswersLeftHalf = nrOfCorrectAnswersLeftHalf;
        this.nrOfIncorrectAnswersLeftHalf = nrOfIncorrectAnswersLeftHalf;
    }
    
    public BinaryString getAnswers() {
        return answers;
    }

    public int getScore() {
        return score;
    }

    public int getNrOfCorrectAnswersLeftHalf() {
        return nrOfCorrectAnswersLeftHalf;
    }

    public int getNrOfIncorrectAnswersLeftHalf() {
        return nrOfIncorrectAnswersLeftHalf;
    }

    public void setNrOfCorrectAnswersRightHalf(int nrOfCorrectAnswersRightHalf) {
        this.nrOfCorrectAnswersRightHalf = nrOfCorrectAnswersRightHalf;
    }

    public int getNrOfCorrectAnswersRightHalf() {
        return nrOfCorrectAnswersRightHalf;
    }

    public void setNrOfIncorrectAnswersRightHalf(int nrOfIncorrectAnswersRightHalf) {
        this.nrOfIncorrectAnswersRightHalf = nrOfIncorrectAnswersRightHalf;
    }

    public int getNrOfIncorrectAnswersRightHalf() {
        return nrOfIncorrectAnswersRightHalf;
    }

    @Override
    public int compareTo(Student otherStudent) {
        int certaintyLeftHalf = nrOfCorrectAnswersLeftHalf
                + nrOfIncorrectAnswersLeftHalf;
        int otherStudentCertaintyLeftHalf = otherStudent.getNrOfCorrectAnswersLeftHalf()
                + otherStudent.getNrOfIncorrectAnswersLeftHalf();
        return Integer.compare(otherStudentCertaintyLeftHalf, certaintyLeftHalf);
    }
}
