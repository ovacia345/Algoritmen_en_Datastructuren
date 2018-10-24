package graphs;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class Graph<T extends Number> {
    private final int nrVertices;
    private final int nrEdgeVariables;
    private final List<Edge<T>>[] adjacencyLists;

    public Graph(int nrVertices, int nrEdgeVariables) {
        if(nrVertices < 0) {
            throw new IllegalArgumentException("You cannot have a negative "
                    + "number of vertices.");
        }
        if(nrEdgeVariables < 0) {
            throw new IllegalArgumentException("You cannot have negative "
                    + "number of edge variables");
        }
        
        this.nrVertices = nrVertices;
        this.nrEdgeVariables = nrEdgeVariables;
        
        adjacencyLists = new List[nrVertices];
        for(int i = 0; i < nrVertices; i++) {
            adjacencyLists[i] = new LinkedList<>();
        }
    }

    public Graph(int nrVertices) {
        this(nrVertices, 0);
    }

    public void addEdge(int source, int destination, T... edgeVariables) {
        Edge<T> edge = new Edge(source, destination, edgeVariables);
        adjacencyLists[source].add(edge);
    }

    public void addEdge(int source, int destination) {
        addEdge(source, destination, null);
    }    
    
    public List<Edge<T>> getAdjacencyList(int vertex) {
        checkInvalidVertex(vertex);
        
        return adjacencyLists[vertex];
    }
    
    public T[] getEdgeVariables(int source, int destination) {
        List<Edge<T>> adjacencyList = getAdjacencyList(source);
        
        for(Edge<T> edge : adjacencyList) {
            if(edge.getDestination() == destination) {
                return edge.getEdgeVariables();
            }
        }
        
        throw new IllegalArgumentException(String.format("Edge (%d,%d) "
                + "does not exist in the graph.", source, destination));
    }
    
    public T getEdgeVariable(int source, int destination, int edgeVariableNr) {
        checkInvalidEdgeVariableNr(edgeVariableNr);
        
        T edgeVariable = getEdgeVariables(source, destination)[edgeVariableNr];
        return edgeVariable;
    }
    
    public void setEdgeVariables(int source, int destination,
            T... edgeVariables) {
        List<Edge<T>> adjacencyList = getAdjacencyList(source);
        
        for(Edge<T> edge : adjacencyList) {
            if(edge.getDestination() == destination) {
                edge.setEdgeVariables(edgeVariables);
            }
        }
        
        throw new IllegalArgumentException(String.format("Edge (%d,%d) "
                + "does not exist in the graph.", source, destination));
    }
    
    public boolean hasEdge(int source, int destination) {
        List<Edge<T>> adjacencyList = getAdjacencyList(source);
        
        for(Edge<T> edge : adjacencyList) {
            if(edge.getDestination() == destination) {
                return true;
            }
        }
        
        return false;
    }
    
    public int getNrVertices() {
        return nrVertices;
    }
    
    public int getNrEdgeVariables() {
        return nrEdgeVariables;
    }

    public void checkInvalidVertex(int vertex) {
        if(vertex < 0 || vertex >= nrVertices) {
            throw new IllegalArgumentException(String.format("Vertex %d does "
                    + "not exist in the graph.", vertex));
        }
    }

    public void checkHasNotEdge(int source, int destination) {
        if(hasEdge(source, destination)) {
            throw new IllegalArgumentException(String.format("Edge (%d,%d) "
                    + "already exists in the graph.", source, destination));
        }
    }

    public void checkInvalidEdgeVariableNr(int edgeVariableNr) {
        if(edgeVariableNr < 0 || edgeVariableNr >= nrEdgeVariables) {
            throw new IllegalArgumentException(String.format("Edge variable "
                    + "%d does not exist in the graph.", edgeVariableNr));
        }
    }
}
