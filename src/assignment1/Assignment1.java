package graphs;

/**
 *
 * @author N.C.M. van Nistelrooij
 * @author C Amghane
 */
public class Assignment1 {
    
    public static int nrOfBoxes;
    public static Box[] Boxes;
    
    public static Box box;
    public static double xLength,yLength,zLength;
    public static Graph graph;
    public static void runAssignment1() {
        
        nrOfBoxes = IOHandler.getIntegers();
        Boxes = new Box[nrOfBoxes];
        for(int i = 0; i< nrOfBoxes; i++) { 
            xLength = IOHandler.getDouble();
            yLength = IOHandler.getDouble();
            zLength = IOHandler.getDouble();
            box = new Box(xLength,yLength,zLength);
            Boxes[i] = box;   
        }  
        
        graph = createGraph();
        graph.printGraph();
        
        
    }
    
    public static Graph createGraph() {
        int nrOfVertices = nrOfBoxes * 2 + 2; // bipartite, +sink +source
        Graph<Integer> g = new Graph<>(nrOfVertices, 1); // firstBox = souce, lastBox = sink;
        int firstBox = 0;
        int lastBox = nrOfVertices-1;
        //source connections  
        for(int i  = 1 ; i < nrOfBoxes + 1; i++) {
            g.addEdge(firstBox, firstBox + i,1);
        }
        //sink connections
        for(int j = lastBox - 1; j > nrOfBoxes ; j--){
            g.addEdge(j, lastBox,1);
        }
        for(int i = 0; i < nrOfBoxes; i++){
            for(int j=0; j < nrOfBoxes; j++){ 
                if(i != j && Boxes[i].fitsIn(Boxes[j])) {
                    g.addEdge(i+1, (j+1) * 2 + nrOfBoxes - (j+1), 1);    // x number of boxes * 2 - nr of current box because of bipartite graph 
                }          
            }
        }
        return g;    
    }
}
