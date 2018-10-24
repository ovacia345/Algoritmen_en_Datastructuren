package graphs;

import java.util.List;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class FordFulkerson {
    private final static int INPUT_GRAPH_NR_EDGE_VARIABLES = 1;
    private final static int INPUT_GRAPH_CAPACITY_VARIABLE_NR = 0;

    private final static int FLOW_GRAPH_NR_EDGE_VARIABLES = 2;
    private final static int FLOW_GRAPH_FLOW_VARIABLE_NR = 0;
    private final static int FLOW_GRAPH_CAPACITY_VARIABLE_NR = 1;

    private final static int RESIDUAL_GRAPH_NR_EDGE_VARIABLES = 1;
    private final static int RESIDUAL_GRAPH_RESIDUAL_CAPACITY_VARIABLE_NR = 0;

    public static <T extends Number> Graph<T> run(Graph<T> graph, int source,
            int sink) {
        checkNullGraph(graph);
        checkInvalidNrEdgeVariables(graph, INPUT_GRAPH_NR_EDGE_VARIABLES);
        checkInvalidVertex(graph, source);
        checkInvalidVertex(graph, sink);
        checkEqualSourceSink(source, sink);
        checkAntiParallelEdges(graph);
        checkNonPositiveCapacities(graph);

        Graph flowGraph = makeFlowGraph(graph);
        Graph residualGraph = graph;

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

    public static <T extends Number> T getFlowValue(Graph<T> flowGraph, int source) {
        checkNullGraph(flowGraph);
        checkInvalidNrEdgeVariables(flowGraph, FLOW_GRAPH_NR_EDGE_VARIABLES);
        checkInvalidVertex(flowGraph, source);

        List<Edge<T>> adjacencyList = flowGraph.getAdjacencyList(source);

        if(!adjacencyList.isEmpty()) {
            T reference = adjacencyList.get(0).getEdgeVariable(FLOW_GRAPH_FLOW_VARIABLE_NR);
            double flowValue = reference.doubleValue();

            for(int vertex = 1; vertex < adjacencyList.size(); vertex++) {
                Edge<T> edge = adjacencyList.get(vertex);
                flowValue += edge.getEdgeVariable(FLOW_GRAPH_FLOW_VARIABLE_NR).doubleValue();
            }

            return getNumberWithReferenceType(reference, (T) new Double(flowValue));
        }

        return (T) new Integer(0);
    }

    private static <T extends Number> Graph makeFlowGraph(Graph<T> graph) {
        int nrVertices = graph.getNrVertices();
        Graph flowGraph = new Graph(nrVertices, FLOW_GRAPH_NR_EDGE_VARIABLES);

        for(int vertexU = 0; vertexU < nrVertices; vertexU++) {
            List<Edge<T>> adjacencyListU = graph.getAdjacencyList(vertexU);

            for(Edge<T> edgeUV : adjacencyListU) {
                int vertexV = edgeUV.getDestination();

                T capacity = edgeUV.getEdgeVariable(INPUT_GRAPH_CAPACITY_VARIABLE_NR);
                T flow = getNumberWithReferenceType(capacity, (T) new Integer(0));

                flowGraph.addEdge(vertexU, vertexV, flow, capacity);
            }
        }

        return flowGraph;
    }

    private static <T extends Number> T getNumberWithReferenceType(T reference, T number) {
        if(reference.getClass() == number.getClass()) {
            return number;
        }
        if(reference instanceof Double && number instanceof Integer) {
            return (T) new Double(number.doubleValue());
        }
        if (reference instanceof Integer && number instanceof Double) {
            return (T) new Integer(number.intValue());
        }
        throw new IllegalArgumentException(String.format("Type %s is not "
                + "supported.", number.getClass()));
    }

    private static <T extends Number> T getPathResidualCapacity(List<Edge<T>> path) {
        double pathResidualCapacity = Double.MAX_VALUE;

        for(Edge<T> edge : path) {
            T residualCapacity = edge.getEdgeVariable(RESIDUAL_GRAPH_RESIDUAL_CAPACITY_VARIABLE_NR);
            pathResidualCapacity = Math.min(pathResidualCapacity,
                    residualCapacity.doubleValue());
        }

        T reference = path.get(0).getEdgeVariable(RESIDUAL_GRAPH_RESIDUAL_CAPACITY_VARIABLE_NR);
        return getNumberWithReferenceType(reference, (T) new Double(pathResidualCapacity));
    }

    private static <T extends Number> void augmentFlow(Graph<T> flowGraph,
            Graph<T> residualGraph, Edge<T> edge, T pathResidualCapacity) {
        int vertexU = edge.getSource();
        int vertexV = edge.getDestination();

        if (flowGraph.hasEdge(vertexU, vertexV)) {
            T[] edgeVariables = flowGraph.getEdgeVariables(vertexU, vertexV);
            T oldFlow = edgeVariables[FLOW_GRAPH_FLOW_VARIABLE_NR];
            T capacity = edgeVariables[FLOW_GRAPH_CAPACITY_VARIABLE_NR];
            if (oldFlow.doubleValue() == 0.0d) {
                residualGraph.addEdge(vertexV, vertexU, (T) new Integer(0));
            }

            T newFlow = getNumberWithReferenceType(capacity,
                    (T) new Double(oldFlow.doubleValue() + pathResidualCapacity.doubleValue()));
            flowGraph.setEdgeVariable(vertexU, vertexV, newFlow, FLOW_GRAPH_FLOW_VARIABLE_NR);

            if (newFlow.doubleValue() == capacity.doubleValue()) {
                residualGraph.removeEdge(vertexU, vertexV);
            } else {
                T residualCapacity = getNumberWithReferenceType(capacity,
                    (T) new Double(capacity.doubleValue() - newFlow.doubleValue()));
                residualGraph.setEdgeVariable(vertexU, vertexV,
                        residualCapacity, RESIDUAL_GRAPH_RESIDUAL_CAPACITY_VARIABLE_NR);
            }
            residualGraph.setEdgeVariable(vertexV, vertexU, newFlow, RESIDUAL_GRAPH_RESIDUAL_CAPACITY_VARIABLE_NR);
        } else {
            T[] edgeVariables = flowGraph.getEdgeVariables(vertexV, vertexU);
            T oldFlow = edgeVariables[FLOW_GRAPH_FLOW_VARIABLE_NR];
            T capacity = edgeVariables[FLOW_GRAPH_CAPACITY_VARIABLE_NR];
            if (oldFlow.doubleValue() == capacity.doubleValue()) {
                residualGraph.addEdge(vertexV, vertexU, (T) new Integer(0));
            }

            T newFlow = getNumberWithReferenceType(capacity,
                    (T) new Double(oldFlow.doubleValue() - pathResidualCapacity.doubleValue()));
            flowGraph.setEdgeVariable(vertexV, vertexU, newFlow, FLOW_GRAPH_FLOW_VARIABLE_NR);

            if (newFlow.doubleValue() == 0.0d) {
                residualGraph.removeEdge(vertexU, vertexV);
            } else {
                residualGraph.setEdgeVariable(vertexU, vertexV,
                        newFlow, RESIDUAL_GRAPH_RESIDUAL_CAPACITY_VARIABLE_NR);
            }
            T residualCapacity = getNumberWithReferenceType(capacity,
                    (T) new Double(capacity.doubleValue() - newFlow.doubleValue()));
            residualGraph.setEdgeVariable(vertexV, vertexU,
                    residualCapacity, RESIDUAL_GRAPH_RESIDUAL_CAPACITY_VARIABLE_NR);
        }
    }

    private static void checkNullGraph(Graph graph) {
        if(graph == null) {
            throw new IllegalArgumentException("A graph with a null pointer "
                    + "was given.");
        }
    }

    private static void checkInvalidNrEdgeVariables(Graph graph, int nrEdgeVariables) {
        if(nrEdgeVariables != graph.getNrEdgeVariables()) {
            throw new IllegalArgumentException(String.format("The edges of the "
                    + "input graph must have exactly %d edge variable(s). But "
                    + "they have %d edge variables.",
                    nrEdgeVariables, graph.getNrEdgeVariables()));
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
        for (int vertexU = 0; vertexU < nrVertices - 1; vertexU++) {
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

    private static <T extends Number> void checkNonPositiveCapacities(Graph<T> graph) {
        int nrVertices = graph.getNrVertices();
        for (int vertex = 0; vertex < nrVertices; vertex++) {
            for (Edge<T> edge : graph.getAdjacencyList(vertex)) {
                T capacity = edge.getEdgeVariable(INPUT_GRAPH_CAPACITY_VARIABLE_NR);
                if (capacity.doubleValue() <= 0.0d) {
                    throw new IllegalArgumentException("Non-positive capacities"
                            + " are not allowed.");
                }
            }
        }
    }
}
