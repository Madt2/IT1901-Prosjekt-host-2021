package fxui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.event.ActionEvent;
import java.io.IOException;

import core.Workout;
import core.Exercise;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CreateController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private TableView<Exercise> workout_table;

    @FXML
    private Text exceptionFeedback;

    @FXML
    private Button back_button;

    @FXML
    private TextField exerciseNameInput;

    @FXML
    private TextField repsInput;

    @FXML
    private TextField weigthInput;

    @FXML
    private TextField setsInput;

    @FXML
    private TextField restInput;

    @FXML
    private TextField titleInput;

    @FXML
    private Button createButton;

    @FXML
    private Button loadButton;

    @FXML
    private Button addExercise;

    private Workout workout = new Workout();
    private Exercise exercise;
    private TableColumn<Exercise, String> exerciseNameColumn;
    private TableColumn<Exercise, Integer> repGoalColumn;
    private TableColumn<Exercise, Float> weightColum;
    private TableColumn<Exercise, Integer> setsColumn;
    private TableColumn<Exercise, Integer> restTimeColumn;

    public void initialize(){
        setTable();
    } 

    /**
     * 
     * Sets the workout table columns. Clears the columns first, to avoid duplicate columns.
     * After the columns are created, they are added to the table view. 
     */
    public void setTable() {
        workout_table.getColumns().clear();
        
        exerciseNameColumn = new TableColumn<Exercise, String>("Exercise name");
        exerciseNameColumn.setCellValueFactory(new PropertyValueFactory<Exercise, String>("exerciseName"));
        
        repGoalColumn = new TableColumn<Exercise, Integer>("Rep goal");
        repGoalColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("repGoal"));

        weightColum = new TableColumn<Exercise, Float>("Weight");
        weightColum.setCellValueFactory(new PropertyValueFactory<Exercise, Float>("weight"));

        setsColumn = new TableColumn<Exercise, Integer>("Nr of sets");
        setsColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("sets"));

        restTimeColumn = new TableColumn<Exercise, Integer>("Rest time");
        restTimeColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("restTime"));
       
        workout_table.getColumns().add(exerciseNameColumn);
        workout_table.getColumns().add(repGoalColumn);
        workout_table.getColumns().add(weightColum);
        workout_table.getColumns().add(setsColumn);
        workout_table.getColumns().add(restTimeColumn);
        setColumnsSize();

        workout_table.getItems().setAll(workout.getExercises());
    }

    /**
     * 
     * Resizes the width of the columns
     *  
     */
    private void setColumnsSize(){
        exerciseNameColumn.setPrefWidth(100);        
        repGoalColumn.setPrefWidth(75);
        weightColum.setPrefWidth(75);
        setsColumn.setPrefWidth(75);
        restTimeColumn.setPrefWidth(75);
    }

    /**
     *
     * @param row the row you want to have access to / get an Exercise object from. int row 0 is the first row,  int row 1 is the second row and so on. Mainly used for test reasons
     * @return the Exercise object on the the requested row 
     */
    public Exercise getTable(int row){
        return workout_table.getItems().get(row);
    }

    /**
     *
     * @return the workout. Mainly used for test reasons
     */
    public Workout getWorkout(){
        return workout;
    }

    /**
     *
     *  Runs when the "Add exercise" button is clicked. If all the input fields are in the correct format, a  Exercise object is made with the
     *  input fields data. The exercise object is then added to the workout object and its list over exercises. The workout table is then
     *  "reloaded" with the new exercise added to the list.
     *  
     *  If the input fields are not in the correct format, the method catches the Exepction. A text with red color appears on the screen with 
     *  a message to the user saying that the exercise could not be added (because of wrong inputs). The text disappears when a exercise is added successfully. 
     */
    @FXML
    void addExercise() {
        createButton.setDisable(false);
        workout.setName(titleInput.getText());

        try{
            exercise = new Exercise(exerciseNameInput.getText(), 
            Integer.valueOf(repsInput.getText()),
            Double.valueOf(weigthInput.getText()), 
            Integer.valueOf(setsInput.getText()), 
            Integer.valueOf(restInput.getText()));
            
            workout.addExercise(exercise);
            workout_table.getItems().add(exercise);   
            exceptionFeedback.setText("");
        }
   
    catch(NumberFormatException i){
        exceptionFeedback.setText("Value can not be in string format, must be number");
    }

    catch (Exception e) {
        exceptionFeedback.setText(e.getMessage());
        }
        
    }
    
    /**
     * Loads a workout using title input in GUI
     * If no title input is given, an error message is displayed in GUI
     * If no file found with given title, an error message is displayed in GUI
     *
     * @param event When Load Workout button is clicked in GUI, loadWorkout() is fired
     */
    @FXML
    void loadWorkout(ActionEvent event) {
        
        if (titleInput.getText().equals("") || titleInput.getText() == null) {
            exceptionFeedback.setText("Missing Title!");
            return;
        }
        String filename = titleInput.getText();
        try {
            workout = new Workout();
            workout.loadWorkout(filename);
            setTable();
            exceptionFeedback.setText("");
        } catch (Exception e) {
            System.err.println(e);
            exceptionFeedback.setText("Workout not found!");
        }

    }

    /**
     * Creates a workout and saves it as a file with input given in GUI
     * If no title input is given, an error message is displayed in GUI
     * If an error occurs in saveWorkout, an error message is displayed in GUI
     *
     * @param event When Create Workout button is clicked in GUI, createWorkout() is fired
     */
    @FXML
    void createWorkout(ActionEvent event) {
        workout.setName(titleInput.getText());

        if(titleInput.getText() == null || titleInput.getText().equals("")){
            System.err.println("Input title is empty, please enter name to workout");
            exceptionFeedback.setText("Missing Title!");
        }
        else {
            try {
                workout.saveWorkout();
                exceptionFeedback.setText("");
            } catch (Exception e) {
                System.err.println(e);
                exceptionFeedback.setText("Save Workout failed!");
            }
        }
    }

    @FXML
    void loadHome(ActionEvent event) throws IOException{
        AnchorPane pane =  FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    void loadLogin(ActionEvent event) throws IOException{
        AnchorPane pane =  FXMLLoader.load(getClass().getResource("Login.fxml"));
        rootPane.getChildren().setAll(pane);
    }

}
