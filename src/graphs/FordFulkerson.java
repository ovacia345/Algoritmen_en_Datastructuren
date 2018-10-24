package graphs;

import java.util.List;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class FordFulkerson {
    private final static int NR_EDGE_VARIABLES_INPUT_GRAPH = 1;
    private final static int NR_EDGE_VARIABLES_FLOW_GRAPH = 2;
    private final static int NR_EDGE_VARIABLES_RESIDUAL_GRAPH = 1;

    public static <T extends Number> Graph<T> run(Graph<T> graph, int source,
            int sink) {
        checkNullGraph(graph);
        checkInvalidNrEdgeVariables(graph);
        checkInvalidVertex(graph, source);
        checkInvalidVertex(graph, sink);
        checkEqualSourceSink(source, sink);
        checkAntiParallelEdges(graph);
        checkNonPositiveEdgeWeights(graph);

        Graph flowGraph = makeFlowGraph(graph);
        Graph residualGraph = makeResidualGraph(flowGraph);

        Edge<T>[] parentEdges = BFS.run(residualGraph, source);
        while(parentEdges[sink] != null) {
            List<Edge<T>> path = BFS.getPath(parentEdges, sink);
            T pathResidualCapacity = getPathResidualCapacity(path);

            for(Edge<T> edge : path) {
                augmentFlow(flowGraph, residualGraph, edge, pathResidualCapacity);
            }

            parentEdges = BFS.run(residualGraph, source);
        }

        return flowGraph;
    }



    public static <T extends Number> T getFlowValue(Graph<T> flowGraph) {

    }

    private static <T extends Number> Graph<T> makeFlowGraph(Graph<T> graph) {
        int nrVertices = graph.getNrVertices();
        Graph<T> flowGraph = new Graph<>(nrVertices, NR_EDGE_VARIABLES_FLOW_GRAPH);

        for(int vertexU = 0; vertexU < nrVertices; vertexU++) {
            List<Edge<T>> adjacencyListU = graph.getAdjacencyList(vertexU);

            for(Edge<T> edgeUV : adjacencyListU) {
                int vertexV = edgeUV.getDestination();
                T flow = (T) new Double(0.0d);
                T capacity = edgeUV.getEdgeVariable(0);

                flowGraph.addEdge(vertexU, vertexV, flow, capacity);
            }
        }

        return flowGraph;
    }

    private static <T extends Number> Graph<T> makeResidualGraph(Graph<T> graph) {
        int nrVertices = graph.getNrVertices();
        Graph<T> residualGraph = new Graph<>(nrVertices,
                NR_EDGE_VARIABLES_RESIDUAL_GRAPH);

        for(int vertexU = 0; vertexU < nrVertices; vertexU++) {
            List<Edge<T>> adjacencyListU = graph.getAdjacencyList(vertexU);

            for(Edge<T> edgeUV : adjacencyListU) {
                int vertexV = edgeUV.getDestination();
                T[] edgeUVVariables = edgeUV.getEdgeVariables();
                T flow = edgeUVVariables[0];
                T capacity = edgeUVVariables[1];

                if(flow.doubleValue() > 0.0d) {
                    residualGraph.addEdge(vertexV, vertexU, flow);
                }
                if(flow.doubleValue() < capacity.doubleValue()) {
                    T residualCapacity = (T) new Double(capacity.doubleValue() -
                            flow.doubleValue());
                    residualGraph.addEdge(vertexU, vertexV, residualCapacity);
                }
            }
        }

        return residualGraph;
    }

    private static <T extends Number> T getPathResidualCapacity(List<Edge<T>> path) {
        double pathResidualCapacity = Double.MAX_VALUE;

        for(Edge<T> edge : path) {
            T residualCapacity = edge.getEdgeVariable(0);
            pathResidualCapacity = Math.min(pathResidualCapacity,
                    residualCapacity.doubleValue());
        }

        return (T) new Double(pathResidualCapacity);
    }

    private static <T extends Number> void augmentFlow(Graph<T> flowGraph,
            Graph<T> residualGraph, Edge<T> edge, T pathResidualCapacity) {
        int vertexU = edge.getSource();
        int vertexV = edge.getDestination();

        if (flowGraph.hasEdge(vertexU, vertexV)) {
            T[] edgeVariables = flowGraph.getEdgeVariables(vertexU, vertexV);
            T oldFlow = edgeVariables[0];
            T capacity = edgeVariables[1];
            if (oldFlow.doubleValue() == 0.0d) {
                residualGraph.addEdge(vertexV, vertexU, oldFlow);
            }

            T newFlow = (T) new Double(oldFlow.doubleValue() +
                    pathResidualCapacity.doubleValue());
            flowGraph.setEdgeVariable(vertexU, vertexV, newFlow, 1);

            if (newFlow.doubleValue() == capacity.doubleValue()) {
                residualGraph.removeEdge(vertexU, vertexV);
            } else {
                T newResidualCapacity = (T) new Double(capacity.doubleValue() -
                        newFlow.doubleValue());
                residualGraph.setEdgeVariable(vertexU, vertexV,
                        newResidualCapacity, 1);
            }
            residualGraph.setEdgeVariable(vertexV, vertexU, newFlow, 1);
        } else {
            T[] edgeVariables = flowGraph.getEdgeVariables(vertexV, vertexU);
            T oldFlow = edgeVariables[0];
            T capacity = edgeVariables[1];
            if (oldFlow.doubleValue() == capacity.doubleValue()) {
                residualGraph.addEdge(vertexV, vertexU, (T) new Double(0.0d));
            }

            T newFlow = (T) new Double(oldFlow.doubleValue() -
                    pathResidualCapacity.doubleValue());
            flowGraph.setEdgeVariable(vertexV, vertexU, newFlow, 1);

            if (newFlow.doubleValue() == 0.0d) {
                residualGraph.removeEdge(vertexU, vertexV);
            } else {
                residualGraph.setEdgeVariable(vertexU, vertexV,
                        newFlow, 1);
            }
            T newResidualCapacity = (T) new Double(capacity.doubleValue() -
                        newFlow.doubleValue());
            residualGraph.setEdgeVariable(vertexV, vertexU, newResidualCapacity, 1);
        }
    }

    private static void checkNullGraph(Graph graph) {
        if(graph == null) {
            throw new IllegalArgumentException("A graph with a null pointer "
                    + "was given.");
        }
    }

    private static void checkInvalidNrEdgeVariables(Graph graph) {
        int nrEdgeVariables = graph.getNrEdgeVariables();
        if(NR_EDGE_VARIABLES_INPUT_GRAPH != nrEdgeVariables) {
            throw new IllegalArgumentException(String.format("The input graph "
                    + "must have exactly %d edge variable(s) per edge.",
                    NR_EDGE_VARIABLES_INPUT_GRAPH));
        }
    }

    private static void checkInvalidVertex(Graph graph, int vertex) {
        int nrVertices = graph.getNrVertices();
        if(vertex < 0 || vertex >= nrVertices) {
            throw new IllegalArgumentException(String.format("Vertex %d does "
                    + "not exist in the graph.", vertex));
        }
    }

    private static void checkEqualSourceSink(int source, int sink) {
        if(source == sink) {
            throw new IllegalArgumentException("Source and sink are equal.");
        }
    }

    private static void checkAntiParallelEdges(Graph graph) {
        int nrVertices = graph.getNrVertices();
        for (int vertexU = 0; vertexU < nrVertices; vertexU++) {
            for (int vertexV = vertexU + 1; vertexV < nrVertices; vertexV++) {
                if (graph.hasEdge(vertexU, vertexV) &&
                        graph.hasEdge(vertexV, vertexU)) {
                    throw new IllegalArgumentException("The input graph in a "
                            + "Ford-Fulkerson method cannot have anti-parallel "
                            + "edges.");
                }
            }
        }
    }

    private static <T extends Number> void checkNonPositiveEdgeWeights(Graph<T> graph) {
        int nrVertices = graph.getNrVertices();
        for (int vertex = 0; vertex < nrVertices; vertex++) {
            for (Edge<T> edge : graph.getAdjacencyList(vertex)) {
                T capacity = edge.getEdgeVariable(0);
                if (capacity.doubleValue() <= 0.0d) {
                    throw new IllegalArgumentException("Non-positive capacities"
                            + " are not allowed.");
                }
            }
        }
    }
}
