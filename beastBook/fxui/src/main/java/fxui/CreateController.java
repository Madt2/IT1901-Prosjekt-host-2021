package fxui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.io.IOException;
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
    private TextField exercise_input;

    @FXML
    private TextField rep_input;

    @FXML
    private TextField weight_input;

    @FXML
    private TextField set_input;

    @FXML
    private TextField rest_input;

    @FXML
    private TextField title_input;

    @FXML
    private Button createWorkout;

    @FXML
    private Button addExercise;

    private Workout workout = new Workout();

    private Exercise exercise;

    @FXML
    void loadHome(ActionEvent event) throws IOException{
        AnchorPane pane =  FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public void initialize(){
        if(workout.getExercises().isEmpty()){
            // TODO If a user has a workout registered, loop through a JSON-file and add a Exercise object
        }
       // workout_table.setItems(exerciseList);
    }


    @FXML
    void addExercise() {
        
        workout.setName(title_input.getText());

        exercise = new Exercise(exercise_input.getText(), 
                                        Integer.valueOf(rep_input.getText()),
                                         Double.valueOf(weight_input.getText()), 
                                         Integer.valueOf(set_input.getText()), 
                                         Integer.valueOf(rest_input.getText()));
                       
        
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
        System.out.println("Test createWorkout-button!!");
    }

}
