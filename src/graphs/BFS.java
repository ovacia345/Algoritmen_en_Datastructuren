package graphs;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class BFS {
    private static enum Color {
        WHITE, GRAY, BLACK
    };

    public static <T extends Number> Edge<T>[] run(Graph<T> graph, int source) {
        checkNullGraph(graph);
        int nrOfVertices = graph.getNrOfVertices();
        checkInvalidVertex(nrOfVertices, source);

        Color[] colors = new Color[nrOfVertices];
        Edge<T>[] parentEdges = new Edge[nrOfVertices];

        for(int vertex = 0; vertex < nrOfVertices; vertex++) {
            colors[vertex] = Color.WHITE;
        }

        colors[source] = Color.GRAY;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);

        while(!queue.isEmpty()) {
            int vertexU = queue.remove();
            List<Edge<T>> adjacencyListVertexU = graph.getAdjacencyList(vertexU);

            for(Edge<T> edgeUV : adjacencyListVertexU) {
                int vertexV = edgeUV.getDestination();
                if(colors[vertexV] == Color.WHITE) {
                    colors[vertexV] = Color.GRAY;
                    parentEdges[vertexV] = edgeUV;
                    queue.add(vertexV);
                }
            }

            colors[vertexU] = Color.BLACK;
        }

        return parentEdges;
    }

    public static <T extends Number> List<Edge<T>> getPath(Edge<T>[] parentEdges,
            int lastVertex) {
        int nrOfVertices = parentEdges.length;

        checkInvalidVertex(nrOfVertices, lastVertex);
        for (int child = 0; child < nrOfVertices; child++) {
            Edge<T> parentEdge = parentEdges[child];

            if (parentEdge != null) {
                int parent = parentEdge.getSource();
                checkInvalidVertex(nrOfVertices, parent);

                int destination = parentEdge.getDestination();
                checkDestinationNotEqualToChild(destination, child);
            }
        }

        return getPathRecursive(parentEdges, lastVertex, 0);
    }

    private static <T extends Number> List<Edge<T>> getPathRecursive(
            Edge<T>[] parentEdges, int child, int pathLength) {
        if (parentEdges[child] == null) {
            List<Edge<T>> path = new LinkedList<>();
            return path;
        }

        pathLength++;
        if(pathLength < parentEdges.length) {
            Edge<T> parentEdge = parentEdges[child];

            int parent = parentEdge.getSource();
            List<Edge<T>> path = getPathRecursive(parentEdges, parent,
                    pathLength);

            path.add(parentEdge);
            return path;
        }

        throw new IllegalArgumentException("A cycle has been detected.");
    }

    private static void checkNullGraph(Graph graph) {
        if(graph == null) {
            throw new IllegalArgumentException("A graph with a null pointer "
                    + "was given.");
        }
    }

    private static void checkInvalidVertex(int nrOfVertices, int vertex) {
        if(vertex < 0 || vertex >= nrOfVertices) {
            throw new IllegalArgumentException(String.format("Vertex %d does "
                    + "not exist in the graph.", vertex));
        }
    }

    private static void checkDestinationNotEqualToChild(int destination, int child) {
        if(destination != child) {
            throw new IllegalArgumentException(String.format("Destination must "
                    + "be vertex %d, but is vertex %d.", destination, child));
        }
    }
}
