package assignment1;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author N.C.M. van Nistelrooij
 */
public class BFS {
    public static Edge[] run(Graph graph, int source, int sink) {
        int nrOfVertices = graph.getNrOfVertices();
        boolean[] discovered = new boolean[nrOfVertices];
        Edge[] parentEdges = new Edge[nrOfVertices];

        discovered[source] = true;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);

        while(!queue.isEmpty()) {
            int vertexU = queue.remove();
            Edge[] adjacencyListVertexU = graph.getAdjacencyList(vertexU);

            for(Edge edge : adjacencyListVertexU) {
                int vertexV = edge.getTo();
                if(discovered[vertexV] == false) {
                    discovered[vertexV] = true;
                    parentEdges[vertexV] = edge;
                    queue.add(vertexV);

                    if(discovered[sink] == true) {
                        return parentEdges;
                    }
                }               
            }           
        }
        
        return parentEdges;
    }
}
