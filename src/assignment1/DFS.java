package assignment1;

import static assignment1.FordFulkerson.NIL;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class DFS {
    /**
     * Checks whether or not there is a match for {@code vertexU} with another
     * vertex.
     * @param smallBox the vertex to be matched
     * @param graph the input graph
     * @param matches the array where {@code pair[u]} is the vertex that forms a
     * match with {@code u}
     * @param distances the distance array
     * @return whether or not {@code vertexU} can be matched with another vertex
     */
    public static boolean run(int smallBox, Graph graph, int[] matches,
            int[] distances) {
        if(smallBox != NIL) {
            for(int bigBox : graph.getAdjacencyList(smallBox)) {
                if(distances[matches[bigBox]] == distances[smallBox] + 1 &&
                        run(matches[bigBox], graph, matches, distances)) {
                    matches[bigBox] = smallBox;
                    matches[smallBox] = bigBox;
                    return true;
                }
            }

            distances[smallBox] = Integer.MAX_VALUE;
            return false;
        }

        return true;
    }
}
