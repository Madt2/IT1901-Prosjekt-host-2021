package fxui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.io.IOException;

import core.Workout;
import javafx.scene.layout.AnchorPane;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import core.Exercise;

public class WorkoutOverviewController {


    @FXML
    private AnchorPane rootPane;

    @FXML
    private MenuItem logout_button;

    @FXML
    private Button back_button;

    @FXML
    private TableView<Workout> workout_overview;

    @FXML
    public TableColumn<Workout, String> workoutName;


    public void initialize(){
       
        workoutName.setCellValueFactory(new PropertyValueFactory<Workout, String>("workoutName"));
       
            
       // workout_overview.getItems().setAll(parseWorkoutList(workout));
         
   
    
   // private List<Exercise> parseWorkoutList(Workout workout){

       
        //Exercise e1 = new Exercise("Benkpress", 12, 70, 3, 90);
        //Exercise e2 = new Exercise("Knebøy", 12, 120, 3, 60);

       // workout.addExercise(e1);
       // workout.addExercise(e2);

        //System.out.println(workout.toString());

        //return workout.getExercises();
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
