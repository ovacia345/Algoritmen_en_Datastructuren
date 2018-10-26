package assignment1;

import java.util.List;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class DFS {
    public static int[][] run(Graph graph, int source, int sink) {
        int nrOfVertices = graph.getNrOfVertices();
        boolean[] discovered = new boolean[nrOfVertices];
        int[][] parentEdges = new int[nrOfVertices][4];

        DFSVisit(graph, source, discovered, parentEdges, sink);

        return parentEdges;
    }

    private static void DFSVisit(Graph graph, int vertexU, boolean[] discovered,
            int[][] parentEdges, int sink) {
        discovered[vertexU] = true;

        List<int[]> adjacencyListVertexU = graph.getAdjacencyList(vertexU);
        for(int[] edgeVariables : adjacencyListVertexU) {
            int vertexV = edgeVariables[1];
            if(discovered[vertexV] == false) {
                parentEdges[vertexV] = edgeVariables;
                DFSVisit(graph, vertexV, discovered, parentEdges, sink);

                if(discovered[sink] == true) {
                    return;
                }
            }
        }
    }
}
