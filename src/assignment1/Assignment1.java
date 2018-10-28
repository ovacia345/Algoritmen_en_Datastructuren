package assignment1;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class Assignment1 {
    public static void runAssignment1() {
        int nrOfBoxes = IOHandler.getInteger();
        Box[] boxes = new Box[nrOfBoxes];
        for(int i = 0; i< nrOfBoxes; i++) { 
            double xLength = IOHandler.getDouble();
            double yLength = IOHandler.getDouble();
            double zLength = IOHandler.getDouble();
            Box box = new Box(xLength,yLength,zLength);
            boxes[i] = box;
        }  
        
        Graph graph = createGraph(nrOfBoxes, boxes);

        int flowValue = HopcroftKarp.flowValue(graph, nrOfBoxes);
        
        IOHandler.print(Integer.toString(nrOfBoxes - flowValue));
    }
    
    private static Graph createGraph(int nrOfBoxes, Box[] boxes) {
        int nrOfVertices = nrOfBoxes * 2 + 2; // bipartite, +sink +source
        Graph graph = new Graph(nrOfVertices); // source = souce, sink = sink;
        int source = 0;
        int sink = nrOfVertices-1;
        //source connections  + sink connections
        for(int box  = 1 ; box <= nrOfBoxes; box++) {
            graph.addEdge(source, box);
            graph.addEdge(box + nrOfBoxes,sink);
        }

        for(int i = 0; i < nrOfBoxes; i++){
            for(int j=0; j < nrOfBoxes; j++){
                if(i != j && boxes[i].fitsIn(boxes[j])) {
                    graph.addEdge(i+1, j+1 + nrOfBoxes);    // x number of boxes * 2 - nr of current box because of bipartite graph
                }          
            }
        }
        return graph;
    }
}
