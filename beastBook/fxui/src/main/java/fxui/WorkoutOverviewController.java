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
    private TableView<Workout> workout_overview = new TableView<Workout>();

    private TableColumn<Workout, String> workoutNameColumn;
    private List<Workout> allWorkouts = new ArrayList<>();
    public static Workout clickedWorkout = new Workout();  
    private Workout workout = new Workout();

    public void initialize(){
        setTable();
    } 
    
    public void setTable() {
        workout_overview.getColumns().clear();
        // TODO Get data from JSON-file to load in here. At the moment, we only use "Fake data" to get some data in the GUI.

        // "FAKE DATA"
        //setFakeData();
        workoutNameColumn = new TableColumn<Workout, String>("Workout name:");
        workoutNameColumn.setCellValueFactory(new PropertyValueFactory<Workout, String>("name"));
        workout_overview.getColumns().add(workoutNameColumn);
        workout_overview.getItems().setAll(allWorkouts);
        setColumnsSize();
        getClickedRow();
    }

    private TableRow<Workout> getClickedRow(){
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
            return row;
        });
        return null;
    }


    private void setFakeData(){
        Workout MyWorkout = new Workout("My workout");
        Workout MyWorkout2 = new Workout("My workout 2 Empty");

        Exercise benkpress = new Exercise("Benkpress", 12, 70, 3, 50);
        Exercise kneboy = new Exercise("Knebøy", 8, 200, 5, 60);
        Exercise skulderpress = new Exercise("Skulderpress", 12, 20, 3, 70);
        Exercise bicepsCurl = new Exercise("Biceps curl", 10, 30, 3, 20);
        Exercise flyes = new Exercise("Flyes", 8, 20, 5, 120);

        MyWorkout.addExercise(benkpress);
        MyWorkout.addExercise(kneboy);
        MyWorkout.addExercise(skulderpress);
        MyWorkout.addExercise(bicepsCurl);
        MyWorkout.addExercise(flyes);

        allWorkouts.add(MyWorkout);
        allWorkouts.add(MyWorkout2);
    }

    public void setWorkout(Workout workout){
        this.workout = workout;
    }

    public Workout getWorkout(){
        return this.workout;
    }

    public void addToAllWorkouts(Workout workout){
        allWorkouts.add(workout);
    }

    // for testing
    public List<Workout> getAllWorkouts() {
        return new ArrayList<>(allWorkouts);
    }

    // for testing
    public Workout getTable(int row){
        return workout_overview.getItems().get(row);
    }

    public TableView<Workout> getWorkoutOverview(){
        return workout_overview;
    }

    private void setColumnsSize(){
        workoutNameColumn.setPrefWidth(150);    
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
