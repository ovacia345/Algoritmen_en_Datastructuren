package graphs;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class Graph<T extends Number> {
    private final int nrOfVertices;
    private final int nrOfEdgeVariables;
    private final List<Edge<T>>[] adjacencyLists;

    private int nrOfEdges;

    public Graph(int nrOfVertices, int nrOfEdgeVariables) {
        checkNegativeNrOfVertices(nrOfVertices);
        checkNegativeNrOfEdgeVariables(nrOfEdgeVariables);
        
        this.nrOfVertices = nrOfVertices;
        nrOfEdges = 0;
        this.nrOfEdgeVariables = nrOfEdgeVariables;
        
        adjacencyLists = new List[nrOfVertices];
        for(int vertex = 0; vertex < nrOfVertices; vertex++) {
            adjacencyLists[vertex] = new LinkedList<>();
        }
    }

    public Graph(int nrOfVertices) {
        this(nrOfVertices, 0);
    }

    public void addEdge(int source, int destination, T... edgeVariables) {
        checkInvalidVertex(source);
        checkInvalidVertex(destination);
        checkInvalidNrOfEdgeVariables(edgeVariables.length);
        checkAlreadyHasEdge(source, destination);

        Edge<T> edge = new Edge<>(source, destination, edgeVariables);
        adjacencyLists[source].add(edge);

        nrOfEdges++;
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
    
    public T getEdgeVariable(int source, int destination, int edgeVariableIndex) {
        List<Edge<T>> adjacencyList = getAdjacencyList(source);

        for(Edge<T> edge : adjacencyList) {
            if(edge.getDestination() == destination) {
                return edge.getEdgeVariable(edgeVariableIndex);
            }
        }

        throw new IllegalArgumentException(String.format("Edge (%d,%d) "
                + "does not exist in the graph.", source, destination));
    }
    
    public void setEdgeVariables(int source, int destination,
            T... edgeVariables) {
        checkInvalidNrOfEdgeVariables(edgeVariables.length);
        
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
            int edgeVariableIndex) {
        List<Edge<T>> adjacencyList = getAdjacencyList(source);

        for(Edge<T> edge : adjacencyList) {
            if(edge.getDestination() == destination) {
                edge.setEdgeVariable(edgeVariable, edgeVariableIndex);
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

                nrOfEdges--;
                return;
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
    
    public int getNrOfVertices() {
        return nrOfVertices;
    }
    
    public int getNrOfEdges() {
        return nrOfEdges;
    }
    
    public int getNrOfEdgeVariables() {
        return nrOfEdgeVariables;
    }
    
    private static void checkNegativeNrOfVertices(int nrOfVertices) {
        if(nrOfVertices < 0) {
            throw new IllegalArgumentException("You cannot have a negative "
                    + "number of vertices.");
        }  
    }
    
    private static void checkNegativeNrOfEdgeVariables(int nrOfEdgeVariables) {
        if(nrOfEdgeVariables < 0) {
            throw new IllegalArgumentException("You cannot have a negative "
                    + "number of edge variables.");
        }    
    }

    private void checkInvalidVertex(int vertex) {
        if(vertex < 0 || vertex >= nrOfVertices) {
            throw new IllegalArgumentException(String.format("Vertex %d does "
                    + "not exist in the graph.", vertex));
        }
    }

    private void checkAlreadyHasEdge(int source, int destination) {
        if(hasEdge(source, destination)) {
            throw new IllegalArgumentException(String.format("Edge (%d,%d) "
                    + "already exists in the graph.", source, destination));
        }
    }

    private void checkInvalidNrOfEdgeVariables(int nrOfEdgeVariables) {
        if(this.nrOfEdgeVariables != nrOfEdgeVariables) {
            throw new IllegalArgumentException(String.format("The edges of the "
                    + "graph have %d edge variable(s), but %d edge variable(s) "
                    + "were given.", this.nrOfEdgeVariables, nrOfEdgeVariables));
        }
    }
    
    public void printGraph() {
        List<Edge<T>> edges ;
        for(int vertex = 0; vertex< nrOfVertices; vertex++){
            edges = getAdjacencyList(vertex);
            for(int j = 0; j<edges.size(); j++) {
                edges.get(j).printEdge();
            }
        }
    }
}
