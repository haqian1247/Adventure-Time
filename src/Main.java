// Name: Joshua Parker
// Email: jjparker5@wisc.edu
// Team: BE
// Role: Front End 2
// TA: Brianna Cochran
// Lecturer: Gary Dahl
// Notes to Grader: implements/uses Streams/lambdas, Dijkstras, CSS, and javaFX
// Necessary Files: javafx-sdk-11.0.2_LinuxX64/, Cs400Graph.java, GraphADT.java, AdventureTime.java,
// Data.java, format.css

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;

/** The hub for the program, upon running, boots up a gui to interact with program. */
public class Main extends Application {
    protected AdventureTime proj = new AdventureTime();
    protected int currentIteration; // # of events in current story
    protected String currentText; // current print out on gui
    protected String currentStory; // only the story elements in a printout
    protected int nearestEnd;
    protected boolean isDone;
    protected String endText =
            "\nYour story has ended. Please either: Cleanup the window to see "
                    + "only the relevant story text, Save your story, Load a previous "
                    + "story, Start a new story by clicking Clear, Generate a Random story, or"
                    + " Quit the program.\n";
    
    /** sets the program to be cleared and prints a starter message */
    protected void clear() {
        currentIteration = 0;
        currentText = "";
        currentStory = "";
        proj = new AdventureTime();
        nearestEnd = 0;
        isDone = false;
        currentText =
                "Please click the A, B, or C button to start a new story or click Load Save"
                        + " to load your most recently saved story. At any point, you may "
                        + "click Save to save your current progress.\n";
    }
    
    /**
     * if no story yet, builds the start of the story and prompts for an initial choice. Otherwise
     * moves to the next event chosen and then prompts for the next event.
     *
     * @param input
     *         what button was pressed (1, 2, or 3)
     */
    protected void advanceStory(int input) {
        // If No current story yet, start story
        if (currentIteration == 0) {
            proj = new AdventureTime();
            currentText = "Start of the Story:\n";
            currentStory = "Start of the Story:\n";
            currentStory += proj.getCurrentEvent().toString() + "\n";
            currentText += proj.getCurrentEvent().toString() + "\n";
            
        } else {
            // if a story has already started, continue story
            try {
                // moves current event to the next based on what button is pressed
                proj.nextEvent(input);
                
            } catch (Exception e) { // checks if an event is mapped to the button and that
                // a button was pressed that has no event mapped to it
                return;
            }
            // update output and story with selection
            currentText += "You selected: " + proj.getCurrentEvent().toString() + "\n";
            
            try {
                // print the result of the decision to story and current text
                printOutCome();
            } catch (Exception e) {
                isDone = true;
            } // couldn't move forward, so story is over
            
        }
        
        ++currentIteration;
        // updates how close the nearest end event is
        nearestEnd = proj.getClosestEndDistance() / 2;
        
        // stops story printouts if the current event was the last in the series
        if (proj.isStoryOver() || isDone) {
            currentStory += endText;
            currentText += endText;
            return;
        }
        // gets prompt for next event and prints it to the gui
        promptStory();
    }
    
    /**
     * Prints the consequence event that follows the decision event to the gui.
     *
     * @throws NoSuchElementException
     *         if current or next event is end.
     */
    private void printOutCome() throws NoSuchElementException {
        proj.nextEvent(1);
        currentStory += proj.getCurrentEvent().toString() + "\n";
        currentText += proj.getCurrentEvent().toString() + "\n";
    }
    
    /** Sets the Story Text to ask for a next step and prints the options. */
    private void promptStory() {
        currentText += "\nPlease choose a next step in the story.\n";
        // gather next events
        ArrayList<String> nextEvents = proj.getNextPossibleEvents();
        // prints each valid next event
        for (int i = 0; i < nextEvents.size(); ++i) {
            char val = 'A';
            val += i;
            currentText += val + ") " + nextEvents.get(i) + "\n";
        }
        currentText += "\n";
    }
    
    /**
     * upon activating, clears the program and randomly generates an entire story and prints it to
     * the screen.
     */
    protected void randomStoryGen() {
        clear();
        Random rand = new Random(); // used for random path choice
        // chooses paths until the story is finished
        while (!proj.isStoryOver() && !isDone) {
            advanceStory((rand.nextInt(3) + 1));
        }
        // print story to gui
        currentText = currentStory;
    }
    
