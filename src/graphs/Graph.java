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

    private int nrEdges;

    public Graph(int nrVertices, int nrEdgeVariables) {
        if(nrVertices < 0) {
            throw new IllegalArgumentException("You cannot have a negative "
                    + "number of vertices.");
        }
        if(nrEdgeVariables < 0) {
            throw new IllegalArgumentException("You cannot have a negative "
                    + "number of edge variables.");
        }
        
        this.nrVertices = nrVertices;
        nrEdges = 0;
        this.nrEdgeVariables = nrEdgeVariables;
        
        adjacencyLists = new List[nrVertices];
        for(int vertex = 0; vertex < nrVertices; vertex++) {
            adjacencyLists[vertex] = new LinkedList<>();
        }
    }

    public Graph(int nrVertices) {
        this(nrVertices, 0);
    }

    public void addEdge(int source, int destination, T... edgeVariables) {
        checkInvalidVertex(source);
        checkInvalidVertex(destination);
        checkInvalidNrEdgeVariables(edgeVariables.length);
        checkHasNotEdge(source, destination);

        Edge<T> edge = new Edge(source, destination, edgeVariables);
        adjacencyLists[source].add(edge);

        nrEdges++;
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
        List<Edge<T>> adjacencyList = getAdjacencyList(source);

        for(Edge<T> edge : adjacencyList) {
            if(edge.getDestination() == destination) {
                return edge.getEdgeVariable(edgeVariableNr);
            }
        }

        throw new IllegalArgumentException(String.format("Edge (%d,%d) "
                + "does not exist in the graph.", source, destination));
    }
    
    public void setEdgeVariables(int source, int destination,
            T... edgeVariables) {
        checkInvalidNrEdgeVariables(edgeVariables.length);
        
        List<Edge<T>> adjacencyList = getAdjacencyList(source);
        
        for(Edge<T> edge : adjacencyList) {
            if(edge.getDestination() == destination) {
                edge.setEdgeVariables(edgeVariables);
                return;
            }
        }
        
        throw new IllegalArgumentException(String.format("Edge (%d,%d) "
                + "does not exist in the graph.", source, destination));
    }

    public void setEdgeVariable(int source, int destination, T edgeVariable,
            int edgeVariableNr) {
        List<Edge<T>> adjacencyList = getAdjacencyList(source);

        for(Edge<T> edge : adjacencyList) {
            if(edge.getDestination() == destination) {
                edge.setEdgeVariable(edgeVariable, edgeVariableNr);
                return;
            }
        }

        throw new IllegalArgumentException(String.format("Edge (%d,%d) "
                + "does not exist in the graph.", source, destination));
    }

    public void removeEdge(int source, int destination) {
        List<Edge<T>> adjacencyList = getAdjacencyList(source);

        for(Edge<T> edge : adjacencyList) {
            if(edge.getDestination() == destination) {
                adjacencyList.remove(edge);
                return;
            }

            nrEdges--;
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

    private void checkInvalidVertex(int vertex) {
        if(vertex < 0 || vertex >= nrVertices) {
            throw new IllegalArgumentException(String.format("Vertex %d does "
                    + "not exist in the graph.", vertex));
        }
    }

    private void checkHasNotEdge(int source, int destination) {
        if(hasEdge(source, destination)) {
            throw new IllegalArgumentException(String.format("Edge (%d,%d) "
                    + "already exists in the graph.", source, destination));
        }
    }

    private void checkInvalidNrEdgeVariables(int nrEdgeVariables) {
        if(this.nrEdgeVariables != nrEdgeVariables) {
            throw new IllegalArgumentException(String.format("The edges of the "
                    + "graph have %d edge variable(s), but %d edge variable(s) "
                    + "were given.", this.nrEdgeVariables, nrEdgeVariables));
        }
    }
    
    public void printGraph() {
        List<Edge<T>> edges ;
        for(int vertex = 0; vertex< nrVertices; vertex++){
            edges = getAdjacencyList(vertex);
            for(int j = 0; j<edges.size(); j++) {
                edges.get(j).printEdge();
            }
        }
    }
}
