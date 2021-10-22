package fxui;

import core.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.io.IOException;

import core.Workout;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

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
  private TableView<Workout> workout_overview = new TableView<Workout>();

  private TableColumn<Workout, String> workoutNameColumn;
  private List<Workout> allWorkouts = new ArrayList<>();
  public static Workout clickedWorkout = new Workout();  
  private Workout workout = new Workout();

  public void initialize() {
    setTable();
  } 
    
  /**
  * Creates a table view with a column for workout name and
  * adds the users workouts to the table view.
  */
  public void setTable() {
    setWorkouts();
    workout_overview.getColumns().clear();
    workoutNameColumn = new TableColumn<Workout, String>("Workout name:");
    workoutNameColumn.setCellValueFactory(new PropertyValueFactory<Workout, String>("name"));
    workout_overview.getColumns().add(workoutNameColumn);
    workout_overview.getItems().setAll(allWorkouts);
    setColumnsSize();
    getClickedRow();
  }

  /**
  * Registers the workout/row that you have clicked on
  * and uses loadWorkout() to load the workout.
  *
  * @return TableRow that is clicked on
  */
  private TableRow<Workout> getClickedRow() {
    // Source: https://stackoverflow.com/questions/30191264/javafx-tableview-how-to-get-the-row-i-clicked
    workout_overview.setRowFactory(tableView -> {
      TableRow<Workout> row = new TableRow<>();
      row.setOnMouseClicked(event -> {
        if (! row.isEmpty() && event.getButton() == MouseButton.PRIMARY 
            && event.getClickCount() == 1) {
              Workout clickedRow = row.getItem();
              setWorkout(clickedRow);
              try {
                loadWorkout();
              } catch (IOException e) {
                  e.printStackTrace();
                  tableView.getSelectionModel();
                }
        }
    });
      return row;
    });
    return null;
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
  void loadLogin(ActionEvent event) throws IOException {
    super.loadLogin(event);
  }

  @FXML
  void loadWorkout() throws IOException {
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
    
  void setUser(User user) {
    this.user = user;
  }

  void setWorkouts() {
    this.allWorkouts = user.getWorkouts();
  }
}
