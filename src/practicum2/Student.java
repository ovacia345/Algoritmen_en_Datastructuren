/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicum2;

/**
 *
 * @author chihab
 */
public class Student {
    int[] answers;
    Integer score;
    
    public Student(int[] answers, Integer score){
        this.answers = answers;
        this.score = score;
    }
    
    public int[] getAnswers() {
        return this.answers;
    }
    public Integer getScore() {
        return this.score;
    }
}
