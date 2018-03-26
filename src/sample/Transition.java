package sample;

//Class to represent the transitions in the FSA
//The current and next states are stored as ints so that the
//FSA can use them as an index in the array list
public class Transition {
    private int current;
    private int next;
    private char symbol;

    //Constructor for the transition
    public Transition(int current, int next, char symbol) {
        this.current = current;
        this.next = next;
        this.symbol = symbol;
    }

    //Getter for the current state
    public int getCurrent() {
        return current;
    }

    //Setter for the current state
    public void setCurrent(int current) {
        this.current = current;
    }

    //Getter for the next state
    public int getNext() {
        return next;
    }

    //Setter for the next state
    public void setNext(int next) {
        this.next = next;
    }

    //Getter for the symbol
    public char getSymbol() {
        return symbol;
    }

    //Setter for the symbol
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
}
