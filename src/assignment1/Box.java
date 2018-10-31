package assignment1;

import java.util.Arrays;

/**
 *
 * @author N.C.M. van Nistelrooij 
 * @author C Amghane
 */
public class Box implements Comparable<Box> {
    private final double[] sideLengths;

    /**
     * Initialize box where the {@code sideLenghts} array is sorted.
     * @param xLength the first side length
     * @param yLength the second side length
     * @param zLength the third side length
     */
    public Box(double xLength, double yLength, double zLength) {
        sideLengths = new double[]{xLength, yLength, zLength};
        Arrays.sort(sideLengths);
    }

    /**
     * Get the side lengths of this box.
     * @return array {@code sideLenghts}; the side lengths of the box
     */
    public double[] getSideLengths() {
        return sideLengths;
    }

    /**
     * Get the volume of this box
     * @return volume of the box
     */
    public double getVolume() {
        return sideLengths[0] * sideLengths[1] * sideLengths[2];
    }

    /**
     * Checks whether this box fits inside the argument box.
     * @param otherBox the argument box
     * @return whether or not this box fits into the argument box
     */
    public boolean fitsIn(Box otherBox) {
        double[] otherBoxSideLenghts = otherBox.getSideLengths();
        for(int i = 0; i < sideLengths.length; i++) {
            if(sideLengths[i] >= otherBoxSideLenghts[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Compare the volume of this box to the volume of the argument box.
     * @param otherBox the argument box
     * @return an integer representation of the comparison between this box's
     * volume and the argument box's volume
     */
    @Override
    public int compareTo(Box otherBox) {
        return Double.compare(getVolume(), otherBox.getVolume());
    }
}
