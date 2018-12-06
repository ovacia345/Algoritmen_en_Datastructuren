package assignment2;

import java.util.Arrays;

/**
 *
 * @author N.C.M. van Nistelrooij
 */
public class BinaryString {
    private final int[] string;
    private final int code;
    private final int nrOfDifferences;

    public BinaryString(int[] string, int code, int nrOfDifferences) {
        this.string = string;
        this.code = code;
        this.nrOfDifferences = nrOfDifferences;
    }

    public BinaryString changeValue(int index) {
        int[] newString = Arrays.copyOf(string, length());
        newString[index] = 1 - newString[index];

        return new BinaryString(newString, index, nrOfDifferences + 1);
    }

    public BinaryString opposite() {
        int[] newString = new int[length()];
        for(int i = 0; i < length(); i++) {
            newString[i] = 1 - string[i];
        }

        return new BinaryString(newString, code, nrOfDifferences);
    }

    public BinaryString copyOf(int cut) {
        int[] newString = Arrays.copyOf(string, cut);

        return new BinaryString(newString, code, nrOfDifferences);
    }

    public int[] getString() {
        return string;
    }

    public int getCode() {
        return code;
    }

    public int getNrOfDifferences() {
        return nrOfDifferences;
    }

    public int getNrOfDifferencesBetween(BinaryString otherBinaryString) {
        int[] otherString = otherBinaryString.getString();
        int nrOfDifferences = 0;
        for(int i = 0; i < length(); i++) {
            nrOfDifferences += (string[i] + otherString[i]) % 2;
        }

        return nrOfDifferences;
    }

    public int length() {
        return string.length;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int value : string) {
            stringBuilder.append(value);
        }

        return stringBuilder.toString();
    }
}
