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

    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }

    public T[] getEdgeVariables() {
        return edgeVariables;
    }

    public T getEdgeVariable(int edgeVariableIndex) {
        checkInvalidEdgeVariableIndex(edgeVariableIndex);

        return edgeVariables[edgeVariableIndex];
    }

    public void setEdgeVariables(T... edgeVariables) {
        this.edgeVariables = edgeVariables;
    }

    public void setEdgeVariable(T edgeVariable, int edgeVariableIndex) {
        checkInvalidEdgeVariableIndex(edgeVariableIndex);

        edgeVariables[edgeVariableIndex] = edgeVariable;
    }

    private void checkInvalidEdgeVariableIndex(int edgeVariableIndex) {
        if(edgeVariableIndex < 0 || edgeVariableIndex >= edgeVariables.length) {
            throw new IllegalArgumentException(String.format("Edge variable "
                    + "%d does not exist in edge (%d,%d).", edgeVariableIndex,
                    source, destination));
        }
    }
    
    public void printEdge() {
        StringBuilder stringBuilder = new StringBuilder();
        
        stringBuilder.append(String.format("%d - %d:", source, destination));        
        for(T edgeVariable : edgeVariables) {
            stringBuilder.append(String.format("\t%s", edgeVariable));
        }
        
        System.out.println(stringBuilder);
    }
}
