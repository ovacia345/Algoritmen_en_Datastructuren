package assignment1;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class DFS {
    public static Edge[] run(Graph graph, int source, int sink) {
        int nrOfVertices = graph.getNrOfVertices();
        boolean[] discovered = new boolean[nrOfVertices];
        Edge[] parentEdges = new Edge[nrOfVertices];

        DFSVisit(graph, source, discovered, parentEdges, sink);

        return parentEdges;
    }

    private static void DFSVisit(Graph graph, int vertexU, boolean[] discovered,
            Edge[] parentEdges, int sink) {
        discovered[vertexU] = true;

        Edge[] adjacencyListVertexU = graph.getAdjacencyList(vertexU);
        for(Edge edge : adjacencyListVertexU) {
            if(edge != null) {
                int vertexV = edge.getTo();
                if(discovered[vertexV] == false) {
                    parentEdges[vertexV] = edge;
                    DFSVisit(graph, vertexV, discovered, parentEdges, sink);

                    if(discovered[sink] == true) {
                        return;
                    }
                }
            }
        }
    }
}