    /**
     * Upon activating, saves the current story to the filesystem in the working directory. Only one
     * save is allowed at a time (the most recent).
     */
    protected void saveStory() {
        try {
            ArrayList<String> ids = new ArrayList<>();
            // converts the Data IDs to strings for Data.save
            proj.getEventsInStory().stream()
                .forEachOrdered((data) -> ids.add(Integer.toString(data.iD)));
            if (ids.size() < 2) {
                currentText += "\nCould not save. Too short or empty.";
                return;
            }
            Data.save(ids);
        } catch (Exception e) {
            currentText += "Could not save. Please retry.\n";
            return;
        }
        currentText +=
                "\nSuccessfully Saved Story! You may load it at a later time by pressing the"
                        + " Load button. Only the most recent save is available.\n";
    }
    
    /**
     * Upon activating, loads the most recent story from the working directory. Will print an error
     * to
     * the gui textfield if fails. Otherwise it will clear the window and print the story, then
     * prompt for a next event or print the end statement.
     */
    protected void loadStory() {
        //clean the gui so it doesn't have extra stuff there.
        clear();
        ArrayList<String> inputIDs;
        
        try {
            inputIDs = Data.load(); // grabs input
            currentIteration = (inputIDs.size() + 1) / 2; // sets the # of events counter ignoring
            // decision events
            if (inputIDs.size() < 2) { // if input too small or invalid
                currentText += "Failed to load or load empty. Please retry.\n";
                return;
            }
            
            // sets the first event in the new Adventure time object
            proj = new AdventureTime();
            // removes first event since it is assumed by  Adventure Time constructor
            // for each remaining id, get the data object associated and put it into the story array.
            inputIDs.stream().skip(1).forEachOrdered(
                    (id) -> {
                        proj.nextEvent(
                                proj.vertices.get(new Data(Integer.parseInt(id), "d", false)).data);
                    });
            
        } catch (Exception e) { // error loading the saved story
            clear();
            currentText += "\n Error loading. Please retry.\n";
            return;
        }
        
        currentText += "Start of Story: \n";
        currentStory = "Start of Story: \n";
        
        // prints the events to the gui
        for (Data events : proj.getEventsInStory()) {
            currentStory += events.toString() + "\n";
            //if story done, don't prompt for next input
        }
        
        if (proj.isStoryOver()) {
            currentStory += endText;
            currentText = currentStory;
            return;
        }
        
        currentText = currentStory;
        nearestEnd = proj.getClosestEndDistance() / 2;
        currentText += "\nLoad Successful! Please continue your story.\n";
        promptStory(); // prompt for user to continue story
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        // sets initial values and text
        clear();
        
        // Middle text field
        Text text = new Text(currentText);
        ScrollPane textWind = new ScrollPane(text);
        // format Middle text scroll pane
        textWind.setFitToWidth(true);
        textWind.setHbarPolicy(ScrollBarPolicy.NEVER);
        text.setWrappingWidth(485);
        
        
        // Top HBox init
        Button save = new Button("Save");
        Button loadSave = new Button("Load Save");
        HBox loadSaveBox = new HBox(save, loadSave);
        // Format Top buttons
        loadSaveBox.setSpacing(5);
        
        // additional top buttons init
        Button randStory = new Button("Truly Random Story");
        Button clear = new Button("Clear");
        Button cleanStory = new Button("Clean up Window");
        HBox clean = new HBox(clear, cleanStory);
        HBox topBit = new HBox(loadSaveBox, clean, randStory);
        // format top grouped together buttons
        topBit.setId("top-buttons");
        clean.setSpacing(5);
        
        // story advancement buttons init
        Button a = new Button("A");
        Button b = new Button("B");
        Button c = new Button("C");
        HBox options = new HBox(a, b, c);
        // Format story advancement buttons
        options.setId("HBox-options");
        a.setId("options");
        b.setId("options");
        c.setId("options");
        
        // Bottom text elements init
        Text eventsToEnd = new Text("Events to Nearest End");
        Text endCountdown = new Text("0");
        Text eventsStart = new Text("Events Since Start");
        Text startCount = new Text("0");
        //format text
        endCountdown.setId("number");
        startCount.setId("number");
        eventsToEnd.setId("static-text");
        eventsStart.setId("static-text");
        
        // group text elements together
        VBox endGroup = new VBox(eventsToEnd, endCountdown);
        VBox fromStart = new VBox(eventsStart, startCount);
        // format VBoxes
        endGroup.setAlignment(Pos.BASELINE_CENTER);
        fromStart.setAlignment(Pos.BASELINE_CENTER);
        
        //Put together the bottom section of the gui
        HBox bottomPane = new HBox(endGroup, options, fromStart);
        bottomPane.setId("bottom-pane");
        
        // Gather elements together and set Main Pane
        BorderPane coreGroup = new BorderPane();
        coreGroup.setTop(topBit);
        coreGroup.setCenter(textWind);
        coreGroup.setBottom(bottomPane);
        
        // create and style the window
        stage.setTitle("Small Text Adventure");
        Scene scene = new Scene(coreGroup, 500, 500);
        stage.setScene(scene);
        stage.setMinWidth(475);
        stage.setMinHeight(150);
        stage.show();
        
        // css style import
        scene.getStylesheets().add("format.css");
        
        // adjust screen wrapping of text box when window resized.
        stage.widthProperty().addListener((observableValue, oldValue, newValue) -> {
            text.setWrappingWidth((newValue.intValue() - 30));
        });
        
        // Button Actions
        
        // sets "Cleanup" button to Clean up the window and makes it just the story without the
        // prompts
        cleanStory.setOnMouseClicked(
                (y) -> {
                    currentText = currentStory;
                    if (currentIteration == 0) {
                        advanceStory(0);
                    } else {
                        if (!proj.isStoryOver() && !isDone) {
                            promptStory();
                        }
                    }
                    // update gui values
                    text.setText(currentText);
                    endCountdown.setText(Integer.toString(nearestEnd));
                    startCount.setText(Integer.toString(currentIteration));
                });
        
        // Sets "Clear" button to wipe the text window and set all values to default
        clear.setOnMouseClicked(
                d -> {
                    clear();
                    //update gui values
                    text.setText(currentText);
                    endCountdown.setText(Integer.toString(nearestEnd));
                    startCount.setText(Integer.toString(currentIteration));
                });
        
        // ABC button actions to advance story
        a.setOnMouseClicked(
                (event) -> {
                    advanceStory(1);
                    //update gui values
                    text.setText(currentText);
                    endCountdown.setText(Integer.toString(nearestEnd));
                    startCount.setText(Integer.toString(currentIteration));
                });
        b.setOnMouseClicked(
                (event) -> {
                    advanceStory(2);
                    // update gui values
                    text.setText(currentText);
                    endCountdown.setText(Integer.toString(nearestEnd));
                    startCount.setText(Integer.toString(currentIteration));
                });
        c.setOnMouseClicked(
                (event) -> {
                    advanceStory(3);
                    //update gui values
                    text.setText(currentText);
                    endCountdown.setText(Integer.toString(nearestEnd));
                    startCount.setText(Integer.toString(currentIteration));
                });
        
        // on click runs the random story program and prints a whole story to screen
        randStory.setOnMouseClicked(
                (event) -> {
                    randomStoryGen();
                    //update gui values
                    text.setText(currentText);
                    endCountdown.setText(Integer.toString(nearestEnd));
                    startCount.setText(Integer.toString(currentIteration));
                });
        
        // on click saves the story to file or prompts to retry save
        save.setOnMouseClicked(
                (event) -> {
                    saveStory();
                    //update gui values
                    text.setText(currentText);
                    endCountdown.setText(Integer.toString(nearestEnd));
                    startCount.setText(Integer.toString(currentIteration));
                });
        
        // on click wipes the screen and loads a new story from save file
        loadSave.setOnMouseClicked(
                (event) -> {
                    loadStory();
                    // update gui values
                    text.setText(currentText);
                    endCountdown.setText(Integer.toString(nearestEnd));
                    startCount.setText(Integer.toString(currentIteration));
                });
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
