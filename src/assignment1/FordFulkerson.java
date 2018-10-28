package assignment1;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class FordFulkerson {
    
    
    public static Graph run(Graph graph, Vertex source, Vertex sink) {
        Graph flowGraph = graph;
        //Graph residualGraph = new Graph(graph); // kost tijd

        //int[][] parentEdges = DFS.run(residualGraph, source, sink);
        List<Edge> pEdges = DFS.run(flowGraph,source.getNumber(),sink.getNumber());
        
        List<Edge> pp = getPath(pEdges, pEdges.get(1).getFrom());
        
        for(Edge e: pEdges){
            //edge's flow should only be updated when residual path is possible...
            
            //augmentFlow(flowGraph,e);
            e.setFlow(1);
        }

        return flowGraph;
    }

    public static int getFlowValue(Graph flowGraph, int source) {
        List<Edge> edges = flowGraph.getSink().getNeighbours();//flowGraph.getAdjLists().get(source).getNeighbours();

        int flowValue = 0;

        for(Edge e:edges){
            flowValue+= e.getFlow();
        }
        System.out.println("flowvalue + " + flowValue);
        return flowValue;
    }

    
    
    // this could be an arrray of edges
    private static List<Edge> getPath(List<Edge> parentEdges, Vertex v) {

        if(v.getNumber() == 0){
            List<Edge> path = new LinkedList<>();
            return path;
        }
        
 
        Edge pE = parentEdges.get(v.getNumber()-1);

        Vertex p = pE.getFrom();

        parentEdges.remove(pE);
        List<Edge> path = getPath(parentEdges, p);

        path.add(pE);
        return path;
    }

    private static void augmentFlow(Graph flowGraph,Graph residualGraph , Edge e) {

        Vertex vU = e.getFrom();
        Vertex vV = e.getTo();
        
        Edge eInfo = flowGraph.getEdge(vU, vV);
        if(eInfo != null) {
            System.out.println("increainsg flow ");
            eInfo.setFlow(1);
        } else {
            System.out.println("not increasing flow");
            flowGraph.addEdge(vV,vU);
        }

        residualGraph.removeEdge(e);

    }
}
