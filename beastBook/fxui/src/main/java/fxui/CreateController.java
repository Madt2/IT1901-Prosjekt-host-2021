package fxui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.List;

import core.Workout;
import core.Exercise;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;






public class CreateController {

  
    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<Exercise> workout_table;


    @FXML
    public TableColumn<Exercise, String> exerciseName;

    @FXML
    public TableColumn<Exercise, String> repGoal;

    @FXML
    public TableColumn<Exercise, String> weight;

    @FXML
    public TableColumn<Exercise, String> sets;

    @FXML
    public TableColumn<Exercise, String> restTime;

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
    private Button addExercise;

    private Workout workout = new Workout();

    private Exercise exercise;

    // Test boolean until user is implemented. If true, use "fake" data 
    private boolean fakeUser = true;

    @FXML
    void loadHome(ActionEvent event) throws IOException{
        AnchorPane pane =  FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void initialize(){
        if(workout.getExercises().isEmpty()){
            // TODO If a user has a workout registered, loop through a JSON-file and add a Exercise object
        }
    
        if(fakeUser){
   
           exerciseName.setCellValueFactory(new PropertyValueFactory<Exercise, String>("exerciseName"));
           repGoal.setCellValueFactory(new PropertyValueFactory<Exercise, String>("repGoal"));
           weight.setCellValueFactory(new PropertyValueFactory<Exercise, String>("weight"));
           sets.setCellValueFactory(new PropertyValueFactory<Exercise, String>("sets"));
           restTime.setCellValueFactory(new PropertyValueFactory<Exercise, String>("restTime"));
            
            workout_table.getItems().setAll(parseExerciseList());
         
        }
    }

    private List<Exercise> parseExerciseList(){

        // TODO Loop through a file to read from, and make exercise object with the info from 
        // each line. 

        Exercise e1 = new Exercise("Benkpress", 12, 70, 3, 90);
        Exercise e2 = new Exercise("Knebøy", 12, 120, 3, 60);

        workout.addExercise(e1);
        workout.addExercise(e2);

        System.out.println(workout.toString());

        return workout.getExercises();
    }

    @FXML
    void addExercise() {
        
        createButton.setDisable(false);

        workout.setName(titleInput.getText());
        
        exercise = new Exercise(exerciseNameInput.getText(), 
                                        Integer.valueOf(repsInput.getText()),
                                         Double.valueOf(weigthInput.getText()), 
                                         Integer.valueOf(setsInput.getText()), 
                                         Integer.valueOf(restInput.getText()));
                   
        
        exerciseName.setCellValueFactory(c -> new SimpleStringProperty(new String(exercise.getExerciseName())));
        repGoal.setCellValueFactory(c -> new SimpleStringProperty(new String(String.valueOf(exercise.getRepGoal()))));
        weight.setCellValueFactory(c -> new SimpleStringProperty(new String(String.valueOf(exercise.getWeight()))));
        sets.setCellValueFactory(c -> new SimpleStringProperty(new String(String.valueOf(exercise.getSets()))));
        restTime.setCellValueFactory(c -> new SimpleStringProperty(new String(String.valueOf(exercise.getRestTime()))));

        workout.addExercise(exercise);
        workout_table.getItems().add(exercise);
        System.out.println(workout.getExercises());       
    }



    @FXML
    void createWorkout() {
        if(titleInput.getText() == null || titleInput.getText().equals("")){
            System.err.println("Input title is empty, please enter name to workout");
        }
        else {
            try {
                workout.saveWorkout();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

}
