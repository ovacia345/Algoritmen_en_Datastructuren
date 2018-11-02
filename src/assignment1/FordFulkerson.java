package assignment1;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class FordFulkerson {
    public final static int NIL = 0;

    /**
     * Computes the maximum number of bipartite matches in the input graph.
     * @param graph the input graph, must be a bipartite graph + source + sink
     * @param nrOfBoxes the number of boxes
     * @return the maximum number of bipartite matches in the input graph, i.e.
     * the number of boxes that are put into other boxes
     */
    public static int hopcroftKarp(Graph graph, int nrOfBoxes) {
        int[] matches = new int[nrOfBoxes * 2 + 1];
        int[] distances = new int[nrOfBoxes * 2 + 1];
        int nrOfMatches = 0;

        while(BFS.run(graph, matches, distances, nrOfBoxes)) {
            for(int smallBox = 1; smallBox <= nrOfBoxes; smallBox++) {
                if(matches[smallBox] == NIL &&
                        DFS.run(smallBox, graph, matches, distances)) {
                    nrOfMatches++;
                }
            }
        }

        return nrOfMatches;
    }
}
