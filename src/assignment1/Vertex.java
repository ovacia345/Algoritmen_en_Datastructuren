/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment1;

import java.util.LinkedList;

/**
 *
 * @author chihab
 */
public class Vertex {
    
    
    private int number;
    //private Vertex parent; // is this necessary?? better not to use parents because we work with bipartite graphs
    private LinkedList<Edge> neighbours;
    
    public Vertex(int number){
        this.number = number;
        this.neighbours = new LinkedList<Edge>();
    }
    
    public Vertex(int number,LinkedList<Edge> neighbours) {
        this.number = number;
        this.neighbours = neighbours;
        
    }
    
    public int getNumber(){
        return this.number;
    } 
            
            
    public void addNeighbour(Edge e) {
        this.neighbours.add(e);
    }
    public void removeNeighbour(Edge e) {
        this.neighbours.remove(e);
    }
    
    public void removeNeighbour(Vertex from, Vertex to){
        LinkedList<Edge> nb =from.getNeighbours();
        for(Edge e: nb){
            if(e.getTo().equals(to)){
                neighbours.remove(e);
            }
        }

    }
    
    public LinkedList<Edge> getNeighbours() {
        return this.neighbours;
    }
    
//    public Vertex getParent(){
//        return this.parent;
//    }
//    
//    public void setParent(Vertex v) {
//        this.parent = v;
//    }
    
    @Override
    public String toString() {
        return this.number + "";
    }
    
}
