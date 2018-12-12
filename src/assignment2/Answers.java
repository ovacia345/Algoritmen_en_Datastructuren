package assignment2;

import java.util.BitSet;

/**
 *
 * @author N.C.M. van Nistelrooij
 */
public class Answers {
    private final BitSet answers;
    private final int lastFlippedIndex;
    private final int nrOfQuestions;

    public Answers(BitSet answers, int lastFlippedIndex, int nrOfQuestions) {
        this.answers = answers;
        this.lastFlippedIndex = lastFlippedIndex;
        this.nrOfQuestions = nrOfQuestions;
    }

    public Answers(BitSet answers, int nrOfQuestions) {
        this(answers, -1, nrOfQuestions);
    }

    public Answers(long answers, int nrOfQuestions) {
        this(BitSet.valueOf(new long[]{answers}), nrOfQuestions);
    }

    public Answers get(int toIndex) {
        BitSet get = answers.get(nrOfQuestions - toIndex, nrOfQuestions);
        return new Answers(get, toIndex);
    }

    public Answers getCompliment(int toIndex) {
        BitSet compliment = answers.get(nrOfQuestions - toIndex, nrOfQuestions);
        compliment.flip(0, toIndex);
        return new Answers(compliment, toIndex);
    }

    public BitSet getBitSet() {
        return answers;
    }

    public int getLastFlippedIndex() {
        return lastFlippedIndex;
    }

    public int getNrOfQuestions() {
        return nrOfQuestions;
    }

    public Answers changeAnswer(int index) {
        BitSet changedAnswers = (BitSet)answers.clone();
        changedAnswers.flip(nrOfQuestions - 1 - index);
        return new Answers(changedAnswers, index, nrOfQuestions);
    }

    public int getNrOfDifferencesWith(Answers otherAnswers) {
        BitSet differences = (BitSet)answers.clone();
        differences.xor(otherAnswers.getBitSet());
        return differences.cardinality();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = answers.length(); i < nrOfQuestions; i++) {
            stringBuilder.append('0');
        }
        if(answers.length() > 0) {
            stringBuilder.append(Long.toBinaryString(answers.toLongArray()[0]));
        }
        return stringBuilder.toString();
    }
}
