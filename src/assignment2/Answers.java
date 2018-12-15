package assignment2;

import java.util.BitSet;

/**
 *
 * @author N.C.M. van Nistelrooij
 */
public class Answers {
    private final int lastChangedAnswerIndex;
    private int nrOfQuestions;
    private BitSet bitSet;

    public Answers(BitSet bitSet, int lastChangedAnswerIndex, int nrOfQuestions) {
        this.bitSet = bitSet;
        this.lastChangedAnswerIndex = lastChangedAnswerIndex;
        this.nrOfQuestions = nrOfQuestions;
    }

    public Answers(BitSet bitSet, int nrOfQuestions) {
        this(bitSet, -1, nrOfQuestions);
    }

    public Answers(long answers, int nrOfQuestions) {
        this(BitSet.valueOf(new long[]{answers}), nrOfQuestions);
    }

    public Answers get(int fromIndex, int toIndex) {
        BitSet answers = bitSet.get(nrOfQuestions - toIndex, nrOfQuestions - fromIndex);
        return new Answers(answers, toIndex - fromIndex);
    }

    public Answers getComplement(int fromIndex, int toIndex) {
        BitSet complement = bitSet.get(nrOfQuestions - toIndex, nrOfQuestions - fromIndex);
        complement.flip(0, toIndex - fromIndex);
        return new Answers(complement, toIndex - fromIndex);
    }

    public Answers getComplement() {
        return getComplement(0, nrOfQuestions);
    }

    public int getLastChangedAnswerIndex() {
        return lastChangedAnswerIndex;
    }

    public int getNrOfQuestions() {
        return nrOfQuestions;
    }


    public BitSet getBitSet() {
        return bitSet;
    }

    public Answers changeAnswer(int index) {
        BitSet changedAnswers = (BitSet)bitSet.clone();
        changedAnswers.flip(nrOfQuestions - 1 - index);
        return new Answers(changedAnswers, index, nrOfQuestions);
    }

    public int getNrOfDifferentAnswersWith(Answers otherAnswers) {
        BitSet differences = (BitSet)bitSet.clone();
        differences.xor(otherAnswers.getBitSet());
        return differences.cardinality();
    }

    public int getNrOfEqualAnswersWith(Answers otherAnswers) {
        return nrOfQuestions - getNrOfDifferentAnswersWith(otherAnswers);
    }

    public Answers concatenate(Answers otherAnswers) {
        long answersLong = bitSet.length() > 0 ? bitSet.toLongArray()[0] : 0;
        long otherAnswersLong = otherAnswers.getBitSet().length() > 0
                ? otherAnswers.getBitSet().toLongArray()[0] : 0;

        int nrOfOtherQuestions = otherAnswers.getNrOfQuestions();
        long concatenation = otherAnswersLong | (answersLong << nrOfOtherQuestions);

        return new Answers(concatenation, nrOfQuestions + nrOfOtherQuestions);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = bitSet.length(); i < nrOfQuestions; i++) {
            stringBuilder.append('0');
        }
        if(bitSet.length() > 0) {
            long answersLong = bitSet.toLongArray()[0];
            stringBuilder.append(Long.toBinaryString(answersLong));
        }
        return stringBuilder.toString();
    }
}
