package assignment1;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class Graph {
    private final int nrOfVertices;
    private final Edge[][] adjacencyMatrix;

    public Graph(int nrOfVertices) {
        this.nrOfVertices = nrOfVertices;
        adjacencyMatrix = new Edge[nrOfVertices][nrOfVertices];
    }    
    
    public Graph(Graph graph) {
        this(graph.getNrOfVertices());

        for(int from = 0; from < nrOfVertices; from++) {
            for(int to = 0; to < nrOfVertices; to++) {
                if(graph.adjacencyMatrix[from][to] != null) {
                    addEdge(from, to);
                }
            }
        }
    }

    /**
     * Add an edge from {@code from} to {@code to} with flow 0 and capacity 1 or
     * residual capacity 1.
     * @param from the source vertex
     * @param to the destination vertex
     */
    public void addEdge(int from, int to) {
        adjacencyMatrix[from][to] = new Edge(from, to);
    }

    /**
     * Get the source of this graph in the context of a Ford-Fulkerson method.
     * @return source of the graph
     */
    public int getSource() {
        return 0;
    }

    /**
     * Get the sink of this graph in the context of a Ford-Fulkerson mathod.
     * @return sink of the graph
     */
    public int getSink() {
        return nrOfVertices - 1;
    }

    /**
     * Get the array of edges from {@code from} to all vertices.
     * @param from the source vertex
     * @return an array of all the edges from from to all vertices. The array
     * can have null edges
     */
    public Edge[] getAdjacencyList(int from) {
        return adjacencyMatrix[from];
    }

    /**
     * Set flow of edge from {@code from} to {@code to} to {@code flow}.
     * @param from the source vertex
     * @param to the destination vertex
     * @param flow the new value for the flow of the edge
     */
    public void setFlow(int from, int to, int flow) {
        adjacencyMatrix[from][to].setFlow(flow);
    }

    /**
     * Get the edge from {@code from} to {@code to}.
     * @param from the source vertex
     * @param to the destination vertex
     * @return the edge from {@code from} to {@code to}
     */
    public Edge getEdge(int from, int to) {
        return adjacencyMatrix[from][to];        
    }

    /**
     * Remove the edge from {@code from} to {@code to}.
     * @param from the source vertex
     * @param to the destination vertex
     */
    public void removeEdge(int from, int to){
        adjacencyMatrix[from][to] = null;
    }

    /**
     * Returns whether or not the edge from {@code from} to {@code to} exists
     * in this graph.
     * @param from the source vertex
     * @param to the destination vertex
     * @return whether or not the edge exists in the graph
     */
    public boolean hasEdge(int from, int to){
        return adjacencyMatrix[from][to] != null;
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
            for(int to = 0; to < nrOfVertices; to++) {
                Edge edge = adjacencyMatrix[from][to];
                if(edge != null) {
                    stringBuilder.append(edge);
                }
            }
        }

        return stringBuilder.toString();
    }
}
