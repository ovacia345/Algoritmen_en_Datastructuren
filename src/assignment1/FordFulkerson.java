package assignment1;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class FordFulkerson {
    
    
    public static Graph run(Graph graph, Vertex source, Vertex sink) {
        Graph flowGraph = graph;

        List<Edge> visitedEdges = new ArrayList<>();
        List<Vertex> visitedVerts = new ArrayList<>();        
        augmentFlow(graph,visitedEdges, visitedVerts);
        

        return flowGraph;
    }

    public static int getFlowValue(Graph flowGraph) {
        int flowValue = 0;
        Vertex sink = flowGraph.getSink();
        int nrOfBoxes = (flowGraph.getNrOfVertices() -2 ) /2 ;
        for(int i = flowGraph.getSink().getNumber() -1 ; i>nrOfBoxes; i--){
            Vertex v = flowGraph.getVertex(i);
           flowValue += flowGraph.getEdge(v, sink).getFlow();
        }
        
        
        return flowValue;
    }


    
    /**
     * This function augments the flow of edges. It starts from the vertices connected with source 
     * and moves towards sink.
     * @param g - the graph 
     * @param visitedEdges - already visited edges
     * @param visitedVerts - already visited vertices
     */
    
    public static void augmentFlow (Graph g, List<Edge> visitedEdges, List<Vertex> visitedVerts) {
        int nrOfBoxes = (g.getNrOfVertices() - 2 ) / 2 ;
        for(int i = 1 ; i<= nrOfBoxes; i++){
            Vertex v = g.getVertex(i);
            DFS.singlerunDFS(v, visitedEdges, visitedVerts, g.getSink());
            if(visitedEdges.size() == 2) augmenter(visitedEdges);
            visitedEdges.clear();
        }
    }
    public static void augmenter(List<Edge> edges) {
        
        for(Edge e : edges){
            e.setFlow(1);
        }
        
    }
    
  
}
