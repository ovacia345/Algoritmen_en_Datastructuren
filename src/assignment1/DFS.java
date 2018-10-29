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
    /**
     * This function runs the DFS
     * @param graph a graph
     * @param source the source of the graph
     * @param sink the sink of the graph
     * @return The tree of the DFS graph
     */
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

    /**
     * This is where the actual DFS magic happens
     * @param graph - graph
     * @param vertexU - the vertex we are currently visiting
     * @param discoveredVertices - already discovered vertices
     * @param npedges - the path to the current vertex
     * @param sink - sink of the graph
     */
    private static void DFSVisit(Graph graph, Vertex vertexU, List<Vertex> discoveredVertices,
                                List<Edge> npedges, Vertex sink) {

        
        List<Edge> edges = vertexU.getNeighbours();
        for(Edge e: edges){
            Vertex vertexV = e.getTo();
            if(!discoveredVertices.contains(vertexV)){
                npedges.add(e);

                DFSVisit(graph, vertexV, discoveredVertices, npedges, sink);
            } 
        }
        
       discoveredVertices.add(vertexU); 
       
    }
    
    /**
     * This functions return the path from a vertex(which has an edge with source) and the sink
     * @param v - Vertex
     * @param path - path towards sink
     * @param visitedVerts - visited vertices
     * @param sink - sink of a graph
     */
    
    public static void singlerunDFS(Vertex v, List<Edge> path, List<Vertex> visitedVerts, Vertex sink){

        List<Edge> nbs = v.getNeighbours();
        if(v.equals(sink) || ( visitedVerts.contains(sink) && path.size() == 2 )) {
            return;
        }
        
        for(Edge e: nbs){
            if(!visitedVerts.contains(e.getTo()) && !visitedVerts.contains(e.getFrom())){ 
                //System.out.println("path size : " + path.size() + " dit is de v " + v);
                path.add(e);

                visitedVerts.add(e.getFrom());
                singlerunDFS(e.getTo(), path,visitedVerts,sink);
            }
        }
        
        
    }
}
