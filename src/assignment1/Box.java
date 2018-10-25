package assignment1;

import java.util.Arrays;

/**
 *
 * @author N.C.M. van Nistelrooij 
 * @author C Amghane
 */
public class Box {
    private final double[] sideLengths;

    public Box(double xLength, double yLength, double zLength) {
        sideLengths = new double[]{xLength, yLength, zLength};
        Arrays.sort(sideLengths);

        if(sideLengths[0] <= 0.5d || sideLengths[sideLengths.length - 1] >= 1.0d) {
            throw new IllegalArgumentException("Boxes must have side lengths "
                    + "strictly between 0.5 and 1.0.");
        }
    }

    public double[] getSideLengths() {
        return sideLengths;
    }

    public boolean fitsIn(Box otherBox) {
        if(otherBox != null) {
            double[] otherBoxSideLenghts = otherBox.getSideLengths();
            for(int i = 0; i < sideLengths.length; i++) {
                if(sideLengths[i] >= otherBoxSideLenghts[i]) {
                    return false;
                }
            }

            return true;
        }

        throw new IllegalArgumentException("Argument box has a null pointer.");
    }
}
