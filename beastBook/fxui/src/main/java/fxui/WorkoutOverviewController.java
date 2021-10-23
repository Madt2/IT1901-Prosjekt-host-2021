package fxui;

import core.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import core.Workout;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import json.BeastBookPersistence;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class WorkoutOverviewController extends AbstractController{
  @FXML
  private AnchorPane rootPane;

  @FXML
  private MenuItem logout_button;

  @FXML
  private Button back_button;

  @FXML
  private Button openButton;

  @FXML
  private Button deleteButton;

  @FXML
  private Text exceptionFeedback;

  @FXML
  private TableView<Workout> workout_overview = new TableView<Workout>();

  private TableColumn<Workout, String> workoutNameColumn;
  private List<Workout> allWorkouts = new ArrayList<>();
  public static Workout clickedWorkout = new Workout();  
  private Workout workout = new Workout();

  public void initialize() {
    loadTable();
  } 
    
  /**
  * Creates a table view with a column for workout name and
  * adds the users workouts to the table view.
  */
  public void loadTable() {
    setWorkouts();
    workout_overview.getColumns().clear();
    workoutNameColumn = new TableColumn<Workout, String>("Workout name:");
    workoutNameColumn.setCellValueFactory(new PropertyValueFactory<Workout, String>("name"));
    workout_overview.getColumns().add(workoutNameColumn);
    workout_overview.getItems().setAll(allWorkouts);
    setColumnsSize();
  }

  @FXML
  private void workoutSelectedListener() throws IOException {
    workout = workout_overview.getSelectionModel().getSelectedItem();
    if (workout != null) {
      exceptionFeedback.setText("");
      openButton.setDisable(false);
      deleteButton.setDisable(false);
    } else {
      openButton.setDisable(true);
      deleteButton.setDisable(true);
    }
  }

  public void setWorkout(Workout workout) {
    this.workout = workout;
  }

  public Workout getWorkout() {
    return this.workout;
  }

  public void addToAllWorkouts(Workout workout) {
    allWorkouts.add(workout);
  }

  List<Workout> getAllWorkouts() {
    return new ArrayList<>(allWorkouts);
  }

  Workout getTable(int row) {
    return workout_overview.getItems().get(row);
  }

  public TableView<Workout> getWorkoutOverview() {
    return workout_overview;
  }

  private void setColumnsSize() {
    workoutNameColumn.setPrefWidth(150);    
  }

  @Override
  void loadHome() throws IOException {
    super.loadHome();
  }

  @Override
  void loadLogin() throws IOException {
    super.loadLogin();
  }

  @FXML
  void loadWorkout() throws IOException {
    try {
      exceptionFeedback.setText("");
      WorkoutController workoutController = new WorkoutController();
      FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("Workout.fxml"));
      fxmlLoader.setController(workoutController);
      workoutController.setUser(user);
      workoutController.setWorkout(this.getWorkout());
    
      AnchorPane pane =  fxmlLoader.load();
      rootPane.getChildren().setAll(pane);
    } catch (Exception e) {
      openButton.setDisable(true);
      deleteButton.setDisable(true);
      exceptionFeedback.setText("No workout is selected!");
    }
  }

  @FXML
  void deleteWorkout() {
    try { 
      user.removeWorkout(workout);
      loadTable();
      exceptionFeedback.setText("Workout deleted!");
      BeastBookPersistence persistence = new BeastBookPersistence();
      persistence.setSaveFilePath(user.getUserName());
      persistence.saveUser(user);
      
    } catch (Exception e) {
      openButton.setDisable(true);
      deleteButton.setDisable(true);
      exceptionFeedback.setText("No workout is selected!");
    } 
  }
    
  void setUser(User user) {
    this.user = user;
  }

  void setWorkouts() {
    this.allWorkouts = user.getWorkouts();
  }
}
