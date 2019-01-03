package assignment2;

import java.util.BitSet;

public class Answers {
    private final int lastChangedAnswerIndex;
    private final int nrOfQuestions;
    private final BitSet bitSet;

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
        BitSet answers = bitSet.get(nrOfQuestions - toIndex,
                nrOfQuestions - fromIndex);
        return new Answers(answers, toIndex - fromIndex);
    }

    public Answers getComplement() {
        BitSet complement = (BitSet)bitSet.clone();
        complement.flip(0, nrOfQuestions);
        return new Answers(complement, nrOfQuestions);
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

    public int getNrOfDifferentAnswers(Answers otherAnswers) {
        BitSet differences = (BitSet)bitSet.clone();
        differences.xor(otherAnswers.getBitSet());
        return differences.cardinality();
    }

    public int getNrOfEqualAnswers(Answers otherAnswers) {
        return nrOfQuestions - getNrOfDifferentAnswers(otherAnswers);
    }
    
    public Answers concatenate(Answers otherAnswers) {
        long answersLong = bitSet.length() > 0 ? bitSet.toLongArray()[0] : 0;
        long otherAnswersLong = otherAnswers.getBitSet().length() > 0
                ? otherAnswers.getBitSet().toLongArray()[0] : 0;

        int nrOfOtherQuestions = otherAnswers.getNrOfQuestions();
        long concatenation = otherAnswersLong |
                (answersLong << nrOfOtherQuestions);

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
