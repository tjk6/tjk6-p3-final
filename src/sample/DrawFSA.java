package sample;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;

import java.util.ArrayList;

//Class for handling all the drawing of the FSA
public class DrawFSA extends Main {
    private static final int LPADDING_START = 160;
    private static final int RPADDING_START = 240;
    private static final int STATE_SPACE = 100;
    private static final int IN_SPACE = 200;
    private static final int CIR_RAD = 20;
    private static final int STATE_DIST = 80;


    //Draws the FSA and returns a group with all the necessary objects
    static Group draw(Automata fsa){
        //Set up Gridpane
        Pane statePane = new Pane();
        //Set the padding
        statePane.setPadding(new Insets(50,100,50,100));
        //Adds all the arrows for the FSA. Done before the circles because of overlap
        statePane.getChildren().addAll(getArrows(fsa));
        //Adds all the circles for the FSA. Done after the arrows to cover any stray lines
        statePane.getChildren().addAll(getCircles(fsa));

        return new Group(statePane);
    }

    //Gets all the arrows for the FSA
    private static Group getArrows(Automata fsa){
        Group tranGroup = new Group();
        //Used for padding the lines going to and from the states on the outside to prevent overlap.
        int lPadding = LPADDING_START; //l and rPadding handle how much farther away the next line going to a state should
        int rPadding = RPADDING_START; //be from another. This prevents confusing overlap of lines.
        int startState = fsa.getStartState();

        //Adds the starting arrow to the group.
        tranGroup.getChildren().add(getStartArrow(startState));
        ArrayList<Transition> transitions = fsa.getTransitions();

        //For each transition, see where the next state is relative to the current state.
        for(Transition currTransition : transitions){
            int currState = currTransition.getCurrent();
            int nextState = currTransition.getNext();
            //Get the number of times this transition has been used with a different symbol.
            //This gets the spacing before the symbol
            int numBefore = getNumBefore(transitions,currTransition);
            //If there is a loop
            if(currState == nextState){
                tranGroup.getChildren().add(getLoopTransition(currState,numBefore,currTransition.getSymbol()));
            }
            //Otherwise, if the next state is right after the current one
            else if(currState+1 == nextState){
                tranGroup.getChildren().add(getNextTransition(currState,nextState,numBefore,currTransition.getSymbol()));
            }
            //Else, if the next state is farther away than that
            else if(currState < nextState){
                tranGroup.getChildren().add(getJumpTransition(currState,nextState,lPadding,currTransition.getSymbol()));
                lPadding += 10;
            }
            //Else, if the next state is before the current state
            else if(currState > nextState){
                tranGroup.getChildren().add(getBackTransition(currState,nextState,rPadding,currTransition.getSymbol()));
                rPadding += 10;
            }
        }
        return tranGroup;
    }

    //Gets all the circles to be added to the group.
    private static Group getCircles(Automata fsa){
        Group stateGroup = new Group();
        int size = fsa.getStates().size();

        for(int i = 0; i < size; i++){
            Circle stateCircle = new Circle(0, 0, CIR_RAD);
            stateCircle.setFill(Color.YELLOW);
            stateCircle.setStroke(Color.BLACK);
            Text stateText = new Text(-6,4,"q" + i);
            Group group = new Group();
            group.setLayoutY((i*STATE_SPACE) + STATE_DIST);
            group.setLayoutX(IN_SPACE);

            group.getChildren().addAll(stateCircle,stateText);

            stateGroup.getChildren().addAll(group);
        }
        return stateGroup;
    }

    //Gets the number of instances this transition was used before this one
    private static int getNumBefore(ArrayList<Transition> transitions,Transition testTran){
        int i = 0;
        int counter = 0;
        while(i < transitions.size()){
            if(transitions.get(i).getCurrent() == testTran.getCurrent() &&
                    transitions.get(i).getNext() == testTran.getNext()){
                if(transitions.get(i).getSymbol() == testTran.getSymbol()){
                    return counter;
                }
                else{
                    counter++;
                }
            }
            i++;
        }
        return 0;
    }

    //Gets the arrow for the start transition
    private static Group getStartArrow(int startState){
        Group startGroup = new Group();

        Line startLine = new Line(150, (startState*STATE_SPACE) + 30, IN_SPACE, (startState*STATE_SPACE) + STATE_DIST);
        Line stArrLeft = new Line(185.86, (startState*STATE_SPACE) + 65.86, 175.86, (startState*STATE_SPACE) + 65.86);
        Line stArrRight = new Line(185.86, (startState*STATE_SPACE) + 65.86, 185.86, (startState*STATE_SPACE) + 55.86);
        startGroup.getChildren().addAll(startLine,stArrLeft,stArrRight);

        return startGroup;
    }

