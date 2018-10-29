package assignment1;

import java.util.Arrays;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class Assignment1 {
    public static void runAssignment1() {
        // Reading in boxes from standard input
        int nrOfBoxes = IOHandler.getInteger();
        Box[] boxes = readBoxes(nrOfBoxes);

        // Creating bipartite graph
        Graph graph = createGraph(boxes, nrOfBoxes);

        // Algorithm
        int flowValue = FordFulkerson.hopcroftKarp(graph, nrOfBoxes);

        // Printing minimum number of visible boxes
        IOHandler.print(Integer.toString(nrOfBoxes - flowValue));
    }

    private static Box[] readBoxes(int nrOfBoxes) {
        Box[] boxes = new Box[nrOfBoxes]; // Initialize Box array

        for(int i = 0; i < nrOfBoxes; i++) {
            double xLength = IOHandler.getDouble();
            double yLength = IOHandler.getDouble();
            double zLength = IOHandler.getDouble();
            Box box = new Box(xLength, yLength, zLength);
            boxes[i] = box;
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
        // Then every pair of boxes, such that the second in the pair has a bigger
        // volume, is tested and if the two boxes fit, an edge is added
        Arrays.sort(boxes);
        for(int i = 0; i < nrOfBoxes - 1; i++){
            for(int j=i + 1; j < nrOfBoxes; j++){
                if(boxes[i].fitsIn(boxes[j])) {
                    graph.addEdge(i + 1, j + 1 + nrOfBoxes); // Box i has index i + 1 and box
                                                             // j has index j + 1 + nrOfBoxes
                }          
            }
        }

        return graph;
    }
}
