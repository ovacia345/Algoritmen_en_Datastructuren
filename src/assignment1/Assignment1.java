package assignment1;

import java.util.Arrays;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class Assignment1 {
    public static void runAssignment1() {
        // Reading in the number of boxes
        int nrOfBoxes = IOHandler.getInteger();

        // Reading in all the boxes
        Box[] boxes = readBoxes(nrOfBoxes);

        // Creating bipartite graph
        Graph graph = createGraph(boxes, nrOfBoxes);

        // Algorithm using Hopcroft-Karp
        int nrOfMatches = FordFulkerson.hopcroftKarp(graph, nrOfBoxes);

        // Printing minimum number of visible boxes
        IOHandler.print(Integer.toString(nrOfBoxes - nrOfMatches));
    }

    private static Box[] readBoxes(int nrOfBoxes) {
        Box[] boxes = new Box[nrOfBoxes]; // Initialize Box array

        for(int i = 0; i < nrOfBoxes; i++) {
            double xLength = IOHandler.getDouble();
            double yLength = IOHandler.getDouble();
            double zLength = IOHandler.getDouble();
            boxes[i] = new Box(xLength, yLength, zLength);
        }

        return boxes;
    }
    
    private static Graph createGraph(Box[] boxes, int nrOfBoxes) {
        int nrOfVertices = nrOfBoxes * 2 + 2; // bipartite, +sink +source
        Graph graph = new Graph(nrOfVertices); // source = souce, sink = sink;
        int source = 0; // First vertex
        int sink = nrOfVertices - 1; // Last vertex
        //source connections  + sink connections
        for(int box  = 1 ; box <= nrOfBoxes; box++) {
            graph.addEdge(source, box);
            graph.addEdge(box + nrOfBoxes, sink);
        }
        
        // {@code boxes} is sorted based on volume from smallest to biggest
        Arrays.sort(boxes);
        // Then every pair of boxes, such that the second in the pair has a bigger
        // volume, is tested and if the two boxes fit, an edge is added.
        for(int smallBox = 0; smallBox < nrOfBoxes - 1; smallBox++){
            for(int bigBox = smallBox + 1; bigBox < nrOfBoxes; bigBox++){
                if(boxes[smallBox].fitsIn(boxes[bigBox])) {
                    graph.addEdge(smallBox + 1, bigBox + 1 + nrOfBoxes);    // smallBox has vertex i + 1
                                                                            // bigBox has vertex j + 1 + nrOfBoxes
                }          
            }
        }

        return graph;
    }
}
