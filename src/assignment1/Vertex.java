package assignment1;

/**
 *
 * @author chihab
 * @author N.C.M. van Nistelrooij
 */
public class Vertex {   
    private final int number;
    
    public Vertex(int number){
        this.number = number;
    }
    
    public int getNumber(){
        return this.number;
    } 
    
    @Override
    public String toString() {
        return Integer.toString(number);
    }    
}
