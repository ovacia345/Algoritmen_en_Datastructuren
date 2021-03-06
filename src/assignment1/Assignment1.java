package assignment1;

import java.util.Arrays;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class Assignment1 {
    public static void runAssignment() {
        // Reading in the number of boxes
        int nrOfBoxes = IOHandler.readInteger();

        // Reading in all the boxes
        Box[] boxes = readBoxes(nrOfBoxes);

        // Creating bipartite graph
        Graph graph = createGraph(boxes, nrOfBoxes);

        // Algorithm using Hopcroft-Karp
        int nrOfMatches = FordFulkerson.hopcroftKarp(graph, nrOfBoxes);

        // Printing minimum number of visible boxes
        IOHandler.write(Integer.toString(nrOfBoxes - nrOfMatches));
    }

    private static Box[] readBoxes(int nrOfBoxes) {
        Box[] boxes = new Box[nrOfBoxes];

        for(int box = 0; box < nrOfBoxes; box++) {
            double xLength = IOHandler.readDouble();
            double yLength = IOHandler.readDouble();
            double zLength = IOHandler.readDouble();
            boxes[box] = new Box(xLength, yLength, zLength);
        }

        return boxes;
    }
    
    private static Graph createGraph(Box[] boxes, int nrOfBoxes) {
        int nrOfVertices = nrOfBoxes * 2 + 1; // NIL vertex + bipartite graph
        Graph graph = new Graph(nrOfVertices);
        
        // {@code boxes} is sorted based on volume from smallest to biggest
        Arrays.sort(boxes);
        // Then every pair of boxes, where the second in the pair has a bigger
        // volume, is checked and if the two boxes fit, an edge is added.
        for(int smallBox = 0; smallBox < nrOfBoxes - 1; smallBox++){
            for(int bigBox = smallBox + 1; bigBox < nrOfBoxes; bigBox++){
                if(boxes[smallBox].fitsIn(boxes[bigBox])) {
                    // {@code smallBox} has vertex {@code smallBox} + 1
                    // {@code bigBox} has vertex {@code bigBox} + 1 + {@code nrOfBoxes}
                    graph.addEdge(smallBox + 1, bigBox + 1 + nrOfBoxes);
                }          
            }
        }

        return graph;
    }
}
