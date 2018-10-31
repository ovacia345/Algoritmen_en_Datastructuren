package assignment1;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class FordFulkerson {
    /**
     * Computes the maximum number of bipartite matches in the input graph.
     * @param graph the input graph, must be a bipartite graph + source + sink
     * @param nrOfBoxes the number of boxes
     * @return the maximum number of bipartite matches in the input graph, i.e.
     * the number of boxes that are put into other boxes
     */
    public static int hopcroftKarp(Graph graph, int nrOfBoxes) {
        int[] pair = new int[nrOfBoxes * 2 + 1];
        int[] dist = new int[nrOfBoxes * 2 + 1];
        int nrOfMatches = 0;

        while(BFS.run(graph, pair, dist, nrOfBoxes)) {
            for(int vertexU = 1; vertexU <= nrOfBoxes; vertexU++) {
                if(pair[vertexU] == 0 && DFS.run(vertexU, graph, pair, dist)) {
                    nrOfMatches++;
                }
            }
        }

        return nrOfMatches;
    }
}
