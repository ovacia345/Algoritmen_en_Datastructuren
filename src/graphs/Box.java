package graphs;

import java.util.Arrays;

/**
 *
 * @author N.C.M. van Nistelrooij 
 * @author C Amghane
 */
public class Box {
    private static final int NR_SIDE_LENGHTS = 3;

    private final double[] sideLengths;

    public Box(double xLength, double yLength, double zLength) {
        sideLengths = new double[]{xLength, yLength, zLength};
        Arrays.sort(sideLengths);
    }

    public double[] getSideLengths() {
        return sideLengths;
    }

    public boolean fitsIn(Box otherBox) {
        if(otherBox != null) {
            double[] otherBoxSideLenghts = otherBox.getSideLengths();
            for(int i = 0; i < NR_SIDE_LENGHTS; i++) {
                if(sideLengths[i] >= otherBoxSideLenghts[i]) {
                    return false;
                }
            }

            return true;
        }

        throw new IllegalArgumentException("Argument box has a null pointer");
    }
}
