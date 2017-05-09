/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tgtore;

/**
 *
 * @author Arham Saeed
 */
public class Transition {
    int currentState;
    String input;
    int st;
    Transition(int curr, String in, int state){
        currentState = curr;
        input = in;
        st = state;
    }
}
