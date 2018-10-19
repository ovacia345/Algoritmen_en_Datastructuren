package graphs;

import java.util.List;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class Graph<T> {
    private final int nrVertices;
    private final List<Integer>[] adjacencyLists;


    public Graph(int nrVertices) {
        this.nrVertices = nrVertices;

        adjacencyLists = new List[nrVertices];
    }

    public void addEdge(int source, int destination) {
        adjacencyLists[source].add(destination);
    }
}
