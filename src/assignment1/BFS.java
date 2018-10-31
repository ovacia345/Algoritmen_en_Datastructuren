package assignment1;

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
     * @param graph the input graph
     * @param pair the array where {@code pair[u]} is the vertex that forms a
     * match with {@code u}
     * @param dist the distance array
     * @param nrOfBoxes the number of boxes
     * @return whether or not there are still more matches possible.
     */
    public static boolean run(Graph graph, int[] pair, int[] dist, int nrOfBoxes) {
        Queue<Integer> queue = new LinkedList<>();

        for(int smallBox = 1; smallBox <= nrOfBoxes; smallBox++) {
            if(pair[smallBox] == 0) {
                dist[smallBox] = 0;
                queue.add(smallBox);
            } else {
                dist[smallBox] = Integer.MAX_VALUE;
            }
        }
        dist[0] = Integer.MAX_VALUE;

        while(!queue.isEmpty()) {
            int smallBox = queue.remove();
            if(dist[smallBox] < dist[0]) {
                for(int bigBox : graph.getAdjacencyList(smallBox)) {
                    if(dist[pair[bigBox]] == Integer.MAX_VALUE) {
                        dist[pair[bigBox]] = dist[smallBox] + 1;
                        queue.add(pair[bigBox]);
                    }
                }
            }
        }

        return dist[0] != Integer.MAX_VALUE;
    }
}
