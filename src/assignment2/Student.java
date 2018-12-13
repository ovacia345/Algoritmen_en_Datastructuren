package assignment2;

/**
 *
 * @author chihab
 * @author N.C.M. van Nistelrooij
 */
public class Student {
    private final Answers answers;
    private final int score;
    private final int maxErrorsLeftCut, maxErrorsComplimentLeftCut;
    private final int maxErrorsRightCut, maxErrorsComplimentRightCut;
    
    public Student(Answers answers, int score, int cutIndex){
        this.answers = answers;
        this.score = score;

        int leftCutWidth = cutIndex;
        int rightCutWidth = answers.getNrOfQuestions() - cutIndex;

        maxErrorsLeftCut = Math.min(leftCutWidth, answers.getNrOfQuestions() - score);
        maxErrorsComplimentLeftCut = Math.min(leftCutWidth, score);

        maxErrorsRightCut = Math.min(rightCutWidth, answers.getNrOfQuestions() - score);
        maxErrorsComplimentRightCut = Math.min(rightCutWidth, score);
    }
    
    public Answers getAnswers() {
        return answers;
    }

    public int getScore() {
        return score;
    }

    public int getMaxErrorsLeftCut() {
        return maxErrorsLeftCut;
    }

    public int getMaxErrorsComplimentLeftCut() {
        return maxErrorsComplimentLeftCut;
    }

    public int getMaxErrorsRightCut() {
        return maxErrorsRightCut;
    }

    public int getMaxErrorsComplimentRightCut() {
        return maxErrorsComplimentRightCut;
    }
}
