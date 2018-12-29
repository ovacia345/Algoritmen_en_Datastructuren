package assignment2;

import java.util.BitSet;

/**
 * Class that represents binary answers.
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class Answers {
    private final int lastChangedAnswerIndex;
    private final int nrOfQuestions;
    private final BitSet bitSet;

    /**
     * {@code Answers} is initialized.
     * @param bitSet The {@code BitSet} representing the binary answers.
     * @param lastChangedAnswerIndex The index of the answer that was flipped
     * most recently.
     * @param nrOfQuestions The number of questions.
     */
    public Answers(BitSet bitSet, int lastChangedAnswerIndex, int nrOfQuestions) {
        this.bitSet = bitSet;
        this.lastChangedAnswerIndex = lastChangedAnswerIndex;
        this.nrOfQuestions = nrOfQuestions;
    }

    /**
     * If no {@code lastChangedAnswerIndex} is given, it is set to -1.
     * @param bitSet The {@code BitSet} representing the binary answers.
     * @param nrOfQuestions The number of questions.
     */
    public Answers(BitSet bitSet, int nrOfQuestions) {
        this(bitSet, -1, nrOfQuestions);
    }

    /**
     * If a {@code long} is given, that {@code long} is transformed to a
     * {@code BitSet} and the above constructor is called.
     * @param answers The {@code long} representing the binary answers.
     * @param nrOfQuestions The number of questions.
     */
    public Answers(long answers, int nrOfQuestions) {
        this(BitSet.valueOf(new long[]{answers}), nrOfQuestions);
    }

    /**
     * Gets a subset of the answers.
     * @param fromIndex The inclusive start index.
     * @param toIndex The exclusive end index.
     * @return Subset of the answers.
     */
    public Answers get(int fromIndex, int toIndex) {
        BitSet answers = bitSet.get(nrOfQuestions - toIndex,
                nrOfQuestions - fromIndex);
        return new Answers(answers, toIndex - fromIndex);
    }

    /**
     * Flips all the answers.
     * @return Complement of the answers.
     */
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

    /**
     * Flips a single answer at index {@code index}.
     * If, for example, an answer at index 12 was 1 it will change to 0.
     * @param index The index of the answer.
     * @return Answers with one flipped answer.
     */
    public Answers changeAnswer(int index) {
        BitSet changedAnswers = (BitSet)bitSet.clone();
        changedAnswers.flip(nrOfQuestions - 1 - index);
        return new Answers(changedAnswers, index, nrOfQuestions);
    }

    /**
     * Gets the number of different answers between {@code this} and
     * {@code otherAnswers}.
     * @param otherAnswers The other answers.
     * @return The number of different answers.
     */
    public int getNrOfDifferentAnswers(Answers otherAnswers) {
        BitSet differences = (BitSet)bitSet.clone();
        differences.xor(otherAnswers.getBitSet());
        return differences.cardinality();
    }

    /**
     * Gets the number of equal answers between {@code this} and
     * {@code otherAnswers}.
     * @param otherAnswers The other answers.
     * @return The number of equal answers.
     */
    public int getNrOfEqualAnswers(Answers otherAnswers) {
        return nrOfQuestions - getNrOfDifferentAnswers(otherAnswers);
    }
    
    /**
     * Concatenates {@code this} and {@code otherAnswers} such that the answers
     * of {@code this} are followed by the answers of {@code otherAnswers}.
     * @param otherAnswers The other answers.
     * @return The concatenation of {@code this} and {@code otherAnswers}.
     */
    public Answers concatenate(Answers otherAnswers) {
        long answersLong = bitSet.length() > 0 ? bitSet.toLongArray()[0] : 0;
        long otherAnswersLong = otherAnswers.getBitSet().length() > 0
                ? otherAnswers.getBitSet().toLongArray()[0] : 0;

        int nrOfOtherQuestions = otherAnswers.getNrOfQuestions();
        long concatenation = otherAnswersLong |
                (answersLong << nrOfOtherQuestions);

        return new Answers(concatenation, nrOfQuestions + nrOfOtherQuestions);
    }

    /**
     * Returns a {@code String} representation of the answers as a string of
     * exactly {@code nrOfQuestions} ones and zeroes.
     * @return {@code String} representation of the answers.
     */
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
