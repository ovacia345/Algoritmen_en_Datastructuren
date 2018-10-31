package assignment1;

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
     * @param pair the array where {@code pair[u]} is the vertex that forms a
     * match with {@code u}
     * @param dist the distance array
     * @return whether or not {@code vertexU} can be matched with another vertex
     */
    public static boolean run(int smallBox, Graph graph, int[] pair, int[] dist) {
        if(smallBox != 0) {
            for(int bigBox : graph.getAdjacencyList(smallBox)) {
                if(dist[pair[bigBox]] == dist[smallBox] + 1 &&
                        run(pair[bigBox], graph, pair, dist)) {
                    pair[bigBox] = smallBox;
                    pair[smallBox] = bigBox;
                    return true;
                }
            }

            dist[smallBox] = Integer.MAX_VALUE;
            return false;
        }

        return true;
    }
}
