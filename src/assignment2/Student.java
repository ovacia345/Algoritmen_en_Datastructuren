package assignment2;

/**
 *
 * @author chihab
 * @author N.C.M. van Nistelrooij
 */
public class Student implements Comparable<Student> {
    private final Answers answers;
    private final int score;
    private final int maxErrorsLeftHalf, maxErrorsComplimentLeftHalf;
    
    public Student(Answers answers, int score,
            int maxErrorsLeftHalf, int maxErrorsComplimentLeftHalf){
        this.answers = answers;
        this.score = score;
        this.maxErrorsLeftHalf = maxErrorsLeftHalf;
        this.maxErrorsComplimentLeftHalf = maxErrorsComplimentLeftHalf;
    }
    
    public Answers getAnswers() {
        return answers;
    }

    public int getScore() {
        return score;
    }

    public int getMaxErrorsLeftHalf() {
        return maxErrorsLeftHalf;
    }

    public int getMaxErrorsComplimentLeftHalf() {
        return maxErrorsComplimentLeftHalf;
    }

    @Override
    public int compareTo(Student otherStudent) {
        int certaintyLeftHalf = maxErrorsLeftHalf + maxErrorsComplimentLeftHalf;
        int otherStudentCertaintyLeftHalf = otherStudent.getMaxErrorsLeftHalf()
                + otherStudent.getMaxErrorsComplimentLeftHalf();
        return Integer.compare(certaintyLeftHalf, otherStudentCertaintyLeftHalf);
    }
}
