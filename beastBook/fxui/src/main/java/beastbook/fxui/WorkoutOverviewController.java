package beastbook.fxui;

import beastbook.core.User;
import beastbook.core.Workout;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controller for the WorkoutOverview screen.
 */
public class WorkoutOverviewController extends AbstractController {
  @FXML
  private AnchorPane rootPane;

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
  private String selectedWorkoutName;

  public void initialize() throws IOException {
    user = user.loadUser(user.getUserName());
    loadTable();
  } 
    
  /**
  * Sets the table view with a column for workout name and
  * adds the users workouts to the table view.
  */
  public void loadTable() {
    workoutOverview.getColumns().clear();
    workoutNameColumn = new TableColumn<Workout, String>("Workout name:");
    workoutNameColumn.setCellValueFactory(new PropertyValueFactory<Workout, String>("name"));
    workoutOverview.getColumns().add(workoutNameColumn);
    workoutOverview.getItems().setAll(user.getWorkouts());
    setColumnsSize();
  }

  /**
  * Listener which registeres if a workout is clicked on in the table view.
  */
  @FXML
  private void workoutSelectedListener() {
    try {
      selectedWorkoutName = workoutOverview.getSelectionModel().getSelectedItem().getName();
      if (selectedWorkoutName != null) {
        exceptionFeedback.setText("");
        openButton.setDisable(false);
        deleteButton.setDisable(false);
      }
    } catch (Exception e) {
      openButton.setDisable(true);
      deleteButton.setDisable(true);
    }
  }

  String getWorkoutName() {
    return selectedWorkoutName;
  }

  void setUser(User user) {
    this.user = user;
  }

  TableView<Workout> getWorkoutOverview() {
    return workoutOverview;
  }

  private void setColumnsSize() {
    workoutNameColumn.setPrefWidth(150);    
  }

  /**
  * Loads the workout which has been selected after Open button is clicked.
  *
  * @param event the event when open button is clicked
  *
  */
  @FXML
  void loadWorkout(ActionEvent event) {
    try {
      exceptionFeedback.setText("");
      WorkoutController workoutController = new WorkoutController();
      FXMLLoader fxmlLoader = new FXMLLoader(
              this.getClass().getResource("/beastbook.fxui/Workout.fxml")
      );
      fxmlLoader.setController(workoutController);
      workoutController.setUser(user);
      workoutController.setWorkoutName(getWorkoutName());
      Parent root = fxmlLoader.load();
      Scene scene = new Scene(root, 600, 500);
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      stage.setScene(scene);
    } catch (Exception e) {
      openButton.setDisable(true);
      deleteButton.setDisable(true);
      exceptionFeedback.setText("No workout is selected!");
    }
  }

  /**
  * Deletes the selected workout from the tableview and the user after Delete button is clicked.
  * 
  */
  @FXML
  void deleteWorkout() {
    try {
      user.removeWorkout(user.getWorkout(getWorkoutName()));
      loadTable();
      exceptionFeedback.setText("Workout deleted!");
      user.saveUser();
      openButton.setDisable(true);
      deleteButton.setDisable(true);
    } catch (IOException e) {
      exceptionFeedback.setText("User could not delete workout!");
    }
  }
}
