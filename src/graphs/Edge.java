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

    public T getEdgeVariable(int edgeVariableNr) {
        checkInvalidEdgeVariableNr(edgeVariableNr);

        return edgeVariables[edgeVariableNr];
    }

    public void setEdgeVariables(T... edgeVariables) {
        checkInvalidNrEdgeVariables(edgeVariables.length);

        this.edgeVariables = edgeVariables;
    }

    public void setEdgeVariable(T edgeVariable, int edgeVariableNr) {
        checkInvalidEdgeVariableNr(edgeVariableNr);

        edgeVariables[edgeVariableNr] = edgeVariable;
    }

    private void checkInvalidEdgeVariableNr(int edgeVariableNr) {
        if(edgeVariableNr < 0 || edgeVariableNr >= edgeVariables.length) {
            throw new IllegalArgumentException(String.format("Edge variable "
                    + "%d does not exist in edge (%d,%d).", edgeVariableNr,
                    source, destination));
        }
    }

    private void checkInvalidNrEdgeVariables(int nrEdgeVariables) {
        if(edgeVariables.length != nrEdgeVariables) {
            throw new IllegalArgumentException(String.format("The edge has "
                    + "%d edge variables, but %d edge variables are given.",
                    edgeVariables.length, nrEdgeVariables));
        }
    }
    
    public void printEdge() {
        StringBuilder stringBuilder = new StringBuilder();
        
        stringBuilder.append(String.format("source: %d\t", source));
        stringBuilder.append(String.format("Destination: %d\t", destination));
        
        if(edgeVariables.length > 0) {
            stringBuilder.append("Edge Variables:\t");
            for(T edgeVariable : edgeVariables) {
                stringBuilder.append(String.format("%f\t", edgeVariable.doubleValue()));
            }
        }
        
        System.out.println(stringBuilder);
    }
}
