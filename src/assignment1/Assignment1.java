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
        long startTime = System.nanoTime();
        Graph graph = createGraph(nrOfBoxes, boxes);
        long graphCreationTime = System.nanoTime();
        System.out.println("time elapsed up untill graph creation " + (graphCreationTime - startTime ));
       
        System.out.println(graph);
        startTime = System.nanoTime();
        Graph flowGraph = FordFulkerson.run(graph, graph.getSource(), graph.getSink());
        long flowGraphTime = System.nanoTime();
        System.out.println("time elapsed untill flowgraph created " + (flowGraphTime- startTime) );
        
        startTime = System.nanoTime();
        int flowValue = FordFulkerson.getFlowValue(flowGraph, 0);
        long flowValueReturnTime = System.nanoTime();
        System.out.println("time for returning flowvalue " + (flowValueReturnTime - startTime) ); 
        
        startTime = System.nanoTime();
        IOHandler.print(Integer.toString(nrOfBoxes - flowValue));
        long printTime = System.nanoTime();
        System.out.println(" print time " + (printTime - startTime));
    }
    
    private static Graph createGraph(int nrOfBoxes, Box[] boxes) {
        int nrOfVertices = nrOfBoxes * 2 + 2; // bipartite, +sink +source
        Graph graph = new Graph(nrOfVertices); // source = souce, sink = sink;
        int source = 0;
        int sink = nrOfVertices-1;
        //source connections  + sink connections
        for(int box  = 1 ; box <= nrOfBoxes; box++) {
            graph.addEdge(graph.getSource(), graph.getVertex(box));
            graph.addEdge(graph.getVertex(box +nrOfBoxes) , graph.getSink());
        }
        //sink connections
//        for(int box = sink - 1; box > nrOfBoxes ; box--){
//            graph.addEdge(box, sink);
//        }
        for(int i = 0; i < nrOfBoxes; i++){
            for(int j=0; j < nrOfBoxes; j++){
                if(i != j && boxes[i].fitsIn(boxes[j])) {
                    graph.addEdge(graph.getVertex(i+1), graph.getVertex(j+1 + nrOfBoxes));    // x number of boxes * 2 - nr of current box because of bipartite graph
                }          
            }
        }
        return graph;
    }
    
    
    
}
