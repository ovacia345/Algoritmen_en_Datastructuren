package assignment2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author N.C.M. van Nistelrooij
 */
public class BinaryStringMaxKDifferencesIterator implements Iterator<BinaryString> {
    private final int k;
    private final Queue<BinaryString> queue;
    private final int stringLength;


    public BinaryStringMaxKDifferencesIterator(BinaryString string, int k) {
        this.k = k;
        queue = new LinkedList<>();
        queue.add(string);
        stringLength = string.length();
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty() && queue.peek().getNrOfDifferences() <= k;
    }

    @Override
    public BinaryString next() {
        BinaryString currentString = queue.poll();

        for(int i = currentString.getCode() + 1; i < stringLength; i++) {
            BinaryString newString = currentString.changeValue(i);
            queue.offer(newString);
        }

        return currentString;
    }
}
