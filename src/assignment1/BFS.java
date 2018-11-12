package assignment1;

import static assignment1.FordFulkerson.NIL;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class BFS {
    /**
     * Checks whether or not there are still more matches possible.
     * It also updates the distances array based on the matches made in
     * {@code DFS.run()}.
     * @param graph the input graph
     * @param matches the array where {@code matches[u]} is the vertex that
     * forms a match with {@code u}
     * @param distances the distances array
     * @param nrOfBoxes the number of boxes
     * @return whether or not there are still more matches possible.
     */
    public static boolean run(Graph graph, int[] matches, int[] distances,
            int nrOfBoxes) {
        Queue<Integer> queue = new LinkedList<>();

        for(int smallBox = 1; smallBox <= nrOfBoxes; smallBox++) {
            if(matches[smallBox] == NIL) {
                distances[smallBox] = 0;
                queue.add(smallBox);
            } else {
                distances[smallBox] = Integer.MAX_VALUE;
            }
        }
        distances[NIL] = Integer.MAX_VALUE;

        while(!queue.isEmpty()) {
            int smallBox = queue.remove();
            if(distances[smallBox] < distances[NIL]) {
                for(int bigBox : graph.getAdjacencyList(smallBox)) {
                    if(distances[matches[bigBox]] == Integer.MAX_VALUE) {
                        distances[matches[bigBox]] = distances[smallBox] + 1;
                        queue.add(matches[bigBox]);
                    }
                }
            }
        }

        return distances[NIL] != Integer.MAX_VALUE;
    }
}
