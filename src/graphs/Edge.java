package graphs;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class Edge<T extends Number> {
    private final int source;
    private final int destination;
    private T[] edgeVariables;

    public Edge(int source, int destination, T... edgeVariables) {
        this.source = source;
        this.destination = destination;
        this.edgeVariables = edgeVariables;
    }

    public int getDestination() {
        return destination;
    }

    public T[] getEdgeVariables() {
        return edgeVariables;
    }

    public void setEdgeVariables(T... edgeVariables) {
        this.edgeVariables = edgeVariables;
    }
}
