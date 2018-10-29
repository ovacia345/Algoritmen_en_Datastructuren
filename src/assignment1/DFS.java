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
     * @param vertexU the vertex to be matched
     * @param graph the input graph
     * @param pair the array where {@code pair[u]} is the vertex that forms a
     * match with {@code u}
     * @param dist the distance array
     * @return whether or not {@code vertexU} can be matched with another vertex
     */
    public static boolean run(int vertexU, Graph graph, int[] pair, int[] dist) {
        if(vertexU != 0) {
            for(int vertexV : graph.getAdjacencyList(vertexU)) {
                if(dist[pair[vertexV]] == dist[vertexU] + 1 &&
                        run(pair[vertexV], graph, pair, dist)) {
                    pair[vertexV] = vertexU;
                    pair[vertexU] = vertexV;
                    return true;
                }
            }

            dist[vertexU] = Integer.MAX_VALUE;
            return false;
        }

        return true;
    }
}
