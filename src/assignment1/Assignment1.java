package assignment1;

import java.util.ArrayList;
import java.util.List;

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
        List<Edge> visitedEdges = new ArrayList<>();
        List<Vertex> visitedVerts = new ArrayList<>(); 
        FordFulkerson.augmentFlow(graph, visitedEdges, visitedVerts);
//        long graphCreationTime = System.nanoTime();
        //System.out.println("time elapsed up untill graph creation " + (graphCreationTime - startTime ));
       
//       // System.out.println(graph);
//        startTime = System.nanoTime();
//        Graph flowGraph = FordFulkerson.run(graph, graph.getSource(), graph.getSink());
//        long flowGraphTime = System.nanoTime();
//        //System.out.println("time elapsed untill flowgraph created " + (flowGraphTime- startTime) );
//        
//        startTime = System.nanoTime();
        int flowValue = FordFulkerson.getFlowValue(graph);
//        long flowValueReturnTime = System.nanoTime();
//        //System.out.println("time for returning flowvalue " + (flowValueReturnTime - startTime) ); 
        
        startTime = System.nanoTime();
        IOHandler.print(Integer.toString(nrOfBoxes - flowValue));
        long printTime = System.nanoTime();
        //System.out.println(" print time " + (printTime - startTime));
    }
    
    public static Graph createGraph(int nrOfBoxes, Box[] boxes) {
        int nrOfVertices = nrOfBoxes * 2 + 2; // bipartite, +sink +source
        Graph graph = new Graph(nrOfVertices); // source = souce, sink = sink;
        int source = 0;
        int sink = nrOfVertices-1;
        //source connections  + sink connections
        for(int box  = 1 ; box <= nrOfBoxes; box++) {
            graph.addEdge(graph.getSource(), graph.getVertex(box));
            graph.addEdge(graph.getVertex(box +nrOfBoxes) , graph.getSink());
        }
   
        // sorteer dozen klein naar groot <-- kost teveel tijd ..
        // als 1 in 2 past dan hoef ik niet te weten of 1 ook in 3 past
        // als 2 in 3 past dan weet ik dat 1 er ook in past..
        // en omdat de dozen op grootte gesorteerd zijn weet je zeker dat als 1 niet in 2 past, 
        // dan past 1 ook niet in 3 etc..
        
        for(int i = 0; i < nrOfBoxes; i++){
            for(int j=0; j < nrOfBoxes; j++){
                if(i != j && boxes[i].fitsIn(boxes[j])) {
                    // als i in j past dan weet je dat j niet in i past..
                    graph.addEdge(graph.getVertex(i+1), graph.getVertex(j+1 + nrOfBoxes));    // x number of boxes * 2 - nr of current box because of bipartite graph
                }          
            }
        }
        return graph;
    }
    
    
    
}
