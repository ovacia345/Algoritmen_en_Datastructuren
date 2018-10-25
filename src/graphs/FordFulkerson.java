package graphs;

import java.util.List;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class FordFulkerson {
    private final int inputGraphNrOfEdgeVariables;
    private final int inputGraphCapacityEdgeVariableIndex;

    private final int flowGraphNrOfEdgeVariables;
    private final int flowGraphFlowEdgeVariableIndex;
    private final int flowGraphCapacityEdgeVariableIndex;

    public FordFulkerson(int inputGraphNrOfEdgeVariables, int inputGraphCapacityEdgeVariableIndex) {
        this.inputGraphNrOfEdgeVariables = inputGraphNrOfEdgeVariables;
        this.inputGraphCapacityEdgeVariableIndex = inputGraphCapacityEdgeVariableIndex;

        flowGraphNrOfEdgeVariables = inputGraphNrOfEdgeVariables + 1;
        flowGraphFlowEdgeVariableIndex = inputGraphCapacityEdgeVariableIndex;
        flowGraphCapacityEdgeVariableIndex = flowGraphFlowEdgeVariableIndex + 1;
    }

    public <T extends Number> Graph<T> run(Graph<T> graph, int source,
            int sink) {
        checkNullGraph(graph);
        checkInvalidNrOfEdgeVariables(graph, inputGraphNrOfEdgeVariables);
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

    public <T extends Number> T getFlowValue(Graph<T> flowGraph, int source) {
        checkNullGraph(flowGraph);
        checkInvalidNrOfEdgeVariables(flowGraph, flowGraphNrOfEdgeVariables);
        checkInvalidVertex(flowGraph, source);

        List<Edge<T>> adjacencyList = flowGraph.getAdjacencyList(source);

        if(!adjacencyList.isEmpty()) {
            double flowValue = 0.0d;
            for(Edge<T> edge : adjacencyList) {
                T flow = edge.getEdgeVariable(flowGraphFlowEdgeVariableIndex);
                flowValue += flow.doubleValue();
            }

            T reference = adjacencyList.get(0).getEdgeVariable(flowGraphFlowEdgeVariableIndex);
            return getNumberWithReferenceType((T) new Double(flowValue), reference);
        }

        throw new IllegalArgumentException("The degree of the source vertex is 0.");
    }

    private <T extends Number> Graph makeFlowGraph(Graph<T> graph) {
        int nrOfVertices = graph.getNrOfVertices();
        Graph flowGraph = new Graph(nrOfVertices, flowGraphNrOfEdgeVariables);

        for(int vertexU = 0; vertexU < nrOfVertices; vertexU++) {
            List<Edge<T>> adjacencyListVertexU = graph.getAdjacencyList(vertexU);

            for(Edge<T> edgeUV : adjacencyListVertexU) {
                int vertexV = edgeUV.getDestination();
                
                Number[] edgeVariables = new Number[flowGraphNrOfEdgeVariables];
                for(int inputGraphEdgeVariableIndex = 0, flowGraphEdgeVariableIndex = 0;
                        flowGraphEdgeVariableIndex < flowGraphNrOfEdgeVariables;
                        inputGraphEdgeVariableIndex++, flowGraphEdgeVariableIndex++) {
                    if(flowGraphEdgeVariableIndex == flowGraphFlowEdgeVariableIndex) {
                        flowGraphEdgeVariableIndex++;
                    }

                    edgeVariables[flowGraphEdgeVariableIndex] = edgeUV.getEdgeVariable(
                            inputGraphEdgeVariableIndex);
                }

                T reference = edgeUV.getEdgeVariable(inputGraphCapacityEdgeVariableIndex);
                T flow = getNumberWithReferenceType((T) new Integer(0), reference);
                edgeVariables[flowGraphFlowEdgeVariableIndex] = flow;

                flowGraph.addEdge(vertexU, vertexV, edgeVariables);
            }
        }

        return flowGraph;
    }

    private <T extends Number> Graph makeResidualGraph(Graph<T> graph) {
        int nrOfVertices = graph.getNrOfVertices();
        Graph residualGraph = new Graph(nrOfVertices, 1);

        for(int vertexU = 0; vertexU < nrOfVertices; vertexU++) {
            List<Edge<T>> adjacencyListVertexU = graph.getAdjacencyList(vertexU);

            for(Edge<T> edgeUV : adjacencyListVertexU) {
                int vertexV = edgeUV.getDestination();

                T residualCapacity = edgeUV.getEdgeVariable(inputGraphCapacityEdgeVariableIndex);
                residualGraph.addEdge(vertexU, vertexV, residualCapacity);
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
        T reference = path.get(0).getEdgeVariable(0);
        double pathResidualCapacity = reference.doubleValue();

        for(Edge<T> edge : path) {
            T residualCapacity = edge.getEdgeVariable(0);
            pathResidualCapacity = Math.min(pathResidualCapacity,
                    residualCapacity.doubleValue());
        }

        return getNumberWithReferenceType((T) new Double(pathResidualCapacity), reference);
    }

    private <T extends Number> void augmentFlow(Graph<T> flowGraph,
            Graph residualGraph, Edge edge, T pathResidualCapacity) {
        int vertexU = edge.getSource();
        int vertexV = edge.getDestination();

        if (flowGraph.hasEdge(vertexU, vertexV)) {
            T[] edgeVariables = flowGraph.getEdgeVariables(vertexU, vertexV);
            T oldFlow = edgeVariables[flowGraphFlowEdgeVariableIndex];
            T capacity = edgeVariables[flowGraphCapacityEdgeVariableIndex];
            if (oldFlow.doubleValue() == 0.0d) {
                residualGraph.addEdge(vertexV, vertexU, 0);
            }

            T newFlow = getNumberWithReferenceType(
                    (T) new Double(oldFlow.doubleValue() + pathResidualCapacity.doubleValue()),
                    capacity);
            flowGraph.setEdgeVariable(vertexU, vertexV, newFlow, flowGraphFlowEdgeVariableIndex);

            if (newFlow.doubleValue() == capacity.doubleValue()) {
                residualGraph.removeEdge(vertexU, vertexV);
            } else {
                T residualCapacity = getNumberWithReferenceType(
                        (T) new Double(capacity.doubleValue() - newFlow.doubleValue()),
                        capacity);
                edge.setEdgeVariable(residualCapacity, 0);
            }
            residualGraph.setEdgeVariable(vertexV, vertexU,
                    newFlow, 0);
        } else {
            T[] edgeVariables = flowGraph.getEdgeVariables(vertexV, vertexU);
            T oldFlow = edgeVariables[flowGraphFlowEdgeVariableIndex];
            T capacity = edgeVariables[flowGraphCapacityEdgeVariableIndex];
            if (oldFlow.doubleValue() == capacity.doubleValue()) {
                residualGraph.addEdge(vertexV, vertexU, 0);
            }

            T newFlow = getNumberWithReferenceType(
                    (T) new Double(oldFlow.doubleValue() - pathResidualCapacity.doubleValue()),
                    capacity);
            flowGraph.setEdgeVariable(vertexV, vertexU, newFlow, flowGraphFlowEdgeVariableIndex);

            if (newFlow.doubleValue() == 0.0d) {
                residualGraph.removeEdge(vertexU, vertexV);
            } else {
                edge.setEdgeVariable(newFlow, 0);
            }
            T residualCapacity = getNumberWithReferenceType(
                    (T) new Double(capacity.doubleValue() - newFlow.doubleValue()),
                    capacity);
            residualGraph.setEdgeVariable(vertexV, vertexU,
                    residualCapacity, 0);
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

    private <T extends Number> void checkNonPositiveCapacities(Graph<T> graph) {
        int nrOfVertices = graph.getNrOfVertices();
        for (int vertex = 0; vertex < nrOfVertices; vertex++) {
            for (Edge<T> edge : graph.getAdjacencyList(vertex)) {
                T capacity = edge.getEdgeVariable(inputGraphCapacityEdgeVariableIndex);
                if (capacity.doubleValue() <= 0.0d) {
                    throw new IllegalArgumentException("Non-positive capacities"
                            + " are not allowed.");
                }
            }
        }
    }
}
