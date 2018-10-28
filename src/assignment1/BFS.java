package assignment1;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author N.C.M. van Nistelrooij
 */
public class BFS {
//    public static int[][] run(Graph graph, int source, int sink) {
//        int nrOfVertices = graph.getNrOfVertices();
//        boolean[] discovered = new boolean[nrOfVertices];
//        int[][] parentEdges = new int[nrOfVertices][4];
//
//        discovered[source] = true;
//        Queue<Integer> queue = new LinkedList<>();
//        queue.add(source);
//
//        while(!queue.isEmpty()) {
//            int vertexU = queue.remove();
//            List<int[]> adjacencyListVertexU = graph.getAdjacencyList(vertexU);
//
//            for(int[] edgeVariables : adjacencyListVertexU) {
//                int vertexV = edgeVariables[1];
//                if(discovered[vertexV] == false) {
//                    discovered[vertexV] = true;
//                    parentEdges[vertexV] = edgeVariables;
//                    queue.add(vertexV);
//
//                    if(discovered[sink] == true) {
//                        return parentEdges;
//                    }
//                }               
//            }           
//        }
//        
//        return parentEdges;
//    }
}
