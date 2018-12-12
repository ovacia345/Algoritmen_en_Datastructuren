package assignment2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author N.C.M. van Nistelrooij
 */
public class MaxKDifferencesAnswersIterator implements Iterator<Answers> {
    private final Answers initialAnswers;
    private final int k;
    private final Queue<Answers> queue;

    public MaxKDifferencesAnswersIterator(Answers answers, int k) {
        this.initialAnswers = answers;
        this.k = k;
        queue = new LinkedList<>();
        queue.offer(answers);
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty() &&
                queue.peek().getNrOfDifferencesWith(initialAnswers) <= k;
    }

    @Override
    public Answers next() {
        Answers answers = queue.poll();

        for(int i = answers.getLastFlippedIndex() + 1;
                i < answers.getNrOfQuestions(); i++) {
            Answers changedAnswers = answers.changeAnswer(i);
            queue.offer(changedAnswers);
        }

        return answers;
    }
}
