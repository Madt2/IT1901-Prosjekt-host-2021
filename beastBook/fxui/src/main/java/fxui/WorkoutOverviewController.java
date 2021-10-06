package fxui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.io.IOException;

import core.Workout;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

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
    private TableColumn<Workout, String> workoutName;

    private List<Workout> allWorkouts = new ArrayList<>();

    public static Workout clickedWorkout = new Workout();  

    private Workout workout = new Workout();

    public void initialize(){
       
        setTable();
    } 
   

    public Workout getWorkout(){
        return this.workout;
    }
    

    public void setTable() {

        // TODO Get data from JSON-file to load in here. At the moment, we only use "Fake data"
        // to get some data in the GUI.
        Workout MyWorkout = new Workout("My workout");

        Exercise benkpress = new Exercise("Benkpress", 25, 70, 3, 50);
        Exercise kneboy = new Exercise("Knebøy", 35, 200, 5, 60);
        Exercise skulderpress = new Exercise("Skulderpress", 30, 20, 3, 70);
        Exercise bicepsCurl = new Exercise("Biceps curl", 10, 30, 3, 20);
        Exercise flyes = new Exercise("Flyes", 50, 20, 5, 120);

        MyWorkout.addExercise(benkpress);
        MyWorkout.addExercise(kneboy);
        MyWorkout.addExercise(skulderpress);
        MyWorkout.addExercise(bicepsCurl);
        MyWorkout.addExercise(flyes);

        allWorkouts.add(MyWorkout);

        workoutName.setCellValueFactory(new PropertyValueFactory<Workout, String>("name"));
        workout_overview.getItems().setAll(allWorkouts);

        // Source: https://stackoverflow.com/questions/30191264/javafx-tableview-how-to-get-the-row-i-clicked
        workout_overview.setRowFactory(tableView -> {
            TableRow<Workout> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY 
                     && event.getClickCount() == 1) {
                    Workout clickedRow = row.getItem();
                    
                    // NON-STATIC
                    setWorkout(clickedRow);

                    //TODO
                    // Find a way that we do not have to use static variable
                    clickedWorkout = clickedRow;

                    loadClickedWorkout(clickedRow, event);
                }
            });
            return row ;
        });

    }

    public void setWorkout(Workout workout){
        this.workout = workout;
    }
  
    private void loadClickedWorkout(Workout workout, MouseEvent event) {
        
        try {
            clickedWorkout = workout;
            loadWorkout(event, workout);
        } catch (IOException e) {
            e.printStackTrace();
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

    @FXML
    void loadWorkout(MouseEvent event, Workout workout) throws IOException{

        AnchorPane pane =  FXMLLoader.load(getClass().getResource("Workout.fxml"));
        rootPane.getChildren().setAll(pane);

        // TODO Try to get "our own controller" to be loaded in the new pane

       /* FXMLLoader loader = new FXMLLoader();

        WorkoutController wc = new WorkoutController();
        wc.setWorkout(workout);
        System.out.println(wc.getWorkout());

        loader.setController(wc);
        loader.setLocation(getClass().getResource("Workout.fxml"));
        AnchorPane pane =  FXMLLoader.load(loader.getLocation());
        rootPane.getChildren().setAll(pane); 
        */

    }
}
