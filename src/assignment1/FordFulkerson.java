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

        Edge[] parentEdges = DFS.run(residualGraph, source, sink);
        while(parentEdges[sink] != null) {

            List<Edge> path = getPath(parentEdges, sink);
            for(Edge edge : path) {
                augmentFlow(flowGraph, residualGraph, edge);
            }

            parentEdges = DFS.run(residualGraph, source, sink);
        }

        return flowGraph;
    }

    public static int getFlowValue(Graph flowGraph, int source) {
        Edge[] adjacencyList = flowGraph.getAdjacencyList(source);

        int flowValue = 0;
        for(Edge edge : adjacencyList) {
            if(edge != null) {
                flowValue += edge.getFlow();
            }
        }

        return flowValue;
    }

    private static List<Edge> getPath(Edge[] parentEdges, int lastVertex) {
        if(parentEdges[lastVertex] == null) {
            List<Edge> path = new LinkedList<>();
            return path;
        }

        Edge parentEdge = parentEdges[lastVertex];

        int parent = parentEdge.getFrom();
        List<Edge> path = getPath(parentEdges, parent);

        path.add(parentEdge);
        return path;
    }

    private static void augmentFlow(Graph flowGraph, Graph residualGraph,
            Edge edge) {
        int from = edge.getFrom();
        int to = edge.getTo();

        if(flowGraph.hasEdge(from, to)) {
            flowGraph.setFlow(from, to, 1);
        } else {
            flowGraph.setFlow(to, from, 0);
        }

        residualGraph.addEdge(to, from);
        residualGraph.removeEdge(from, to);
    }
}
