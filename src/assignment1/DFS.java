package assignment1;

import java.util.List;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class DFS {
    public static Edge[] run(Graph graph, int source, int sink) {
        int nrOfVertices = graph.getNrOfVertices();
        boolean[] discovered = new boolean[nrOfVertices];
        int[][] parentEdges = new int[nrOfVertices][4];
        
        Edge[] npedges = new Edge[nrOfVertices];
        
        DFSVisit(graph, source, discovered, npedges, sink);

        return npedges;
    }

    private static void DFSVisit(Graph graph, int vertexU, boolean[] discovered,
                                Edge[] npedges, int sink) {
        discovered[vertexU] = true;

//        List<int[]> adjacencyListVertexU = graph.getAdjacencyList(vertexU);
//        for(int[] edgeVariables : adjacencyListVertexU) {
//            int vertexV = edgeVariables[1];
//            if(discovered[vertexV] == false) {
//                parentEdges[vertexV] = edgeVariables;
//                DFSVisit(graph, vertexV, discovered, parentEdges, sink);
//
//                if(discovered[sink] == true) {
//                    return;
//                }
//            }
//        }
        
        List<Edge> edges = graph.getAdjLists().get(vertexU).getNeighbours();
        for(Edge e: edges){
            int vertexV = e.getTo().getNumber();
            if(discovered[vertexV] == false ){
                //parentEdges[vertexV] = e;
                npedges[0] = e;
                DFSVisit(graph, vertexV, discovered,npedges, sink);
                if(discovered[sink] == true) {
                    return;
                }
            } 
        }
        
        
        
    }
}
