package fxui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.io.IOException;

import core.Workout;
import javafx.scene.layout.AnchorPane;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;
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

    private List<Workout> allWorkouts = new ArrayList<>();
    



    public void initialize(){
       
        setTable();
       
            
       // workout_overview.getItems().setAll(parseWorkoutList(workout));
         
   
    
   // private List<Exercise> parseWorkoutList(Workout workout){

       
        //Exercise e1 = new Exercise("Benkpress", 12, 70, 3, 90);
        //Exercise e2 = new Exercise("Knebøy", 12, 120, 3, 60);

       // workout.addExercise(e1);
       // workout.addExercise(e2);

        //System.out.println(workout.toString());

        //return workout.getExercises();
    } 
    

    public void setTable() {
        
        Workout chest = new Workout("chest");
        Workout biceps = new Workout("biceps");
        Exercise benkpress = new Exercise("Benkpress", 25, 70, 15, 60);
        Exercise kneboy = new Exercise("Knebøy", 25, 70, 15, 60);
        chest.addExercise(benkpress);
        biceps.addExercise(kneboy);
        allWorkouts.add(chest);
        allWorkouts.add(biceps);

        workoutName.setCellValueFactory(new PropertyValueFactory<Workout, String>("name"));
        workout_overview.getItems().setAll(allWorkouts);

        // Source: https://stackoverflow.com/questions/30191264/javafx-tableview-how-to-get-the-row-i-clicked
        workout_overview.setRowFactory(tableView -> {
            TableRow<Workout> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY 
                     && event.getClickCount() == 1) {
        
                    Workout clickedRow = row.getItem();
                    printRow(clickedRow);
                }
            });
            return row ;
        });

    }
    // Used for test reasons for now. printRow will be changed with a function which takes the user to a overview
    // over the clicked workout.
    
    private void printRow(Workout item) {
        System.out.println(item.getName() + item.getExercises());
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
