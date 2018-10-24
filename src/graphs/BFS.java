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
        checkInvalidVertex(graph, source);

        int nrVertices = graph.getNrVertices();
        Color[] colors = new Color[nrVertices];
        Edge<T>[] parentEdges = new Edge[nrVertices];

        for(int vertex = 0; vertex < nrVertices; vertex++) {
            colors[vertex] = Color.WHITE;
        }

        colors[source] = Color.GRAY;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);

        while(!queue.isEmpty()) {
            int vertexU = queue.remove();
            List<Edge<T>> adjacencyListU = graph.getAdjacencyList(vertexU);

            for(Edge<T> edgeUV : adjacencyListU) {
                int vertexV = edgeUV.getDestination();
                if(colors[vertexV] == Color.WHITE) {
                    colors[vertexV] = Color.GRAY;
                    parentEdges[vertexV] = edgeUV;
                }
            }

            colors[vertexU] = Color.BLACK;
        }

        return parentEdges;
    }

    public static <T extends Number> List<Edge<T>> getPath(Edge<T>[] parentEdges,
            int child) {
        return getPathRecursive(parentEdges, child, 0);
    }

    private static <T extends Number> List<Edge<T>> getPathRecursive(
    Edge<T>[] parentEdges, int child, int nrRecursiveCalls) {
        if (parentEdges[child] == null) {
            List<Edge<T>> path = new LinkedList<>();
            return path;
        }

        nrRecursiveCalls++;
        if(nrRecursiveCalls < parentEdges.length) {
            Edge<T> parentEdge = parentEdges[child];

            int parent = parentEdge.getSource();
            List<Edge<T>> path = getPathRecursive(parentEdges, parent,
                    nrRecursiveCalls);

            path.add(parentEdge);
            return path;
        }

        throw new IllegalArgumentException("A cycle has been detected");
    }

    private static void checkNullGraph(Graph graph) {
        if(graph == null) {
            throw new IllegalArgumentException("A graph with a null pointer "
                    + "was given.");
        }
    }

    private static void checkInvalidVertex(Graph graph, int vertex) {
        if(vertex < 0 || vertex >= graph.getNrVertices()) {
            throw new IllegalArgumentException(String.format("Vertex %d does "
                    + "not exist in the graph.", vertex));
        }
    }
}