    //Get the arrow for a backwards transition
    private static Group getBackTransition(int currState, int nextState, int rPadding, char tranSymbol){
        Group backGroup = new Group();

        Line currLine = new Line(IN_SPACE, (currState*STATE_SPACE) + STATE_DIST, rPadding, (currState*STATE_SPACE) + STATE_DIST);
        Line nextLine = new Line(IN_SPACE, (nextState*STATE_SPACE) + STATE_DIST, rPadding, (nextState*STATE_SPACE) + STATE_DIST);
        Line fromLine = new Line(rPadding, (currState*STATE_SPACE) + STATE_DIST, rPadding, (nextState*STATE_SPACE) + STATE_DIST);
        //Arrow lines
        Line arrowTop = new Line(220, (nextState*STATE_SPACE) + STATE_DIST, 225, (nextState*STATE_SPACE) + 75);
        Line arrowBottom = new Line(220, (nextState*STATE_SPACE) + STATE_DIST, 225, (nextState*STATE_SPACE) + 85);

        Text symbol = new Text(rPadding+4, (currState*STATE_SPACE) + 40,"" + tranSymbol);
        backGroup.getChildren().addAll(currLine,nextLine,fromLine,arrowTop,arrowBottom,symbol);
        return backGroup;
    }

    //Get the arrow for a transition more than one away (goes around states)
    private static Group getJumpTransition(int currState, int nextState, int lPadding, char tranSymbol){
        Group jumpGroup = new Group();

        Line currLine = new Line(IN_SPACE, (currState*STATE_SPACE) + STATE_DIST, lPadding, (currState*STATE_SPACE) + STATE_DIST);
        Line nextLine = new Line(IN_SPACE, (nextState*STATE_SPACE) + STATE_DIST, lPadding, (nextState*STATE_SPACE) + STATE_DIST);
        Line toLine = new Line(lPadding, (currState*STATE_SPACE) + STATE_DIST, lPadding, (nextState*STATE_SPACE) + STATE_DIST);
        //Arrow lines
        Line arrowTop = new Line(180, (nextState*STATE_SPACE) + STATE_DIST, 175, (nextState*STATE_SPACE) + 75);
        Line arrowBottom = new Line(180, (nextState*STATE_SPACE) + STATE_DIST, 175, (nextState*STATE_SPACE) + 85);

        Text symbol = new Text(lPadding-8, (currState*STATE_SPACE) + STATE_SPACE,"" + tranSymbol);
        jumpGroup.getChildren().addAll(currLine,nextLine,toLine,arrowTop,arrowBottom,symbol);

        return jumpGroup;
    }

    //Gets the arrow for a transition exactly one state away and moves the symbol over the appropriate number of
    // times if more than one transition uses it.
    private static Group getNextTransition(int currState, int nextState, int numBefore, char tranSymbol){
        Group nextGroup = new Group();

        Line currLine = new Line(IN_SPACE, (currState*STATE_SPACE) + STATE_DIST, IN_SPACE, (nextState*STATE_SPACE) + STATE_DIST);
        Line arrowLeft = new Line(IN_SPACE, (nextState*STATE_SPACE) + 60, 195, (nextState*STATE_SPACE) + 55);
        Line arrowRight = new Line(IN_SPACE, (nextState*STATE_SPACE) + 60, 205, (nextState*STATE_SPACE) + 55);
        Text symbol = new Text(205 + (numBefore * 8),(currState * STATE_SPACE) + 130,"" + tranSymbol);
        nextGroup.getChildren().addAll(currLine, arrowRight, arrowLeft, symbol);

        return nextGroup;
    }

    //Gets the arrow for a loop. This is actually a circle that is put behind a state circle.
    private static Group getLoopTransition(int currState, int numBefore, char tranSymbol){
        Group loopGroup = new Group();

        Circle loopCircle = new Circle(235, (currState * STATE_SPACE) + STATE_DIST, 25);
        loopCircle.setFill(Color.TRANSPARENT);
        loopCircle.setStroke(Color.BLACK);
        Text symbol = new Text(265 + (numBefore * 8),(currState * STATE_SPACE) + STATE_DIST,"" + tranSymbol);
        //Arrow lines
        Line arrowLeft = new Line(214.14, (currState*STATE_SPACE) + 65.86, 214.14, (currState*STATE_SPACE) + 55.86);
        Line arrowRight = new Line(214.14, (currState*STATE_SPACE) + 65.86, 224.14, (currState*STATE_SPACE) + 65.86);

        loopGroup.getChildren().addAll(loopCircle,arrowLeft,arrowRight,symbol);

        return loopGroup;
    }

}
