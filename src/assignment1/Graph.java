package assignment1;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class Graph {
    private final int nrOfVertices;
    private final List<int[]>[] adjacencyLists;

    public Graph(int nrOfVertices) {
        this.nrOfVertices = nrOfVertices;

        adjacencyLists = new List[nrOfVertices];
        for(int vertex = 0; vertex < nrOfVertices; vertex++) {
            adjacencyLists[vertex] = new LinkedList<>();
        }
    }

    public Graph(Graph graph) {
        this(graph.getNrOfVertices());

        for(List<int[]> adjacencyList : graph.adjacencyLists) {
            for(int[] edgeVariables : adjacencyList) {
                int vertexU = edgeVariables[0];
                int vertexV = edgeVariables[1];
                addEdge(vertexU, vertexV);
            }
        }
    }

    public void addEdge(int source, int destination) {
        adjacencyLists[source].add(new int[]{source, destination, 0, 1});
    }

    public List<int[]> getAdjacencyList(int vertex) {
        return adjacencyLists[vertex];
    }

    public int[] getEdgeVariables(int source, int destination) {
        List<int[]> adjacencyList = getAdjacencyList(source);

        for(int[] edgeVariables : adjacencyList) {
            if(edgeVariables[1] == destination) {
                return edgeVariables;
            }
        }

        return null;
    }

    public void setFlow(int source, int destination, int flow) {
        List<int[]> adjacencyList = getAdjacencyList(source);

        for(int[] edgeVariables : adjacencyList) {
            if(edgeVariables[1] == destination) {
                edgeVariables[2] = flow;
            }
        }
    }

    public void removeEdge(int source, int destination) {
        List<int[]> adjacencyList = getAdjacencyList(source);

        for(int[] edgeVariables : adjacencyList) {
            if(edgeVariables[1] == destination) {
                adjacencyList.remove(edgeVariables);
                return;
            }
        }
    }

    public boolean hasEdge(int source, int destination) {
        List<int[]> adjacencyList = getAdjacencyList(source);

        for(int[] edgeVariables : adjacencyList) {
            if(edgeVariables[1] == destination) {
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
            List<int[]> adjacencyList = adjacencyLists[vertex];
            for(int[] edgeVariables : adjacencyList) {
                stringBuilder.append(String.format("%d - %d:\t%d\t%d", vertex,
                        edgeVariables[1], edgeVariables[2], edgeVariables[3]));
            }
        }

        return stringBuilder.toString();
    }
}
