package beastbook.fxui;

import beastbook.core.Exceptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controller for the WorkoutOverview screen.
 */
public class WorkoutOverviewController extends AbstractController {

  @FXML
  private Button openButton;

  @FXML
  private Button deleteButton;

  @FXML
  private Text exceptionFeedback;

  @FXML
  private TableView<String> workoutOverview = new TableView<>();
  private TableColumn<String, String> workoutNameColumn;
  private String selectedWorkoutId;
  private Map<String, String> workoutMap;

  public void initialize() {
    workoutMap = service.getWorkoutMap();
    loadTable();
  }

  /**
   * Sets the table view with a column for workout name.
   * Adds the users workouts to the table view.
   */
  private void loadTable() {
    workoutOverview.getColumns().clear();
    workoutNameColumn = new TableColumn<>("Workout name:");
    workoutNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
    workoutOverview.getColumns().add(workoutNameColumn);
    workoutOverview.getItems().setAll(workoutMap.values());
    setColumnProperties();
  }

  /**
   * Listener which registers if a workout is clicked on in the table view.
   */
  @FXML
  private void workoutSelectedListener() {
    try {
      String name = workoutOverview.getSelectionModel().getSelectedItem();
      Iterator<Map.Entry<String, String>> it = workoutMap.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry entry = (Map.Entry) it.next();
        if (entry.getValue().equals(name)) {
          selectedWorkoutId = entry.getKey().toString();
          break;
        }
      }
      if (selectedWorkoutId != null) {
        exceptionFeedback.setText("");
        openButton.setDisable(false);
        deleteButton.setDisable(false);
      }
    } catch (Exception e) {
      openButton.setDisable(true);
      deleteButton.setDisable(true);
    }
  }

  TableView<String> getWorkoutOverview() {
    return workoutOverview;
  }

  /**
  * Sets different properties to the columns.
  * Width, not reorderable and not resizable functionality is set.
  */
  private void setColumnProperties() {
    workoutNameColumn.setPrefWidth(230);
    workoutNameColumn.setReorderable(false);
    workoutNameColumn.setResizable(false);
  }

  /**
   * Loads the workout which has been selected after Open button is clicked.
   *
   * @param event the event when open button is clicked.
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
      workoutController.setService(service);
      workoutController.setWorkoutId(selectedWorkoutId);
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
      service.removeWorkout(selectedWorkoutId);
      workoutMap = service.getWorkoutMap();
      exceptionFeedback.setText("Workout deleted!");
      openButton.setDisable(true);
      deleteButton.setDisable(true);
      loadTable();
    } catch (Exceptions.BadPackageException | Exceptions.ServerException
        | Exceptions.IllegalIdException | URISyntaxException
        | JsonProcessingException e) {
      exceptionFeedback.setText(e.getMessage());
    }
  }
}
