/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Vertex)) return false;
        if(o == this) return true;
        Vertex otherVertex = (Vertex) o;
        return number == otherVertex.getNumber();
        
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + this.number;
        return hash;
    }


    
}
