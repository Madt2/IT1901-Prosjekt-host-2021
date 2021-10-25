package beastBook.fxui;

import beastBook.core.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import beastBook.core.Workout;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import beastBook.json.BeastBookPersistence;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class WorkoutOverviewController extends AbstractController{
  @FXML
  private AnchorPane rootPane;

  @FXML
  private MenuItem logOutButton;

  @FXML
  private Button backButton;

  @FXML
  private Button openButton;

  @FXML
  private Button deleteButton;

  @FXML
  private Text exceptionFeedback;

  @FXML
  private TableView<Workout> workoutOverview = new TableView<Workout>();

  private TableColumn<Workout, String> workoutNameColumn;
  private List<Workout> allWorkouts = new ArrayList<>();
  public static Workout clickedWorkout = new Workout();  
  private Workout workout = new Workout();
  private BeastBookPersistence persistence = new BeastBookPersistence();

  public void initialize() {
    loadTable();
  } 
    
  /**
  * Creates a table view with a column for workout name and
  * adds the users workouts to the table view.
  */
  public void loadTable() {
    setWorkouts();
    workoutOverview.getColumns().clear();
    workoutNameColumn = new TableColumn<Workout, String>("Workout name:");
    workoutNameColumn.setCellValueFactory(new PropertyValueFactory<Workout, String>("name"));
    workoutOverview.getColumns().add(workoutNameColumn);
    workoutOverview.getItems().setAll(allWorkouts);
    setColumnsSize();
  }

  @FXML
  private void workoutSelectedListener() throws IOException {
    workout = workoutOverview.getSelectionModel().getSelectedItem();
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
    return workoutOverview.getItems().get(row);
  }

  public TableView<Workout> getWorkoutOverview() {
    return workoutOverview;
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
  void deleteWorkout() throws IllegalStateException, IOException {
    user.removeWorkout(workout);
    loadTable();
    exceptionFeedback.setText("Workout deleted!");
    persistence.setSaveFilePath(user.getUserName());
    persistence.saveUser(user);
    openButton.setDisable(true);
    deleteButton.setDisable(true);
  }
    
  void setUser(User user) {
    this.user = user;
  }

  void setWorkouts() {
    this.allWorkouts = user.getWorkouts();
  }
}
