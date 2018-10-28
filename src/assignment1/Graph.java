package assignment1;

import java.util.ArrayList;
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
        for(int vertex = 0; vertex < nrOfVertices; vertex++) {
            adjacencyLists.add(new Vertex(vertex));
        }
        
        System.out.println("graph met " + nrOfVertices + " vertices");
    }
    
    
    public Graph(int nrOfVertices, List<Vertex> adjList) {
        this.nrOfVertices = nrOfVertices;
        this.adjacencyLists = adjList;
    }
    
    
    public Graph(Graph graph) {
        this(graph.getNrOfVertices(), graph.getAdjLists());

    }

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
    


    public int getEdgeFlow(Edge e) {
        return e.getFlow();
        
    }
    
    public int getEdgeCap(Edge e) {
        return e.getCapacity();
    }

    
    public void setFlow(Edge e, int flow ){
        e.setFlow(flow);
    }
    
    public void removeEdge(Edge e){
        e.getFrom().removeNeighbour(e.getFrom(), e.getTo());
    }

    
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
