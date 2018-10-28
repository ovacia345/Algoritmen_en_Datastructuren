package assignment1;

/**
 *
 * @author N.C.M van Nistelrooij
 * @author chihab
 */
public class Edge {
    
    private final int from ;
    private final int to;

    private int flow;
    private final int capacity;
    
    public Edge(int from, int to){
        this.from = from;
        this.to = to;

        this.flow = 0;
        this.capacity = 1;     
    }
    
    
    public void setFlow(int flow) {
        this.flow = flow;
    }
    
    public int getFlow() {
        return flow;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public int getFrom(){
        return from;
    }
    
    public int getTo() {
        return to;
    }

    @Override
    public String toString() {
        return String.format("%d - %d:\t%d\t%d\n", from, to, flow, capacity);
    }
}
