package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static java.lang.System.exit;

//Class that stores all the information of the Automata
class Automata {
    private int startState;
    private ArrayList<State> states;
    private ArrayList<Transition> transitions;
    private char[] alphabet;
    private int[] acceptStates;


    //Takes the string obtained from the file and sets all the variables based on the tokens it gets
    Automata(String filestring){
        ArrayList<String> tokenArray = new ArrayList<>();
        Collections.addAll(tokenArray, filestring.split(";"));

        //Gets the start state from the string
        this.setStartState(tokenArray.get(3));
        //Gets the accept states from the string
        this.setAcceptStates(tokenArray.get(4));
        //Gets the alphabet based on the
        this.setAlphabet(tokenArray.get(1));
        //Sets the states based on the number of states given by the first token
        this.setStates(Integer.parseInt(tokenArray.get(0)));
        //Sets up the transitions
        this.setTransitions(tokenArray.get(2));

    }

    //Sets the transitions based on the transition token
    private void setTransitions(String tranString){
        this.transitions = new ArrayList<>();
        String[] tranParams = new String[3];
        int i;
        //Tokenize the long string of transitions
        for(String tranToken: tranString.split(",")){
            //Must get rid of the '(' and ')' before tokenizing the tokenized string again.
            tranToken = tranToken.substring(tranToken.indexOf('(')+1, tranToken.indexOf(')'));
            i = 0;
            //Tokenize each transition into an array.
            for(String symbol: tranToken.split(":")){
                tranParams[i] = symbol;
                i++;
            }
            //Checks to make sure the character is in this alphabet
            checkIfValid(tranParams[2].charAt(0));
            //If it isn't, then throw an error and exit.
            if(Integer.parseInt(tranParams[0]) < 0 || Integer.parseInt(tranParams[0]) > this.states.size() ||
                    Integer.parseInt(tranParams[1]) < 0 || Integer.parseInt(tranParams[1]) > this.states.size()){
                Alert invalidTransition;
                invalidTransition = new Alert(Alert.AlertType.ERROR,"ERROR: INVALID STATE TRANSITION", ButtonType.OK);
                Optional<ButtonType> result = invalidTransition.showAndWait();
                if(result.get() == ButtonType.OK){
                    exit(0);
                }
            }

            Transition newTransition = new Transition(Integer.parseInt(tranParams[0]),
                    Integer.parseInt(tranParams[1]),tranParams[2].charAt(0));
            this.transitions.add(newTransition);
            //Add this transition to the state in the states list whose
            //stateNum matches the currState param of the transition
            this.states.get(newTransition.getCurrent()).getTransitions().add(newTransition);
        }
    }

    //Initializes the alphabet based on the input string
    private void setAlphabet(String alphaString){
        ArrayList<Character> alphabetList = new ArrayList<>();

        int symbolCount = 0;
        //Get each letter
        for(String symbol: alphaString.split(",")){
            alphabetList.add(symbol.charAt(0));
            symbolCount++;
        }
        char[] tempArray = new char[symbolCount];
        //Copy the array list into an array
        for(int i = 0; i < symbolCount; i++){
            tempArray[i] = alphabetList.get(i);
        }
        this.alphabet = tempArray;

    }

    //Initializes the states based on the number of states given. if numStates is 5, the states will be 0-4
    private void setStates(int numStates){
        //Sets the list of states and initializes the start state
        this.states = new ArrayList<>();
        for(int i = 0; i < numStates; i++){
            State insertState = new State(i, (i == this.startState));
            this.states.add(insertState);
        }
        //Sets the accept states to be true
        for(int accState : this.acceptStates){
            this.states.get(accState).setAccept(true);
        }
    }

    //Sets the accept state of the FSA
    private void setAcceptStates(String acceptString){
            ArrayList<Integer> accState = new ArrayList<>();

        int accCount = 0;
        for(String symbol: acceptString.split(",")){
            accState.add(Integer.parseInt(symbol));
            accCount++;
        }
        int[] accStateArray = new int[accCount];
        int i = 0;
        for(Integer currInt : accState){
            accStateArray[i] = currInt;
            i++;
        }
        this.acceptStates = accStateArray;
    }

    //Sets the start state of the fsa
    private void setStartState(String startString){
        this.startState = Integer.parseInt(startString);
    }

    //Runs the automata.
    int runAutomata(String inputData){
        State currState = this.getStates().get(this.startState);
        int nextNum;
        //Get the next transition of a state based on the character until the end of the character input or getNextState
        // returns negative (indicating that there is not a next state for that character
        int i = 0;
        while(i < inputData.length()){
            if((nextNum = currState.getNextState(inputData.charAt(i))) >= 0){
                currState = this.states.get(nextNum);
            }
            else
                return nextNum;
            i++;
        }
        int finalState = currState.getStateNum();
        //Checks to see if the last state the fsa landed on is an accept state.
        for(int accept : this.acceptStates){
            if(accept == finalState){
                return finalState;
            }
        }
        Alert rejectedAlert;
        rejectedAlert = new Alert(Alert.AlertType.ERROR,"REJECTED at state " + finalState
                + " with character " + inputData.charAt(i-1), ButtonType.OK);
        rejectedAlert.showAndWait();

        return -1;
    }

    //Gets the start state
    int getStartState() {
        return startState;
    }

    //Gets the array list of all the states
    ArrayList<State> getStates() {
        return states;
    }

    //Gets the array list of all the transitions
    ArrayList<Transition> getTransitions() {
        return transitions;
    }

    //Checks to see if the symbol is a valid character
    private void checkIfValid(char symbol){
        for(char alpha : this.alphabet){
            if(alpha == symbol){
                return;
            }
        }
        //If the character is invalid, show an error dialogue and close the program.
        Alert invalidSymbol;
        invalidSymbol = new Alert(Alert.AlertType.ERROR,"ERROR: INVALID SYMBOL " + symbol, ButtonType.OK);
        Optional<ButtonType> result = invalidSymbol.showAndWait();
        if(result.get() == ButtonType.OK){
            exit(0);
        }

    }
}
