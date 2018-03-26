package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    private static String FSA_FILEPATH = "./src/sample/fsas";
    private static String INPUT_FILEPATH = "./src/sample/inps";

    //Main stage with menu for selection
    @Override
    public void start(Stage primaryStage) throws Exception{
        //Gets the necessary fsa input files
        ArrayList<String> fileList = getLocalFiles(FSA_FILEPATH);
        //Gets the necessary test input files
        ArrayList<String> inputList = getLocalFiles(INPUT_FILEPATH);

        primaryStage.setTitle("Select Files");

        //Dropdown box for the fsa selector
        Text fsaSelectLabel = new Text("FSA File:");
        ChoiceBox<String> fsaFileSelector = new ChoiceBox<>();
        fsaFileSelector.getItems().addAll(fileList);

        //Dropdown box for the file selector
        Text testFileSelectLabel = new Text("Test File:");
        ChoiceBox<String> testFileSelector = new ChoiceBox<>();
        testFileSelector.getItems().addAll(inputList);

        //Button for running
        Button runButton = new Button("Run");
        runButton.setLayoutX(250);
        runButton.setLayoutY(250);

        //Button event.
        runButton.setOnMouseClicked((event -> {
            String fsaFilename = fsaFileSelector.getSelectionModel().getSelectedItem();
            String testFilename = testFileSelector.getSelectionModel().getSelectedItem();
            //Makes sure that there is something in both drop-down boxes before the run button can be pressed
            if(fsaFilename != null && testFilename!= null){
                //Gets the local file location
                Automata testAutomata = new Automata(getFileString(FSA_FILEPATH + "/" + fsaFilename));
                String inputString = getFileString(INPUT_FILEPATH + "/" + testFilename);
                //Runs the automata and returns the state it accepts on. Returns -1 if the fsa is rejected.
                int isComplete = testAutomata.runAutomata(inputString);
                if(isComplete >= 0) {
                    System.out.println("Accepted at state at State " + isComplete);
                    Stage secondaryStage = new Stage();
                    secondaryStage.setTitle("Resulting FSA for " + fsaFilename);
                    Scene fsaScene = new Scene(DrawFSA.draw(testAutomata) );
                    secondaryStage.setScene(fsaScene);
                    secondaryStage.show();
                }
                else if(isComplete == -1) {
                    System.out.println("Rejected.");
                }

            }
        }));

        //Set up Gridpane
        GridPane gp = new GridPane();
        //Set the padding
        gp.setPadding(new Insets(50,100,50,100));
        //Set the gaps
        gp.setVgap(10);
        gp.setHgap(10);
        //Set the grid alignment
        gp.setAlignment(Pos.CENTER);
        //Add the labels to the grid
        gp.add(fsaSelectLabel, 0, 0);
        gp.add(fsaFileSelector, 1, 0);
        gp.add(testFileSelectLabel, 0, 1);
        gp.add(testFileSelector, 1, 1);
        gp.add(runButton, 1, 3);
        primaryStage.setScene(new Scene(gp));
        primaryStage.show();
    }

    //Launch menu from main
    public static void main(String[] args) {
        launch(args);
    }

    //Gets all the file names at the directory
    private static ArrayList<String> getLocalFiles(String filePath) {
        File fsaFolder = new File(filePath);
        File[] fileArray = fsaFolder.listFiles();
        ArrayList<String> fileList = new ArrayList<>();
        if(fileArray != null){
            for(File file : fileArray) {
                fileList.add(file.getName());
            }
        }
        return fileList;
    }

    //Eliminates all white space so that the tokenizer can get the integer of a string without the space stopping it.
    private static String getFileString(String filename){
        String fileString;
        try{
            BufferedReader fileReader = new BufferedReader(new FileReader(filename));
            fileString = fileReader.readLine();
            fileString = fileString.replaceAll("\\s","");
            return fileString;
        }
        catch(IOException e){
            e.getStackTrace();
        }
        return "ERROR: UNABLE TO GET STRING";
    }

}
