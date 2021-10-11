package fxui;

import core.User;
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
    private User user;

    public void initialize(){
        setTable();
    } 
    
    public void setTable() {
        setWorkouts();
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

                    setWorkout(clickedRow);
                    try {
                        loadWorkout();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

/*    private void loadClickedWorkout(Workout workout, MouseEvent event) {
        try {
            clickedWorkout = workout;
            loadWorkout(event, workout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @FXML
    void loadHome(ActionEvent event) throws IOException{
        HomeScreenController homeScreenController = new HomeScreenController();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("HomeScreen.fxml"));
        fxmlLoader.setController(homeScreenController);
        homeScreenController.setUser(user);

        AnchorPane pane =  fxmlLoader.load();
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    void loadLogin(ActionEvent event) throws IOException{
        LoginController loginController = new LoginController();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("Login.fxml"));
        fxmlLoader.setController(loginController);

        AnchorPane pane =  fxmlLoader.load();
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    void loadWorkout() throws IOException{
        try {
            WorkoutController workoutController = new WorkoutController();
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("Workout.fxml"));
            fxmlLoader.setController(workoutController);
            workoutController.setUser(user);
            workoutController.setWorkout(this.getWorkout());

            AnchorPane pane =  fxmlLoader.load();
            rootPane.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    void setUser(User user){
        this.user = user;
    }

    void setWorkouts() {
        this.allWorkouts = user.getWorkouts();
    }
}
