package assignment2;

/**
 *
 * @author chihab
 * @author N.C.M. van Nistelrooij
 */
public class Student implements Comparable<Student> {
    private final int[] answers;
    private final int score;
    
    public Student(int[] answers, int score){
        this.answers = answers;
        this.score = score;
    }
    
    public int[] getAnswers() {
        return answers;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Student otherStudent) {
        return Integer.compare(score, otherStudent.getScore());
    }
}
