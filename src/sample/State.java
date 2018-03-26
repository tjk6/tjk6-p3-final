package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;

//Class to represent a state in the FSA
public class State {
    private int stateNum;
    private boolean isAccept;
    private boolean isStart;
    private ArrayList<Transition> transitions;

    //Basic constructor (not used in this program)
    public State(int stateNum, boolean isAccept, boolean isStart, ArrayList<Transition> transitions) {
        this.stateNum = stateNum;
        this.isAccept = isAccept;
        this.isStart = isStart;
        this.transitions = transitions;
    }

    //Constructor used in this program. Transitions have to be initialized in Automata
    public State(int stateNum, boolean isStart) {
        this.stateNum = stateNum;
        this.isAccept = false;
        this.isStart = isStart;
        this.transitions = new ArrayList<>();
    }

    //gets the state number
    public int getStateNum() {
        return stateNum;
    }

    //sets the state number
    public void setStateNum(int stateNum) {
        this.stateNum = stateNum;
    }

    //Checks to see if the state is an accept state
    public boolean isAccept() {
        return isAccept;
    }

    //Sets the accept state
    public void setAccept(boolean accept) {
        isAccept = accept;
    }

    //Checks the to see if the state is a start state
    public boolean isStart() {
        return isStart;
    }

    //Sets the start state
    public void setStart(boolean start) {
        isStart = start;
    }

    //Gets the potential transitions the state has
    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    //Sets the transitions
    public void setTransitions(ArrayList<Transition> transitions) {
        this.transitions = transitions;
    }

    //Gets the next state from the current state.
    int getNextState(char symbol){
        int i = 0;
        Alert rejectedAlert;

        //Makes sure this state has transitions.
        if(transitions != null){
            Transition currentTransition;
            while(i < transitions.size()){
                currentTransition = transitions.get(i);
                //If the current transition in the list has the same current state number as this state, and the symbol
                // matches, get the the next state given by the transition and return it.
                if(currentTransition.getCurrent() == this.stateNum &&
                        currentTransition.getSymbol() == symbol) {
                    return transitions.get(i).getNext();
                }
                i++;
            }
            rejectedAlert = new Alert(Alert.AlertType.ERROR,"REJECTED at state " + this.stateNum
                    + " with symbol " + symbol, ButtonType.OK);
            rejectedAlert.showAndWait();
            System.out.println("Failure at state:" + " " + this.stateNum + ", symbol: " + symbol);
            return -1; //Return that there is no transition for that symbol in this state
        }
        //If getNextState was called on a final state, then this should return rejected because that means that the
        // string had more after an accept state with no other transitions
        else{
            rejectedAlert = new Alert(Alert.AlertType.ERROR,"REJECTED at state " + this.stateNum
                    + " with symbol " + symbol, ButtonType.OK);
            rejectedAlert.showAndWait();
            System.out.println("Failure at state:" + " " + this.stateNum + ", symbol: " + symbol);
            return -1; //Return that the string should be rejected
        }
    }


}
