package assignment1;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class Graph {
    private final int nrOfVertices;
    private final List<Integer>[] adjacencyLists;

    /**
     * Initialize graph with {@code nrOfVertices} vertices.
     * @param nrOfVertices the number of vertices of the graph
     */
    public Graph(int nrOfVertices) {
        this.nrOfVertices = nrOfVertices;

        adjacencyLists = new List[nrOfVertices];
        for(int vertex = 0; vertex < nrOfVertices; vertex++) {
            adjacencyLists[vertex] = new ArrayList<>();
        }
    }    

    /**
     * Add an edge from {@code from} to {@code to} to this graph.
     * @param from the source vertex
     * @param to the destination vertex
     */
    public void addEdge(int from, int to) {
        adjacencyLists[from].add(to);
    }

    /**
     * Get the list of adjacent vertices of vertex {@code vertex}.
     * @param vertex the source vertex
     * @return a list of all the vertices that are reachable through exactly one
     * edge from vertex {@code vertex}
     */
    public List<Integer> getAdjacencyList(int vertex) {
        return adjacencyLists[vertex];
    }

    /**
     * Get the number of vertices of this graph.
     * @return number of vertices of the graph
     */
    public int getNrOfVertices() {
        return nrOfVertices;
    }

    /**
     * Returns a {@code String} representation of this graph according to the
     * {@code toString()} of the edges.
     * @return {@code String} representation of the graph
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for(int from = 0; from < nrOfVertices; from++) {
            List<Integer> adjacencyList = adjacencyLists[from];
            for(int to : adjacencyList) {
                stringBuilder.append(String.format("%d - %d\n", from, to));
            }
        }

        return stringBuilder.toString();
    }
}
