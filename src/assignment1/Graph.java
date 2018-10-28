package assignment1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class Graph {
    private final int nrOfVertices;
    private final List<Vertex> adjacencyLists;

    public Graph(int nrOfVertices) {
        this.nrOfVertices = nrOfVertices;
        adjacencyLists = new ArrayList<Vertex>();
//        adjacencyLists = new List[nrOfVertices];
        for(int vertex = 0; vertex < nrOfVertices; vertex++) {
            adjacencyLists.add(new Vertex(vertex));
//            adjacencyLists[vertex] = new Vertex();
        }
        
        System.out.println("graph met " + nrOfVertices + " vertices");
    }
    
    
    public Graph(int nrOfVertices, List<Vertex> adjList) {
        this.nrOfVertices = nrOfVertices;
        this.adjacencyLists = adjList;
    }
    
    
    public Graph(Graph graph) {
        this(graph.getNrOfVertices(), graph.getAdjLists());

//        for(List<int[]> adjacencyList : graph.adjacencyLists) {
//            for(int[] edgeVariables : adjacencyList) { //should be vertices instead of edgevars?
//                System.out.println("edgeVar0  " +edgeVariables[0] );
//                System.out.println("edgeVars legnte " +edgeVariables.length);
//
//                int vertexU = edgeVariables[0];
//                int vertexV = edgeVariables[1];
//                addEdge(vertexU, vertexV);
//            }
//        }
    }
    
//    public void addEdge(int source, int destination) {
//        adjacencyLists[source].add(new int[]{source, destination, 0, 1});
//    }
    public Vertex getSource() {
        return this.getVertex(0);
    }
    public Vertex getSink() {
        return this.getVertex(this.nrOfVertices-1);
    }
    
    public Vertex getVertex(int number) {
        return this.adjacencyLists.get(number);
        
    }
    
    public void addEdge(Vertex from, Vertex to) {
        from.addNeighbour(new Edge(from, to));
        //adjacencyLists.
    }
    public List<Vertex> getAdjLists() {
        return adjacencyLists;
    }
    
//    public List<int[]> getAdjacencyList(int vertex) {
//        return adjacencyLists[vertex];
//    }

    public int getEdgeFlow(Edge e) {
        return e.getFlow();
        
    }
    
    public int getEdgeCap(Edge e) {
        return e.getCapacity();
    }
    //loop everytime, this could be done more efficiently
//    public int[] getEdgeVariables(int source, int destination) {
//        List<int[]> adjacencyList = getAdjacencyList(source);
//
//        for(int[] edgeVariables : adjacencyList) {
//            if(edgeVariables[1] == destination) {
//                return edgeVariables;
//            }
//        }
//
//        return null;
//    }
        
    //loop everytime, this could be done more efficiently
//    public void setFlow(int source, int destination, int flow) {
//        List<int[]> adjacencyList = getAdjacencyList(source);
//
//        for(int[] edgeVariables : adjacencyList) {
//            if(edgeVariables[1] == destination) {
//                edgeVariables[2] = flow;
//            }
//        }
//    }
    
    public void setFlow(Edge e, int flow ){
        e.setFlow(flow);
    }
    
    public void removeEdge(Edge e){
        e.getFrom().removeNeighbour(e.getFrom(), e.getTo());
    }
    
//    public void removeEdge(int source, int[] edgeVariables) {
//        List<int[]> adjacencyList = getAdjacencyList(source);
//        adjacencyList.remove(edgeVariables);
//    }
    
    
    public Edge getEdge(Vertex from, Vertex to){
        LinkedList<Edge> nb =from.getNeighbours();
        for(Edge e: nb){
            if(e.getTo().equals(to)){
                return e;
            }
        }
        return null;
    }
    
    public boolean hasEdge(Vertex from, Vertex to){
        LinkedList<Edge> nb =from.getNeighbours();
        for(Edge e: nb){
            if(e.getTo().equals(to)){
                return true;
            }
        }
        return false;
    }
              
//    public boolean hasEdge(int source, int destination) {
//        List<int[]> adjacencyList = getAdjacencyList(source);
//
//        for(int[] edgeVariables : adjacencyList) {
//            if(edgeVariables[1] == destination) {
//                return true;
//            }
//        }
//
//        return false;
//    }

    public int getNrOfVertices() {
        return nrOfVertices;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for(int vertex = 0; vertex < nrOfVertices; vertex++) {
            LinkedList<Edge> edges = adjacencyLists.get(vertex).getNeighbours();
            for(Edge e : edges) {
                System.out.println(e.getFrom() + " : " + e.getTo());
                stringBuilder.append(String.format("%d - %s:\t%d %d\t", vertex,
                         e.getTo(), e.getFlow(), e.getCapacity()));
            }
        }

        return stringBuilder.toString();
    }
}
