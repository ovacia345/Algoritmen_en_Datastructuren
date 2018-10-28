package assignment1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class DFS {
    public static List<Edge> run(Graph graph, int source, int sink) {
        
        int nrOfVertices = graph.getNrOfVertices();
        
        List<Vertex> discoveredVertices = new ArrayList<>();
        
        int[] disc = new int[nrOfVertices];
        
        List<Edge> npedges = new LinkedList<>();

        for(Vertex u : graph.getAdjLists()){           
            if(!discoveredVertices.contains(u)){
                DFSVisit(graph, u, discoveredVertices,npedges,graph.getSink());
            }
          
        }

        return npedges;
    }

    private static void DFSVisit(Graph graph, Vertex vertexU, List<Vertex> discoveredVertices,
                                List<Edge> npedges, Vertex sink) {

        
        List<Edge> edges = vertexU.getNeighbours();
        for(Edge e: edges){
            Vertex vertexV = e.getTo();
            if(!discoveredVertices.contains(vertexV)){
                //parentEdges[vertexV] = e;
                npedges.add(e);

                DFSVisit(graph, vertexV, discoveredVertices, npedges, sink);
//                if(discoveredVertices.contains(sink)) return;
            } 
        }
        
       discoveredVertices.add(vertexU); 
       
    }
    
    private static void singlerunDFS(Vertex v){
        
        //g.getAdjLists().get(1).getNeighbours()
        
        List<Edge> nbs = v.getNeighbours();
        
        for(Edge e: nbs){
            
        }
        
        
    }
}
