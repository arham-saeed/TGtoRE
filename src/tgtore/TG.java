/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tgtore;

import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;
/**
 *
 * @author Arham Saeed
 */
public class TG {
    Transition[][] TT;
    int[] IS;
    int[] FS;
    int noState;
    Scanner in = new Scanner(System.in);
    public void makeGraph(){
        System.out.println("Number of States: ");
        noState = in.nextInt() + 2;         //adding two for the initial and final state
        
        System.out.println("No. of Initial States : ");
        int noInitialState = in.nextInt();
        IS = new int[noInitialState];
        for (int i = 0; i < noInitialState; i++) {
            System.out.println("Enter Initial State "+(i)+": ");
            IS[i] = in.nextInt();
        }
        
        System.out.println("No. of Final States: ");
        int noFinalState = in.nextInt();
        FS = new int[noFinalState];
        for (int i = 0; i < noFinalState; i++) {
            System.out.println("Enter Final State "+(i)+": ");
            FS[i] = in.nextInt();
        }
        
        TT = new Transition[noState][];             //creating jagged array of object transition
        
        for (int i = 0; i < noState-2; i++) {       //-2 bc last two states are for initial and final
            System.out.println();
            System.out.print("No. of transition of state "+(i)+": ");
            int nt = in.nextInt();
            TT[i] = new Transition[noState];             //array of array      //adding one for final state
            for (int j = 0; j < nt; j++) {
                System.out.println("Transition "+(j)+"?");
                System.out.print("\tEnter input string = ");
                String inputString = in.next();
                System.out.print("\tEnter state = ");
                int state = in.nextInt();
                if (TT[i][state] == null) {
                    TT[i][state] = new Transition(i, inputString, state);
                }
                else{
                    if (TT[i][state].input != null) {
                        TT[i][state].input = "("+TT[i][state].input+"+"+inputString+")";
                    }else
                        TT[i][state].input+=inputString;
                }
            }
        }
        makeInitialState(noInitialState);
        makeFinalState(noFinalState);
    }
    public String getRe(){
        String RE="";
        for (int i = 0; i < TT.length-2; i++) {
            removeStates(i);
        }
        for (int i = 0; i < TT[TT.length-2].length; i++) {
            if (TT[TT.length-2][i] != null) {
                if (RE != "") {
                    RE+="+";
                }
                RE+=TT[TT.length-2][i].input;
            }
        }
        return RE;
    }
    private void makeInitialState(int noInitialState){
        TT[TT.length-2] = new Transition[noState];
        for (int i = 0; i < noInitialState; i++) {
            TT[TT.length-2][IS[i]] = new Transition(TT.length-2, "",IS[i]);
        }
    }
    private void makeFinalState(int noFinalState){
        for (int i = 0; i < noFinalState; i++) {
            TT[FS[i]][TT[FS[i]].length-1] = new Transition(FS[i], "",TT.length-1);
        }
    }
    public ArrayList findIncomingEdges(int state){
        ArrayList<Transition> incomingEdges = new ArrayList<>();
        for (int i = 0; i < TT.length-1; i++) {
            if(TT[i] != null){
                for (int j = 0; j < TT[i].length; j++) {
                   if (TT[i][j] != null && i != state && TT[i][j].st == state) {   //checking if the soure state is different than the 
                       incomingEdges.add(TT[i][j]);                                //destination state because loop condition will apply on it
                       TT[i][j] = null;
                   }
                }
            }
        }
        return incomingEdges;
    }
    public String findLoops(int state){
        String temp = "";
        for (int i = 0; i < TT[state].length; i++) {
            if (TT[state][i] != null && TT[state][i].st == state) {
                if (temp != "") {
                    temp += "+";
                }
                temp += TT[state][i].input;
                TT[state][i] = null;
            }
        }
        if (temp.startsWith("(")) {
            temp = temp+"*";
        }
        else if(temp != "") {
            temp = "("+temp+")*";
        }
        return temp;
    }
    public void removeStates(int state){
        String loop = findLoops(state);
        ArrayList<Transition> incomingEdges = findIncomingEdges(state);
        for (int i = 0; i < incomingEdges.size(); i++) {
            for (int j = 0; j < TT[state].length; j++) {
                if(TT[state] != null && TT[state][j] != null){
                    if (TT[incomingEdges.get(i).currentState][TT[state][j].st] == null) {
                        TT[incomingEdges.get(i).currentState][TT[state][j].st] = new Transition(incomingEdges.get(i).currentState,incomingEdges.get(i).input +
                                                 loop+TT[state][j].input,TT[state][j].st);
                    }
                    else{
                        
                        TT[incomingEdges.get(i).currentState][TT[state][j].st].input="("+TT[incomingEdges.get(i).currentState][TT[state][j].st].input
                                +"+" + incomingEdges.get(i).input +
                                                 loop+TT[state][j].input+")";
                    }
                }
            }
        }
        TT[state] = null;
    }
}
