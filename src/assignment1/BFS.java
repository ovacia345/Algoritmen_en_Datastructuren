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

        for(int box = 1; box <= nrOfBoxes; box++) {
            if(pair[box] == 0) {
                dist[box] = 0;
                queue.add(box);
            } else {
                dist[box] = Integer.MAX_VALUE;
            }
        }
        dist[0] = Integer.MAX_VALUE;

        while(!queue.isEmpty()) {
            int vertexU = queue.remove();
            if(dist[vertexU] < dist[0]) {
                for(int vertexV : graph.getAdjacencyList(vertexU)) {
                    if(dist[pair[vertexV]] == Integer.MAX_VALUE) {
                        dist[pair[vertexV]] = dist[vertexU] + 1;
                        queue.add(pair[vertexV]);
                    }
                }
            }
        }

        return dist[0] != Integer.MAX_VALUE;
    }
}
