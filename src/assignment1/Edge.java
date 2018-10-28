/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment1;

/**
 *
 * @author chihab
 */
public class Edge {
    
    private Vertex from ;
    private Vertex to;
    private int flow;
    private int capacity;
    
    public Edge(Vertex from, Vertex to){
        this.from = from;
        this.to = to;
        this.flow = 0;
        this.capacity = 1;        
    }
    
    
    public void setFlow(int flow) {
        this.flow = flow;
    }
    
    public void setCapacity(int cap) {
        this.capacity = cap;
    }
    
    public int getFlow() {
        return this.flow;
    }
    
    public int getCapacity() {
        return this.capacity;
    }
    
    public Vertex getFrom(){
        return this.from;
    }
    
    public Vertex getTo() {
        return this.to;
    }
}
