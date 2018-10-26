package assignment1;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class FordFulkerson {
    public static Graph run(Graph graph, int source, int sink) {
        Graph flowGraph = graph;
        Graph residualGraph = new Graph(graph);

        int[][] parentEdges = DFS.run(residualGraph, source, sink);
        while(parentEdges[sink][1] == sink) {

            List<int[]> path = getPath(parentEdges, sink);
            for(int[] edgeVariables : path) {
                int vertexU = edgeVariables[0];
                int vertexV = edgeVariables[1];
                augmentFlow(flowGraph, residualGraph, vertexU, vertexV);
            }

            parentEdges = DFS.run(residualGraph, source, sink);
        }

        return flowGraph;
    }

    public static int getFlowValue(Graph flowGraph, int source) {
        List<int[]> adjacencyList = flowGraph.getAdjacencyList(source);

        int flowValue = 0;
        for(int[] edgeVariables : adjacencyList) {
            flowValue += edgeVariables[2];
        }

        return flowValue;
    }

    private static List<int[]> getPath(int[][] parentEdges, int lastVertex) {
        if(parentEdges[lastVertex][1] == 0) {
            List<int[]> path = new LinkedList<>();
            return path;
        }

        int[] parentEdge = parentEdges[lastVertex];

        int parent = parentEdge[0];
        List<int[]> path = getPath(parentEdges, parent);

        path.add(parentEdge);
        return path;
    }

    private static void augmentFlow(Graph flowGraph, Graph residualGraph,
            int vertexU, int vertexV) {
        int[] edgeVariables = flowGraph.getEdgeVariables(vertexU, vertexV);
        if(edgeVariables != null) {
            edgeVariables[2] = 1;
        } else {
            flowGraph.setFlow(vertexV, vertexU, 0);
        }

        residualGraph.addEdge(vertexV, vertexU);
        residualGraph.removeEdge(vertexU, vertexV);
    }
}
