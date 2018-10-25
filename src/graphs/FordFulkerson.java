package graphs;

import java.util.List;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class FordFulkerson {
    private final static int INPUT_GRAPH_NR_OF_EDGE_VARIABLES = 1;
    private final static int INPUT_GRAPH_CAPACITY_EDGE_VARIABLE_INDEX = 0;

    private final static int FLOW_GRAPH_NR_OF_EDGE_VARIABLES = 2;
    private final static int FLOW_GRAPH_FLOW_EDGE_VARIABLE_INDEX = 0;
    private final static int FLOW_GRAPH_CAPACITY_EDGE_VARIABLE_INDEX = 1;

    private final static int RESIDUAL_GRAPH_NR_OF_EDGE_VARIABLES = 1;
    private final static int RESIDUAL_GRAPH_RESIDUAL_CAPACITY_EDGE_VARIABLE_INDEX = 0;

    public static <T extends Number> Graph<T> run(Graph<T> graph, int source,
            int sink) {
        checkNullGraph(graph);
        checkInvalidNrOfEdgeVariables(graph, INPUT_GRAPH_NR_OF_EDGE_VARIABLES);
        checkInvalidVertex(graph, source);
        checkInvalidVertex(graph, sink);
        checkSourceEqualToSink(source, sink);
        checkAntiParallelEdges(graph);
        checkNonPositiveCapacities(graph);

        Graph flowGraph = makeFlowGraph(graph);
        Graph residualGraph = makeResidualGraph(graph);

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
        checkInvalidNrOfEdgeVariables(flowGraph, FLOW_GRAPH_NR_OF_EDGE_VARIABLES);
        checkInvalidVertex(flowGraph, source);

        List<Edge<T>> adjacencyList = flowGraph.getAdjacencyList(source);

        if(!adjacencyList.isEmpty()) {
            double flowValue = 0.0d;
            for(Edge<T> edge : adjacencyList) {
                T flow = edge.getEdgeVariable(FLOW_GRAPH_FLOW_EDGE_VARIABLE_INDEX);
                flowValue += flow.doubleValue();
            }

            T reference = adjacencyList.get(0).getEdgeVariable(FLOW_GRAPH_FLOW_EDGE_VARIABLE_INDEX);
            return getNumberWithReferenceType((T) new Double(flowValue), reference);
        }

        return (T) new Integer(1);
    }

    private static <T extends Number> Graph makeFlowGraph(Graph<T> graph) {
        int nrOfVertices = graph.getNrOfVertices();
        Graph flowGraph = new Graph(nrOfVertices, FLOW_GRAPH_NR_OF_EDGE_VARIABLES);

        for(int vertexU = 0; vertexU < nrOfVertices; vertexU++) {
            List<Edge<T>> adjacencyListVertexU = graph.getAdjacencyList(vertexU);

            for(Edge<T> edgeUV : adjacencyListVertexU) {
                int vertexV = edgeUV.getDestination();
                
                Number[] emptyEdgeVariables = new Number[FLOW_GRAPH_NR_OF_EDGE_VARIABLES];
                flowGraph.addEdge(vertexU, vertexV, emptyEdgeVariables);

                T capacity = edgeUV.getEdgeVariable(INPUT_GRAPH_CAPACITY_EDGE_VARIABLE_INDEX);
                flowGraph.setEdgeVariable(vertexU, vertexV, capacity, FLOW_GRAPH_CAPACITY_EDGE_VARIABLE_INDEX);

                T flow = getNumberWithReferenceType((T) new Integer(0), capacity);
                flowGraph.setEdgeVariable(vertexU, vertexV, flow, FLOW_GRAPH_FLOW_EDGE_VARIABLE_INDEX);
            }
        }

        return flowGraph;
    }

    private static <T extends Number> Graph makeResidualGraph(Graph<T> graph) {
        int nrOfVertices = graph.getNrOfVertices();
        Graph residualGraph = new Graph(nrOfVertices, RESIDUAL_GRAPH_NR_OF_EDGE_VARIABLES);

        for(int vertexU = 0; vertexU < nrOfVertices; vertexU++) {
            List<Edge<T>> adjacencyListVertexU = graph.getAdjacencyList(vertexU);

            for(Edge<T> edgeUV : adjacencyListVertexU) {
                int vertexV = edgeUV.getDestination();

                Number[] emptyEdgeVariables = new Number[RESIDUAL_GRAPH_NR_OF_EDGE_VARIABLES];
                residualGraph.addEdge(vertexU, vertexV, emptyEdgeVariables);

                T residualCapacity = edgeUV.getEdgeVariable(INPUT_GRAPH_CAPACITY_EDGE_VARIABLE_INDEX);
                residualGraph.setEdgeVariable(vertexU, vertexV,
                        residualCapacity, RESIDUAL_GRAPH_RESIDUAL_CAPACITY_EDGE_VARIABLE_INDEX);
            }
        }

        return residualGraph;
    }

    private static <T extends Number> T getNumberWithReferenceType(T number, T reference) {
        if(reference instanceof Double) {
            return (T) new Double(number.doubleValue());
        }
        if (reference instanceof Integer) {
            return (T) new Integer(number.intValue());
        }
        if (reference instanceof Byte) {
            return (T) new Byte(number.byteValue());
        }
        if (reference instanceof Short) {
            return (T) new Short(number.shortValue());
        }
        if (reference instanceof Long) {
            return (T) new Long(number.longValue());
        }
        if (reference instanceof Float) {
            return (T) new Float(number.floatValue());
        }

        throw new IllegalArgumentException(String.format("Type %s is not "
                + "supported.", reference.getClass()));
    }

    private static <T extends Number> T getPathResidualCapacity(List<Edge<T>> path) {
        T reference = path.get(0).getEdgeVariable(RESIDUAL_GRAPH_RESIDUAL_CAPACITY_EDGE_VARIABLE_INDEX);
        double pathResidualCapacity = reference.doubleValue();

        for(Edge<T> edge : path) {
            T residualCapacity = edge.getEdgeVariable(RESIDUAL_GRAPH_RESIDUAL_CAPACITY_EDGE_VARIABLE_INDEX);
            pathResidualCapacity = Math.min(pathResidualCapacity,
                    residualCapacity.doubleValue());
        }

        return getNumberWithReferenceType((T) new Double(pathResidualCapacity), reference);
    }

    private static <T extends Number> void augmentFlow(Graph<T> flowGraph,
            Graph residualGraph, Edge edge, T pathResidualCapacity) {
        int vertexU = edge.getSource();
        int vertexV = edge.getDestination();

        if (flowGraph.hasEdge(vertexU, vertexV)) {
            T[] edgeVariables = flowGraph.getEdgeVariables(vertexU, vertexV);
            T oldFlow = edgeVariables[FLOW_GRAPH_FLOW_EDGE_VARIABLE_INDEX];
            T capacity = edgeVariables[FLOW_GRAPH_CAPACITY_EDGE_VARIABLE_INDEX];
            if (oldFlow.doubleValue() == 0.0d) {
                Number[] emptyEdgeVariables = new Number[RESIDUAL_GRAPH_NR_OF_EDGE_VARIABLES];
                residualGraph.addEdge(vertexV, vertexU, emptyEdgeVariables);
            }

            T newFlow = getNumberWithReferenceType(
                    (T) new Double(oldFlow.doubleValue() + pathResidualCapacity.doubleValue()),
                    capacity);
            flowGraph.setEdgeVariable(vertexU, vertexV, newFlow, FLOW_GRAPH_FLOW_EDGE_VARIABLE_INDEX);

            if (newFlow.doubleValue() == capacity.doubleValue()) {
                residualGraph.removeEdge(vertexU, vertexV);
            } else {
                T residualCapacity = getNumberWithReferenceType(
                        (T) new Double(capacity.doubleValue() - newFlow.doubleValue()),
                        capacity);
                edge.setEdgeVariable(residualCapacity, RESIDUAL_GRAPH_RESIDUAL_CAPACITY_EDGE_VARIABLE_INDEX);
            }
            residualGraph.setEdgeVariable(vertexV, vertexU,
                    newFlow, RESIDUAL_GRAPH_RESIDUAL_CAPACITY_EDGE_VARIABLE_INDEX);
        } else {
            T[] edgeVariables = flowGraph.getEdgeVariables(vertexV, vertexU);
            T oldFlow = edgeVariables[FLOW_GRAPH_FLOW_EDGE_VARIABLE_INDEX];
            T capacity = edgeVariables[FLOW_GRAPH_CAPACITY_EDGE_VARIABLE_INDEX];
            if (oldFlow.doubleValue() == capacity.doubleValue()) {
                Number[] emptyEdgeVariables = new Number[RESIDUAL_GRAPH_NR_OF_EDGE_VARIABLES];
                residualGraph.addEdge(vertexV, vertexU, emptyEdgeVariables);
            }

            T newFlow = getNumberWithReferenceType(
                    (T) new Double(oldFlow.doubleValue() - pathResidualCapacity.doubleValue()),
                    capacity);
            flowGraph.setEdgeVariable(vertexV, vertexU, newFlow, FLOW_GRAPH_FLOW_EDGE_VARIABLE_INDEX);

            if (newFlow.doubleValue() == 0.0d) {
                residualGraph.removeEdge(vertexU, vertexV);
            } else {
                edge.setEdgeVariable(newFlow, RESIDUAL_GRAPH_RESIDUAL_CAPACITY_EDGE_VARIABLE_INDEX);
            }
            T residualCapacity = getNumberWithReferenceType(
                    (T) new Double(capacity.doubleValue() - newFlow.doubleValue()),
                    capacity);
            residualGraph.setEdgeVariable(vertexV, vertexU,
                    residualCapacity, RESIDUAL_GRAPH_RESIDUAL_CAPACITY_EDGE_VARIABLE_INDEX);
        }
    }

    private static void checkNullGraph(Graph graph) {
        if(graph == null) {
            throw new IllegalArgumentException("A graph with a null pointer "
                    + "was given.");
        }
    }

    private static void checkInvalidNrOfEdgeVariables(Graph graph, int nrOfEdgeVariables) {
        if(nrOfEdgeVariables != graph.getNrOfEdgeVariables()) {
            throw new IllegalArgumentException(String.format("The edges of the "
                    + "input graph must have exactly %d edge variable(s). But "
                    + "they have %d edge variables.",
                    nrOfEdgeVariables, graph.getNrOfEdgeVariables()));
        }
    }

    private static void checkInvalidVertex(Graph graph, int vertex) {
        int nrVertices = graph.getNrOfVertices();
        if(vertex < 0 || vertex >= nrVertices) {
            throw new IllegalArgumentException(String.format("Vertex %d does "
                    + "not exist in the graph.", vertex));
        }
    }

    private static void checkSourceEqualToSink(int source, int sink) {
        if(source == sink) {
            throw new IllegalArgumentException("Source and sink are equal.");
        }
    }

    private static void checkAntiParallelEdges(Graph graph) {
        int nrOfVertices = graph.getNrOfVertices();
        for (int vertexU = 0; vertexU < nrOfVertices - 1; vertexU++) {
            for (int vertexV = vertexU + 1; vertexV < nrOfVertices; vertexV++) {
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
        int nrOfVertices = graph.getNrOfVertices();
        for (int vertex = 0; vertex < nrOfVertices; vertex++) {
            for (Edge<T> edge : graph.getAdjacencyList(vertex)) {
                T capacity = edge.getEdgeVariable(INPUT_GRAPH_CAPACITY_EDGE_VARIABLE_INDEX);
                if (capacity.doubleValue() <= 0.0d) {
                    throw new IllegalArgumentException("Non-positive capacities"
                            + " are not allowed.");
                }
            }
        }
    }
}
